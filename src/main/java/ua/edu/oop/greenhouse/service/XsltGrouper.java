package ua.edu.oop.greenhouse.service;

import java.nio.file.Files;
import java.nio.file.Path;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import ua.edu.oop.greenhouse.parser.XmlProcessingException;
import ua.edu.oop.greenhouse.util.XmlSecurity;

public final class XsltGrouper {
    public void groupBySoil(Path xmlPath, Path xsltPath, Path outputPath) throws XmlProcessingException {
        try {
            Path parent = outputPath.toAbsolutePath().getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Transformer transformer = XmlSecurity.transformerFactory()
                    .newTransformer(new StreamSource(xsltPath.toFile()));
            transformer.transform(new StreamSource(xmlPath.toFile()), new StreamResult(outputPath.toFile()));
        } catch (Exception e) {
            throw new XmlProcessingException("XSLT transformation failed", e);
        }
    }
}
