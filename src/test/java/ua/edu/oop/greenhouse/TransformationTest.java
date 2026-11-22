package ua.edu.oop.greenhouse;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import ua.edu.oop.greenhouse.parser.XmlProcessingException;
import ua.edu.oop.greenhouse.service.XsltGrouper;
import ua.edu.oop.greenhouse.util.XmlSecurity;

import static org.junit.jupiter.api.Assertions.*;

class TransformationTest {
    @TempDir Path tempDir;

    @Test
    void xsltGroupsAllPlantsBySoilWithoutLossOrDuplication() throws Exception {
        Path output = tempDir.resolve("nested/flowers-by-soil.xml");
        new XsltGrouper().groupBySoil(TestFiles.XML, TestFiles.XSL, output);
        Document result = XmlSecurity.documentBuilderFactory().newDocumentBuilder().parse(output.toFile());
        assertEquals("FlowerGroupedBySoil", result.getDocumentElement().getTagName());
        NodeList groups = result.getElementsByTagName("SoilGroup");
        assertEquals(3, groups.getLength());
        Set<String> ids = new HashSet<>();
        Set<String> soils = new HashSet<>();
        for (int i = 0; i < groups.getLength(); i++) {
            Element group = (Element) groups.item(i);
            String soil = group.getAttribute("soil");
            soils.add(soil);
            NodeList plants = group.getElementsByTagName("Plant");
            assertEquals(2, plants.getLength());
            for (int j = 0; j < plants.getLength(); j++) {
                Element plant = (Element) plants.item(j);
                assertEquals(soil, plant.getElementsByTagName("Soil").item(0).getTextContent());
                assertTrue(ids.add(plant.getAttribute("id")), "Duplicate ID in result");
            }
        }
        assertEquals(Set.of("ground", "podzolic", "sod-podzolic"), soils);
        assertEquals(6, ids.size());
    }

    @Test
    void invalidXsltCausesClearException() {
        assertThrows(XmlProcessingException.class, () -> new XsltGrouper().groupBySoil(
                TestFiles.XML, tempDir.resolve("missing.xsl"), tempDir.resolve("out.xml")));
    }
}
