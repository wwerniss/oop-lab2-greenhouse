package ua.edu.oop.greenhouse.parser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import ua.edu.oop.greenhouse.model.GrowingTips;
import ua.edu.oop.greenhouse.model.Multiplying;
import ua.edu.oop.greenhouse.model.Plant;
import ua.edu.oop.greenhouse.model.Soil;
import ua.edu.oop.greenhouse.model.VisualParameters;

final class PlantHandler {
    private final List<Plant> plants = new ArrayList<>();
    private String id;
    private String name;
    private Soil soil;
    private String origin;
    private String stemColor;
    private String leafColor;
    private BigDecimal averageSize;
    private Integer temperature;
    private Boolean lightLoving;
    private Integer watering;
    private Multiplying multiplying;
    private boolean active;

    static boolean isField(String elementName) {
        return switch (elementName) {
            case "Name", "Soil", "Origin", "StemColor", "LeafColor", "AverageSizeCm",
                    "TemperatureCelsius", "LightLoving", "WateringMlPerWeek", "Multiplying" -> true;
            default -> false;
        };
    }

    void startPlant(String id) {
        if (active) {
            throw new IllegalStateException("A plant is already being parsed");
        }
        this.id = id;
        name = null;
        soil = null;
        origin = null;
        stemColor = null;
        leafColor = null;
        averageSize = null;
        temperature = null;
        lightLoving = null;
        watering = null;
        multiplying = null;
        active = true;
    }

    boolean hasActivePlant() {
        return active;
    }

    void acceptField(String elementName, String rawValue) {
        if (!active) {
            throw new IllegalStateException("XML field outside Plant: " + elementName);
        }
        String value = rawValue.trim();
        switch (elementName) {
            case "Name" -> name = value;
            case "Soil" -> soil = Soil.fromXml(value);
            case "Origin" -> origin = value;
            case "StemColor" -> stemColor = value;
            case "LeafColor" -> leafColor = value;
            case "AverageSizeCm" -> averageSize = new BigDecimal(value);
            case "TemperatureCelsius" -> temperature = Integer.parseInt(value);
            case "LightLoving" -> lightLoving = parseBoolean(value);
            case "WateringMlPerWeek" -> watering = Integer.parseInt(value);
            case "Multiplying" -> multiplying = Multiplying.fromXml(value);
            default -> throw new IllegalArgumentException("Unexpected XML field: " + elementName);
        }
    }

    void endPlant() {
        if (!active) {
            throw new IllegalStateException("Closing Plant without opening it");
        }
        plants.add(new Plant(id, name, soil, origin,
                new VisualParameters(stemColor, leafColor, averageSize),
                new GrowingTips(temperature, lightLoving, watering), multiplying));
        active = false;
    }

    List<Plant> getPlants() {
        if (active) {
            throw new IllegalStateException("Cannot return results with an unfinished Plant");
        }
        return List.copyOf(plants);
    }

    private static boolean parseBoolean(String value) {
        return switch (value) {
            case "true", "1" -> true;
            case "false", "0" -> false;
            default -> throw new IllegalArgumentException("Invalid XML boolean: " + value);
        };
    }
}
