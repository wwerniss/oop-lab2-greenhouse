package ua.edu.oop.greenhouse.service;

import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;
import ua.edu.oop.greenhouse.model.Plant;
import ua.edu.oop.greenhouse.parser.DomPlantParser;
import ua.edu.oop.greenhouse.parser.PlantParser;
import ua.edu.oop.greenhouse.parser.SaxPlantParser;
import ua.edu.oop.greenhouse.parser.StaxPlantParser;
import ua.edu.oop.greenhouse.parser.XmlProcessingException;

public final class GreenhouseService {
    private static final Logger LOGGER = Logger.getLogger(GreenhouseService.class.getName());
    private final XmlValidator validator;
    private final List<PlantParser> parsers;

    public GreenhouseService(Path xsdPath) throws XmlProcessingException {
        validator = new XmlValidator(xsdPath);
        parsers = List.of(new DomPlantParser(), new SaxPlantParser(), new StaxPlantParser());
    }

    public List<Plant> loadPlants(Path xmlPath) throws XmlProcessingException {
        validator.validate(xmlPath);
        LOGGER.info("XML passed XSD validation");

        List<Plant> reference = null;
        for (PlantParser parser : parsers) {
            List<Plant> current = parser.parse(xmlPath);
            LOGGER.info(() -> parser.getClass().getSimpleName() + ": parsed " + current.size() + " plants");
            if (reference != null && !reference.equals(current)) {
                throw new XmlProcessingException("Parsers returned different data", null);
            }
            reference = current;
        }
        return List.copyOf(reference);
    }
}
