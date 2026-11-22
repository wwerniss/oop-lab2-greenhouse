package ua.edu.oop.greenhouse.model;

import java.util.Objects;

public record Plant(
        String id,
        String name,
        Soil soil,
        String origin,
        VisualParameters visualParameters,
        GrowingTips growingTips,
        Multiplying multiplying) {

    public Plant {
        if (id == null || !id.matches("[A-Za-z_][A-Za-z0-9._-]*")) {
            throw new IllegalArgumentException("id must be a valid simple XML ID");
        }
        name = nonBlank(name, "name");
        origin = nonBlank(origin, "origin");
        Objects.requireNonNull(soil, "soil");
        Objects.requireNonNull(visualParameters, "visualParameters");
        Objects.requireNonNull(growingTips, "growingTips");
        Objects.requireNonNull(multiplying, "multiplying");
    }

    private static String nonBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }
}
