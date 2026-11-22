package ua.edu.oop.greenhouse.model;

import java.util.Comparator;

public final class PlantComparators {
    public static final Comparator<Plant> BY_NAME = Comparator
            .comparing(Plant::name, String.CASE_INSENSITIVE_ORDER)
            .thenComparing(Plant::id);

    public static final Comparator<Plant> BY_AVERAGE_SIZE = Comparator
            .comparing((Plant plant) -> plant.visualParameters().averageSizeCm())
            .thenComparing(Plant::id);

    private PlantComparators() {
    }
}
