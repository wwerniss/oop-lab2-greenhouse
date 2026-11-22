package ua.edu.oop.greenhouse.model;

import java.util.Arrays;

public enum Soil {
    PODZOLIC("podzolic"),
    GROUND("ground"),
    SOD_PODZOLIC("sod-podzolic");

    private final String xmlValue;

    Soil(String xmlValue) {
        this.xmlValue = xmlValue;
    }

    public String xmlValue() {
        return xmlValue;
    }

    public static Soil fromXml(String value) {
        return Arrays.stream(values())
                .filter(soil -> soil.xmlValue.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown soil: " + value));
    }
}
