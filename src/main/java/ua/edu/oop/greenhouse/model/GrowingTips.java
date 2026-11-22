package ua.edu.oop.greenhouse.model;

public record GrowingTips(int temperatureCelsius, boolean lightLoving, int wateringMlPerWeek) {
    public GrowingTips {
        if (temperatureCelsius < -30 || temperatureCelsius > 60) {
            throw new IllegalArgumentException("temperatureCelsius must be between -30 and 60");
        }
        if (wateringMlPerWeek < 0 || wateringMlPerWeek > 10_000) {
            throw new IllegalArgumentException("wateringMlPerWeek must be between 0 and 10000");
        }
    }
}
