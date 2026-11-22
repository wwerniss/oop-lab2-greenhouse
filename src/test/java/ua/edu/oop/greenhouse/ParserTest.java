package ua.edu.oop.greenhouse;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ua.edu.oop.greenhouse.model.Multiplying;
import ua.edu.oop.greenhouse.model.Plant;
import ua.edu.oop.greenhouse.model.Soil;
import ua.edu.oop.greenhouse.parser.DomPlantParser;
import ua.edu.oop.greenhouse.parser.PlantParser;
import ua.edu.oop.greenhouse.parser.SaxPlantParser;
import ua.edu.oop.greenhouse.parser.StaxPlantParser;
import ua.edu.oop.greenhouse.parser.XmlProcessingException;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
    @TempDir Path tempDir;
    private final List<PlantParser> parsers = List.of(
            new DomPlantParser(), new SaxPlantParser(), new StaxPlantParser());

    @Test
    void threeParsersProduceExactlyTheSamePlants() throws Exception {
        List<Plant> expected = parsers.get(0).parse(TestFiles.XML);
        assertEquals(6, expected.size());
        assertEquals(expected, parsers.get(1).parse(TestFiles.XML));
        assertEquals(expected, parsers.get(2).parse(TestFiles.XML));
        Plant ficus = expected.get(0);
        assertEquals("ficus_01", ficus.id());
        assertEquals("Ficus", ficus.name());
        assertEquals(Soil.GROUND, ficus.soil());
        assertEquals("Southeast Asia", ficus.origin());
        assertEquals("brown", ficus.visualParameters().stemColor());
        assertEquals("dark green", ficus.visualParameters().leafColor());
        assertEquals(0, ficus.visualParameters().averageSizeCm().compareTo(new java.math.BigDecimal("120.5")));
        assertEquals(22, ficus.growingTips().temperatureCelsius());
        assertFalse(ficus.growingTips().lightLoving());
        assertEquals(600, ficus.growingTips().wateringMlPerWeek());
        assertEquals(Multiplying.CUTTINGS, ficus.multiplying());
    }

    @Test
    void parsersUnderstandBothLegalXmlBooleanSpellings() throws Exception {
        Path changed = TestFiles.modifiedXml(tempDir, "<LightLoving>false</LightLoving>",
                "<LightLoving>1</LightLoving>");
        for (PlantParser parser : parsers) {
            assertTrue(parser.parse(changed).get(0).growingTips().lightLoving());
        }
    }

    @Test
    void parsersRejectMalformedXml() throws Exception {
        Path invalid = tempDir.resolve("malformed.xml");
        Files.writeString(invalid, "<Flower><Plant></Flower>");
        for (PlantParser parser : parsers) {
            assertThrows(XmlProcessingException.class, () -> parser.parse(invalid));
        }
    }

    @Test
    void parsersRejectUnexpectedRoot() throws Exception {
        Path invalid = tempDir.resolve("unexpected-root.xml");
        Files.writeString(invalid, Files.readString(TestFiles.XML)
                .replace("<Flower ", "<Garden ")
                .replace("</Flower>", "</Garden>"));
        for (PlantParser parser : parsers) {
            assertThrows(XmlProcessingException.class, () -> parser.parse(invalid));
        }
    }

    @Test
    void parsersRejectUnknownSoilEvenWithoutSeparateValidation() throws Exception {
        Path invalid = TestFiles.modifiedXml(tempDir, "<Soil>ground</Soil>", "<Soil>sand</Soil>");
        for (PlantParser parser : parsers) {
            assertThrows(XmlProcessingException.class, () -> parser.parse(invalid));
        }
    }

    @Test
    void parsersRejectExternalEntityDoctype() throws Exception {
        Path invalid = TestFiles.modifiedXml(tempDir, "<Flower ",
                "<!DOCTYPE Flower [<!ENTITY ext SYSTEM \"file:///etc/passwd\">]><Flower ");
        for (PlantParser parser : parsers) {
            assertThrows(XmlProcessingException.class, () -> parser.parse(invalid));
        }
    }
}
