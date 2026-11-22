package ua.edu.oop.greenhouse.parser;

import java.nio.file.Path;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ua.edu.oop.greenhouse.model.Plant;
import ua.edu.oop.greenhouse.util.XmlSecurity;

public final class SaxPlantParser implements PlantParser {
    @Override
    public List<Plant> parse(Path xmlPath) throws XmlProcessingException {
        try {
            PlantHandler handler = new PlantHandler();
            XmlSecurity.saxParserFactory().newSAXParser().parse(xmlPath.toFile(), new SaxEventAdapter(handler));
            return handler.getPlants();
        } catch (Exception e) {
            throw new XmlProcessingException("SAX parsing failed: " + xmlPath, e);
        }
    }

    private static final class SaxEventAdapter extends DefaultHandler {
        private final PlantHandler handler;
        private final StringBuilder text = new StringBuilder();
        private String field;
        private boolean rootSeen;

        SaxEventAdapter(PlantHandler handler) {
            this.handler = handler;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
            if (!rootSeen) {
                if (!"Flower".equals(qName)) {
                    throw new SAXException("Expected Flower root element");
                }
                rootSeen = true;
            }
            if ("Plant".equals(qName)) {
                handler.startPlant(attributes.getValue("id"));
            } else if (PlantHandler.isField(qName)) {
                field = qName;
                text.setLength(0);
            }
        }

        @Override
        public void characters(char[] characters, int start, int length) {
            if (field != null) {
                text.append(characters, start, length);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            if (field != null && field.equals(qName)) {
                handler.acceptField(field, text.toString());
                field = null;
            } else if ("Plant".equals(qName)) {
                handler.endPlant();
            }
        }
    }
}
