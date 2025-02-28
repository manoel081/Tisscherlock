package com.mv.tisscherlock.controller;

import com.mv.tisscherlock.model.ValidationResult;
import com.mv.tisscherlock.service.XMLAnalyzerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")  // Permite requisições do front-end em localhost:4200
public class XMLAnalyzerController {

    private static final Logger LOGGER = Logger.getLogger(XMLAnalyzerController.class.getName());

    @Autowired
    private XMLAnalyzerService xmlAnalyzerService;

    /**
     * Endpoint para validar um arquivo XML enviado pelo usuário.
     *
     * @param xmlFile O arquivo XML a ser validado.
     * @param quantidadeEsperada A quantidade esperada de números de carteira.
     * @return A resposta de validação com o resultado.
     */
    @PostMapping("/validar-xml")
    public CompletableFuture<ResponseEntity<ValidationResult>> validateXML(
            @RequestParam("file") MultipartFile xmlFile,
            @RequestParam(value = "quantidadeEsperada", defaultValue = "10") int quantidadeEsperada) {

        LOGGER.info("Requisição de validação de XML recebida.");

        // Verificar se o arquivo foi enviado
        if (xmlFile.isEmpty()) {
            LOGGER.warning("Nenhum arquivo XML foi enviado.");
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body(
                new ValidationResult("Por favor, envie um arquivo XML.", Set.of(), null, null, null, null)
            ));
        }

        // Verificação de tipo de arquivo (MIME Type)
        if (!xmlFile.getContentType().equals("application/xml") && !xmlFile.getContentType().equals("text/xml")) {
            LOGGER.warning("O arquivo enviado não é um XML válido.");
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body(
                new ValidationResult("O arquivo enviado não é um XML válido.", Set.of(), null, null, null, null)
            ));
        }

        // Processa a requisição de validação de XML de forma assíncrona
        return xmlAnalyzerService.validateXMLAsync(xmlFile)
            .thenApply(result -> {
                // Verificar se o XML foi validado com sucesso
                if (result.isValid()) {
                    return ResponseEntity.ok(result);  // Se o XML for válido, retorna um código 200 (OK)
                } else {
                    return ResponseEntity.badRequest().body(result);  // Se houver erros, retorna um código 400 (Bad Request)
                }
            })
            .exceptionally(ex -> {
                // Caso ocorra algum erro inesperado
                LOGGER.severe("Erro durante o processamento do XML: " + ex.getMessage());
                // Retorna erro 500 em caso de falha inesperada
                return ResponseEntity.status(500).body(
                        new ValidationResult("Erro inesperado ao processar o arquivo.", Set.of(), null, null, null, null)
                );
            });
    }
}
