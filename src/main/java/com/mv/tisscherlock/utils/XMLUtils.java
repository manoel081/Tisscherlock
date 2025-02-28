package com.mv.tisscherlock.utils;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class XMLUtils {

    private static final Logger LOGGER = Logger.getLogger(XMLUtils.class.getName());

    /**
     * Identifica a versão do XML, buscando a tag <ans:versaoPadrao>.
     * 
     * @param xmlFilePath O caminho do arquivo XML.
     * @return A versão identificada no XML.
     * @throws Exception Se a tag de versão não for encontrada.
     */
    public static String identificarVersao(String xmlFilePath) throws Exception {
        File xmlFile = new File(xmlFilePath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        // Buscando a tag ans:versaoPadrao para obter a versão
        NodeList nodes = doc.getElementsByTagName("ans:versaoPadrao");
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent(); // Retorna o conteúdo da tag
        }
        throw new Exception("Tag <ans:versaoPadrao> não encontrada no XML.");
    }

    /**
     * Busca os valores de uma tag específica no XML.
     * 
     * @param xmlFilePath O caminho do arquivo XML.
     * @param tagName O nome da tag a ser buscada.
     * @return Lista de valores encontrados para a tag.
     */
    public static List<String> buscarValorXML(String xmlFilePath, String tagName) {
        List<String> valores = new ArrayList<>();
        try {
            File xmlFile = new File(xmlFilePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            // Busca os elementos da tag especificada
            NodeList nodes = doc.getElementsByTagName(tagName);
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    valores.add(node.getTextContent());
                }
            }
        } catch (Exception e) {
            LOGGER.severe("Erro ao buscar valor de tag no XML: " + e.getMessage());
        }
        return valores;
    }

    /**
     * Verifica a quantidade de números de carteira no XML e compara com a quantidade esperada.
     * 
     * @param xmlFilePath O caminho do arquivo XML.
     * @param quantidadeEsperada A quantidade de números esperados.
     * @return Lista de erros encontrados com exemplo de correção.
     */
    public static List<String> verificarNumerosCarteira(String xmlFilePath, int quantidadeEsperada) {
        List<String> erros = new ArrayList<>();
        try {
            // Usando o SAXParser para detectar as linhas e os números de carteira
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            CarteiraHandler handler = new CarteiraHandler(quantidadeEsperada);
            parser.parse(new File(xmlFilePath), handler);

            // Recebe os erros de quantidade de números de carteira
            erros.addAll(handler.getErros());
        } catch (Exception e) {
            LOGGER.severe("Erro ao verificar números de carteira: " + e.getMessage());
            erros.add("Erro ao verificar os números da carteira.");
        }
        return erros;
    }

    /**
     * Classe personalizada do SAXHandler para contar os números de carteiras e verificar erros.
     */
    static class CarteiraHandler extends DefaultHandler {
        private List<String> erros = new ArrayList<>();
        private int quantidadeEsperada;
        private int quantidadeEncontrada = 0;

        public CarteiraHandler(int quantidadeEsperada) {
            this.quantidadeEsperada = quantidadeEsperada;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if ("numeroCarteira".equals(qName)) {
                quantidadeEncontrada++;
            }
        }

        @Override
        public void endDocument() throws SAXException {
            if (quantidadeEncontrada < quantidadeEsperada) {
                erros.add("❌ Faltam " + (quantidadeEsperada - quantidadeEncontrada) + " números na carteira.");
            } else if (quantidadeEncontrada > quantidadeEsperada) {
                erros.add("❌ Existem " + (quantidadeEncontrada - quantidadeEsperada) + " números a mais na carteira.");
            }
        }

        public List<String> getErros() {
            return erros;
        }
    }

    /**
     * Verifica os valores no XML com base nas regras de negócios.
     * Exemplo: Se <valorLiberado> for 0.00, verifica se <relacaoGlosa> está presente.
     * @param xmlFilePath O caminho do arquivo XML.
     * @return Lista de erros encontrados.
     */
    public static List<String> verificarValores(String xmlFilePath) {
        List<String> erros = new ArrayList<>();
        try {
            File xmlFile = new File(xmlFilePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            // Verificar valorLiberado e relacaoGlosa
            NodeList valorLiberadoNodes = doc.getElementsByTagName("ans:valorLiberado");
            NodeList relacaoGlosaNodes = doc.getElementsByTagName("ans:relacaoGlosa");

            if (valorLiberadoNodes.getLength() > 0) {
                String valorLiberado = valorLiberadoNodes.item(0).getTextContent();
                if ("0.00".equals(valorLiberado) && relacaoGlosaNodes.getLength() == 0) {
                    erros.add("❌ a tag <relacaoGlosa> deve estar presente.");
                }
            }

        } catch (Exception e) {
            LOGGER.severe("Erro ao verificar valores no XML: " + e.getMessage());
        }
        return erros;
    }

    /**
     * Verifica as tags obrigatórias no XML.
     * @param xmlFilePath O caminho do arquivo XML.
     * @param tipoArquivo Tipo do arquivo ("ENVIO" ou "RETORNO").
     * @return Lista de erros encontrados com exemplo de correção.
     */
    public static List<String> verificarTagsObrigatorias(String xmlFilePath, String tipoArquivo) {
        List<String> erros = new ArrayList<>();
        try {
            File xmlFile = new File(xmlFilePath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            if (tipoArquivo.equals("ENVIO")) {
                if (doc.getElementsByTagName("ans:prestador").getLength() == 0) {
                    erros.add("❌ Tag obrigatória ausente: <ans:prestador>. Exemplo de correção: <ans:prestador> [Valor esperado]</ans:prestador>");
                }
                if (doc.getElementsByTagName("ans:guiaTISS").getLength() == 0) {
                    erros.add("❌ Tag obrigatória ausente: <ans:guiaTISS>. Exemplo de correção: <ans:guiaTISS> [Valor esperado]</ans:guiaTISS>");
                }
            } else if (tipoArquivo.equals("RETORNO")) {
                if (doc.getElementsByTagName("ans:protocolo").getLength() == 0) {
                    erros.add("❌ Tag obrigatória ausente: <ans:protocolo>. Exemplo de correção: <ans:protocolo> [Valor esperado]</ans:protocolo>");
                }
                if (doc.getElementsByTagName("ans:valorGlosaProtocolo").getLength() == 0) {
                    erros.add("❌ Tag obrigatória ausente: <ans:valorGlosaProtocolo>. Exemplo de correção: <ans:valorGlosaProtocolo> [Valor esperado]</ans:valorGlosaProtocolo>");
                }
            }
        } catch (Exception e) {
            LOGGER.severe("Erro ao processar o XML: " + e.getMessage());
        }
        return erros;
    }

    /**
     * Verifica se existem tags não fechadas no XML.
     * @param xmlFilePath O caminho do arquivo XML.
     * @return Lista de erros encontrados com exemplo de correção.
     */
    public static List<String> verificarTagsNaoFechadas(String xmlFilePath) {
        List<String> erros = new ArrayList<>();
        try {
            String content = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(xmlFilePath)));
            String regex = "<(\\w+:?\\w+)(?![^>]*\\/)>";  // Captura aberturas de tags
            java.util.regex.Matcher matcher = java.util.regex.Pattern.compile(regex).matcher(content);

            while (matcher.find()) {
                String tag = matcher.group(1);
                if (!content.contains("</" + tag + ">")) {
                    // Exemplo de correção de tag não fechada
                    erros.add("❌ Tag não fechada corretamente: <" + tag + "> - Exemplo de correção: </" + tag + ">");
                }
            }
        } catch (Exception e) {
            LOGGER.severe("Erro ao verificar tags não fechadas: " + e.getMessage());
        }
        return erros;
    }

    /**
     * Verifica erros de digitação nas tags do XML.
     * @param xmlFilePath O caminho do arquivo XML.
     * @return Lista de erros encontrados com exemplo de correção.
     */
    public static List<String> verificarErrosDigitacao(String xmlFilePath) {
        List<String> erros = new ArrayList<>();
        try {
            String content = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(xmlFilePath)));
            // Supondo que temos tags válidas conhecidas
            List<String> tagsValidas = List.of("ans:prestador", "ans:guiaTISS", "ans:protocolo", "ans:valorGlosaProtocolo");

            java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("<(/?)(\\w+:?\\w+)").matcher(content);

            while (matcher.find()) {
                String tag = matcher.group(2); // Nome da tag encontrada
                if (!tagsValidas.contains(tag)) {
                    erros.add("⚠️ Possível erro de digitação na tag: <" + tag + ">");
                }
            }
        } catch (Exception e) {
            LOGGER.severe("Erro ao verificar erros de digitação: " + e.getMessage());
        }
        return erros;
    }
}
