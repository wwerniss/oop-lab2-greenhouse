package ua.edu.oop.greenhouse.parser;

import java.nio.file.Path;
import java.util.List;
import ua.edu.oop.greenhouse.model.Plant;

public interface PlantParser {
    List<Plant> parse(Path xmlPath) throws XmlProcessingException;
}
