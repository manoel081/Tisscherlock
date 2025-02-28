package com.mv.tisscherlock.utils;

import java.io.InputStream;
import java.net.URL;
import java.net.HttpURLConnection;

public class XSDDownloader {

    /**
     * Recupera o arquivo XSD diretamente do site da ANS com base na versão fornecida no XML.
     *
     * @param version A versão do XSD necessária, extraída do XML.
     * @return Um InputStream contendo o XSD para validação.
     * @throws Exception Se ocorrer um erro ao recuperar o XSD.
     */
    public static InputStream downloadXSD(String version) throws Exception {
        String xsdUrl = "https://www.gov.br/ans/arquivos/tiss/tiss-schema-" + version + ".xsd";
        URL url = new URL(xsdUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new Exception("Erro ao obter o XSD: " + responseCode);
        }

        // Retorna o InputStream contendo o XSD
        return connection.getInputStream();
    }
}
