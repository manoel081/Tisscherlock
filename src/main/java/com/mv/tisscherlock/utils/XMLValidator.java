package com.mv.tisscherlock.utils;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.InputStream;

public class XMLValidator {

    /**
     * Valida o XML contra um XSD fornecido como InputStream.
     *
     * @param xmlFilePath O caminho do arquivo XML a ser validado.
     * @param xsdInputStream O XSD fornecido como InputStream.
     * @return true se o XML for válido, false caso contrário.
     * @throws Exception Se ocorrer um erro na validação.
     */
    public static boolean validateXML(String xmlFilePath, InputStream xsdInputStream) throws Exception {
        // Cria o Schema a partir do InputStream do XSD
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new StreamSource(xsdInputStream));
        
        // Cria o validador
        Validator validator = schema.newValidator();
        
        // Realiza a validação do XML contra o XSD
        validator.validate(new StreamSource(xmlFilePath));
        
        return true; // Se não houver exceções, o XML é válido
    }
}
