package ua.edu.oop.greenhouse.model;

import java.math.BigDecimal;
import java.util.Objects;

public record VisualParameters(String stemColor, String leafColor, BigDecimal averageSizeCm) {
    public VisualParameters {
        stemColor = requireText(stemColor, "stemColor");
        leafColor = requireText(leafColor, "leafColor");
        Objects.requireNonNull(averageSizeCm, "averageSizeCm");
        if (averageSizeCm.compareTo(new BigDecimal("0.1")) < 0
                || averageSizeCm.compareTo(new BigDecimal("500")) > 0) {
            throw new IllegalArgumentException("averageSizeCm must be between 0.1 and 500");
        }
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }
}
