package ua.edu.oop.greenhouse.model;

import java.util.Arrays;

public enum Multiplying {
    LEAVES("leaves"),
    CUTTINGS("cuttings"),
    SEEDS("seeds");

    private final String xmlValue;

    Multiplying(String xmlValue) {
        this.xmlValue = xmlValue;
    }

    public String xmlValue() {
        return xmlValue;
    }

    public static Multiplying fromXml(String value) {
        return Arrays.stream(values())
                .filter(method -> method.xmlValue.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown multiplying: " + value));
    }
}
