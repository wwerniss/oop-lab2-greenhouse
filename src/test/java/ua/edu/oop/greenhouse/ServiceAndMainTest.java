package ua.edu.oop.greenhouse;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ua.edu.oop.greenhouse.model.Plant;
import ua.edu.oop.greenhouse.parser.XmlProcessingException;
import ua.edu.oop.greenhouse.service.GreenhouseService;

import static org.junit.jupiter.api.Assertions.*;

class ServiceAndMainTest {
    @TempDir Path tempDir;

    @Test
    void serviceValidatesAndReturnsUnmodifiableCollection() throws Exception {
        List<Plant> plants = new GreenhouseService(TestFiles.XSD).loadPlants(TestFiles.XML);
        assertEquals(6, plants.size());
        assertThrows(UnsupportedOperationException.class, () -> plants.add(plants.get(0)));
        Path invalid = TestFiles.modifiedXml(tempDir, "<Soil>ground</Soil>", "<Soil>sand</Soil>");
        assertThrows(XmlProcessingException.class,
                () -> new GreenhouseService(TestFiles.XSD).loadPlants(invalid));
    }

    @Test
    void mainCreatesXmlOutputAndReportsSuccess() {
        Path output = tempDir.resolve("generated.xml");
        assertEquals(0, Main.run(new String[]{
                TestFiles.XML.toString(), TestFiles.XSD.toString(),
                TestFiles.XSL.toString(), output.toString()}));
        assertTrue(Files.exists(output));
    }

    @Test
    void defaultMainEntryPointAlsoWorks() {
        assertEquals(0, Main.run(new String[0]));
        Main.main(new String[0]);
        assertTrue(Files.exists(Path.of("target/flowers-grouped.xml")));
    }

    @Test
    void mainRejectsInvalidArgumentsAndMissingInput() {
        assertEquals(2, Main.run(new String[]{"bad"}));
        assertEquals(1, Main.run(new String[]{
                tempDir.resolve("missing.xml").toString(), TestFiles.XSD.toString(),
                TestFiles.XSL.toString(), tempDir.resolve("out.xml").toString()}));
    }
}
