package ua.edu.oop.greenhouse.service;

import java.nio.file.Path;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import ua.edu.oop.greenhouse.parser.XmlProcessingException;
import ua.edu.oop.greenhouse.util.XmlSecurity;

public final class XmlValidator {
    private final Schema schema;

    public XmlValidator(Path xsdPath) throws XmlProcessingException {
        try {
            schema = XmlSecurity.schemaFactory().newSchema(xsdPath.toFile());
        } catch (Exception e) {
            throw new XmlProcessingException("Cannot load XSD: " + xsdPath, e);
        }
    }

    public void validate(Path xmlPath) throws XmlProcessingException {
        try {
            Validator validator = schema.newValidator();
            validator.setProperty(javax.xml.XMLConstants.ACCESS_EXTERNAL_DTD, "");
            validator.setProperty(javax.xml.XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            validator.validate(new StreamSource(xmlPath.toFile()));
        } catch (Exception e) {
            throw new XmlProcessingException("XML is not valid against XSD: " + xmlPath, e);
        }
    }
}
