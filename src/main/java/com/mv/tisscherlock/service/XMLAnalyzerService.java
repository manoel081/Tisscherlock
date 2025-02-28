package com.mv.tisscherlock.service;

import com.mv.tisscherlock.model.ValidationResult;
import com.mv.tisscherlock.model.FinancialResults;
import com.mv.tisscherlock.utils.XMLValidator;
import com.mv.tisscherlock.utils.XMLUtils;
import com.mv.tisscherlock.utils.XSDDownloader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.scheduling.annotation.Async;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

@Service
public class XMLAnalyzerService {

    private static final Logger LOGGER = Logger.getLogger(XMLAnalyzerService.class.getName());

    /**
     * Valida o XML enviado pelo usuário de forma assíncrona.
     * 
     * @param xmlFile O arquivo XML a ser validado.
     * @return CompletableFuture<ValidationResult> com o resultado da validação.
     */
    @Async
    public CompletableFuture<ValidationResult> validateXMLAsync(MultipartFile xmlFile) {
        LOGGER.info("Iniciando validação assíncrona do XML...");

        try {
            // Salva o arquivo XML temporariamente no sistema
            File tempXMLFile = File.createTempFile("uploaded-", ".xml");
            xmlFile.transferTo(tempXMLFile);

            LOGGER.info("Arquivo XML salvo com sucesso: " + tempXMLFile.getAbsolutePath());

            // Identifica a versão do XML
            String version = XMLUtils.identificarVersao(tempXMLFile.getPath());
            LOGGER.info("Versão identificada no XML: " + version);

            // Recupera o XSD diretamente da ANS como InputStream
            InputStream xsdInputStream = XSDDownloader.downloadXSD(version);
            LOGGER.info("XSD recuperado com sucesso.");

            // Verificações de estrutura e erros no XML
            List<String> tagsObrigatorias = XMLUtils.verificarTagsObrigatorias(tempXMLFile.getPath(), "ENVIO");
            List<String> tagsNaoFechadas = XMLUtils.verificarTagsNaoFechadas(tempXMLFile.getPath());
            List<String> errosDigitacao = XMLUtils.verificarErrosDigitacao(tempXMLFile.getPath());

            // Combine todos os erros
            tagsObrigatorias.addAll(tagsNaoFechadas);
            tagsObrigatorias.addAll(errosDigitacao);

            // Verificação de valores no XML
            List<String> errosValores = XMLUtils.verificarValores(tempXMLFile.getPath());
            tagsObrigatorias.addAll(errosValores);

            // Verificação da quantidade de números de carteira
            int quantidadeEsperada = 10; // Defina a quantidade esperada de números de carteira
            List<String> errosCarteira = XMLUtils.verificarNumerosCarteira(tempXMLFile.getPath(), quantidadeEsperada);
            tagsObrigatorias.addAll(errosCarteira);

            // Cria o ValidationResult com os resultados
            Set<String> erros = new HashSet<>(tagsObrigatorias);

            // Valida o XML contra o XSD recuperado
            boolean isValid = XMLValidator.validateXML(tempXMLFile.getPath(), xsdInputStream);

            // Extrair os valores financeiros diretamente do XML
            double totalLiberado = 0.0;
            double totalGlosa = 0.0;

            // Buscar os valores de totalLiberado e totalGlosa diretamente no XML
            List<String> totalLiberadoTags = XMLUtils.buscarValorXML(tempXMLFile.getPath(), "ans:valorLiberado");
            List<String> totalGlosaTags = XMLUtils.buscarValorXML(tempXMLFile.getPath(), "ans:valorGlosa");

            if (!totalLiberadoTags.isEmpty()) {
                totalLiberado = Double.parseDouble(totalLiberadoTags.get(0)); // Supondo que o valor é o primeiro encontrado
            }

            if (!totalGlosaTags.isEmpty()) {
                totalGlosa = Double.parseDouble(totalGlosaTags.get(0)); // Supondo que o valor é o primeiro encontrado
            }

            // Cálculo da discrepância
            double discrepancia = totalLiberado - totalGlosa;

            // Formatação monetária
            String totalLiberadoFormatted = String.format("R$ %.2f", totalLiberado);
            String totalGlosaFormatted = totalGlosa != 0 ? String.format("R$ %.2f", totalGlosa) : null;
            String discrepanciaFormatted = discrepancia != 0 ? String.format("R$ %.2f", discrepancia) : null;

            // Criação do objeto FinancialResults com os valores extraídos
            FinancialResults financialResults = new FinancialResults(totalLiberado, totalGlosa, discrepancia);

            // Retorna o resultado da validação com os dados financeiros
            ValidationResult result = isValid && erros.isEmpty() ?
                    new ValidationResult("O XML foi validado com sucesso.", erros, financialResults, totalLiberadoFormatted, totalGlosaFormatted, discrepanciaFormatted) :
                    new ValidationResult("O XML é inválido, por favor, verifique os erros.", erros, financialResults, totalLiberadoFormatted, totalGlosaFormatted, discrepanciaFormatted);

            // Retorna o CompletableFuture com o ValidationResult
            return CompletableFuture.completedFuture(result);

        } catch (Exception e) {
            LOGGER.severe("Erro ao processar o arquivo XML: " + e.getMessage());
            // Retorna o CompletableFuture com o erro
            ValidationResult errorResult = new ValidationResult("Erro inesperado ao processar o arquivo: " + e.getMessage(), Set.of(), null, null, null, null);
            return CompletableFuture.completedFuture(errorResult);
        }
    }
}
