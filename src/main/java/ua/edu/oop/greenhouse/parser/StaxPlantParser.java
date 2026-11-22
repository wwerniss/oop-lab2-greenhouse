package ua.edu.oop.greenhouse.parser;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;
import ua.edu.oop.greenhouse.model.Plant;
import ua.edu.oop.greenhouse.util.XmlSecurity;

public final class StaxPlantParser implements PlantParser {
    @Override
    public List<Plant> parse(Path xmlPath) throws XmlProcessingException {
        try (InputStream input = Files.newInputStream(xmlPath)) {
            XMLStreamReader reader = XmlSecurity.xmlInputFactory().createXMLStreamReader(input);
            try {
                return readPlants(reader);
            } finally {
                reader.close();
            }
        } catch (Exception e) {
            throw new XmlProcessingException("StAX parsing failed: " + xmlPath, e);
        }
    }

    private List<Plant> readPlants(XMLStreamReader reader) throws Exception {
        PlantHandler handler = new PlantHandler();
        boolean rootSeen = false;
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.DTD) {
                throw new XMLStreamException("DTD declarations are forbidden");
            }
            if (event == XMLStreamConstants.START_ELEMENT) {
                String tag = reader.getLocalName();
                if (!rootSeen) {
                    if (!"Flower".equals(tag)) {
                        throw new IllegalArgumentException("Expected Flower root element");
                    }
                    rootSeen = true;
                }
                if ("Plant".equals(tag)) {
                    handler.startPlant(reader.getAttributeValue(null, "id"));
                } else if (handler.hasActivePlant() && PlantHandler.isField(tag)) {
                    handler.acceptField(tag, reader.getElementText());
                }
            } else if (event == XMLStreamConstants.END_ELEMENT && "Plant".equals(reader.getLocalName())) {
                handler.endPlant();
            }
        }
        return handler.getPlants();
    }
}
