package ua.edu.oop.greenhouse.parser;

import java.nio.file.Path;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ua.edu.oop.greenhouse.model.Plant;
import ua.edu.oop.greenhouse.util.XmlSecurity;

public final class DomPlantParser implements PlantParser {
    @Override
    public List<Plant> parse(Path xmlPath) throws XmlProcessingException {
        try {
            DocumentBuilderFactory factory = XmlSecurity.documentBuilderFactory();
            Document document = factory.newDocumentBuilder().parse(xmlPath.toFile());
            if (!"Flower".equals(document.getDocumentElement().getTagName())) {
                throw new IllegalArgumentException("Expected Flower root element");
            }
            PlantHandler handler = new PlantHandler();
            NodeList nodes = document.getDocumentElement().getElementsByTagName("Plant");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);
                handler.startPlant(element.getAttribute("id"));
                readFields(element, handler);
                handler.endPlant();
            }
            return handler.getPlants();
        } catch (Exception e) {
            throw new XmlProcessingException("DOM parsing failed: " + xmlPath, e);
        }
    }

    private void readFields(Element parent, PlantHandler handler) {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Element child = (Element) node;
            if (PlantHandler.isField(child.getTagName())) {
                handler.acceptField(child.getTagName(), child.getTextContent());
            } else {
                readFields(child, handler);
            }
        }
    }
}
