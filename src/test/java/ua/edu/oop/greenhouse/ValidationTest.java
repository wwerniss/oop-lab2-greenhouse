package ua.edu.oop.greenhouse;

import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ua.edu.oop.greenhouse.parser.XmlProcessingException;
import ua.edu.oop.greenhouse.service.XmlValidator;

import static org.junit.jupiter.api.Assertions.*;

class ValidationTest {
    @TempDir Path tempDir;

    @Test
    void validXmlPassesSchemaValidation() throws Exception {
        assertDoesNotThrow(() -> new XmlValidator(TestFiles.XSD).validate(TestFiles.XML));
    }

    @Test
    void schemaRejectsUnknownSoilEnumValue() throws Exception {
        Path invalid = TestFiles.modifiedXml(tempDir, "<Soil>ground</Soil>", "<Soil>sand</Soil>");
        assertThrows(XmlProcessingException.class, () -> new XmlValidator(TestFiles.XSD).validate(invalid));
    }

    @Test
    void schemaRejectsDuplicateXmlId() throws Exception {
        Path invalid = TestFiles.modifiedXml(tempDir, "id=\"aloe_02\"", "id=\"ficus_01\"");
        assertThrows(XmlProcessingException.class, () -> new XmlValidator(TestFiles.XSD).validate(invalid));
    }

    @Test
    void schemaRejectsOutOfRangeTemperature() throws Exception {
        Path invalid = TestFiles.modifiedXml(tempDir,
                "<TemperatureCelsius>22</TemperatureCelsius>",
                "<TemperatureCelsius>100</TemperatureCelsius>");
        assertThrows(XmlProcessingException.class, () -> new XmlValidator(TestFiles.XSD).validate(invalid));
    }

    @Test
    void schemaRejectsNegativeWatering() throws Exception {
        Path invalid = TestFiles.modifiedXml(tempDir,
                "<WateringMlPerWeek>600</WateringMlPerWeek>",
                "<WateringMlPerWeek>-1</WateringMlPerWeek>");
        assertThrows(XmlProcessingException.class, () -> new XmlValidator(TestFiles.XSD).validate(invalid));
    }

    @Test
    void schemaRejectsMissingRequiredAttribute() throws Exception {
        Path invalid = TestFiles.modifiedXml(tempDir, "<Plant id=\"ficus_01\">", "<Plant>");
        assertThrows(XmlProcessingException.class, () -> new XmlValidator(TestFiles.XSD).validate(invalid));
    }

    @Test
    void schemaRejectsWrongUnitAttribute() throws Exception {
        Path invalid = TestFiles.modifiedXml(tempDir, "unit=\"cm\"", "unit=\"m\"");
        assertThrows(XmlProcessingException.class, () -> new XmlValidator(TestFiles.XSD).validate(invalid));
    }

    @Test
    void schemaRejectsInvalidMultiplyingAndSize() throws Exception {
        Path invalidMethod = TestFiles.modifiedXml(tempDir, "<Multiplying>cuttings</Multiplying>",
                "<Multiplying>rhizomes</Multiplying>");
        assertThrows(XmlProcessingException.class, () -> new XmlValidator(TestFiles.XSD).validate(invalidMethod));
        Path invalidSize = TestFiles.modifiedXml(tempDir, ">120.5</AverageSizeCm>", ">600</AverageSizeCm>");
        assertThrows(XmlProcessingException.class, () -> new XmlValidator(TestFiles.XSD).validate(invalidSize));
    }

    @Test
    void invalidSchemaFileFailsToLoad() {
        assertThrows(XmlProcessingException.class,
                () -> new XmlValidator(tempDir.resolve("missing.xsd")));
    }
}
