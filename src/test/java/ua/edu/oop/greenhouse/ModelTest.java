package ua.edu.oop.greenhouse;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.Test;
import ua.edu.oop.greenhouse.model.GrowingTips;
import ua.edu.oop.greenhouse.model.Multiplying;
import ua.edu.oop.greenhouse.model.Plant;
import ua.edu.oop.greenhouse.model.PlantComparators;
import ua.edu.oop.greenhouse.model.Soil;
import ua.edu.oop.greenhouse.model.VisualParameters;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest {
    @Test
    void enumsMapEveryPermittedXmlValue() {
        for (Soil soil : Soil.values()) {
            assertEquals(soil, Soil.fromXml(soil.xmlValue()));
        }
        for (Multiplying multiplying : Multiplying.values()) {
            assertEquals(multiplying, Multiplying.fromXml(multiplying.xmlValue()));
        }
        assertThrows(IllegalArgumentException.class, () -> Soil.fromXml("unknown"));
        assertThrows(IllegalArgumentException.class, () -> Multiplying.fromXml("unknown"));
    }

    @Test
    void recordsEnforceConstraintsEvenWithoutSchemaValidation() {
        assertThrows(IllegalArgumentException.class,
                () -> new GrowingTips(-31, true, 100));
        assertThrows(IllegalArgumentException.class,
                () -> new GrowingTips(20, true, 10_001));
        assertThrows(IllegalArgumentException.class,
                () -> new VisualParameters("", "green", BigDecimal.TEN));
        assertThrows(IllegalArgumentException.class,
                () -> new VisualParameters("brown", "green", BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class,
                () -> new VisualParameters("brown", "green", new BigDecimal("501")));
        assertThrows(IllegalArgumentException.class,
                () -> new Plant("invalid id", "Aloe", Soil.GROUND, "Africa",
                        visual("15"), new GrowingTips(20, true, 10), Multiplying.LEAVES));
        assertThrows(IllegalArgumentException.class,
                () -> new Plant("id", " ", Soil.GROUND, "Africa",
                        visual("15"), new GrowingTips(20, true, 10), Multiplying.LEAVES));
        assertThrows(NullPointerException.class,
                () -> new Plant("id", "Aloe", null, "Africa",
                        visual("15"), new GrowingTips(20, true, 10), Multiplying.LEAVES));
    }

    @Test
    void comparatorOrdersByNameAndBySize() {
        Plant z = plant("z", "Zebra", "12");
        Plant b = plant("b", "aloe", "40");
        Plant a = plant("a", "Aloe", "12");
        List<Plant> plants = List.of(z, b, a);
        assertEquals(List.of(a, b, z), plants.stream().sorted(PlantComparators.BY_NAME).toList());
        assertEquals(List.of(a, z, b), plants.stream().sorted(PlantComparators.BY_AVERAGE_SIZE).toList());
        Comparator<Plant> name = PlantComparators.BY_NAME;
        assertTrue(name.compare(a, b) < 0);
    }

    private static VisualParameters visual(String size) {
        return new VisualParameters("green", "green", new BigDecimal(size));
    }

    private static Plant plant(String id, String name, String size) {
        return new Plant(id, name, Soil.GROUND, "Ukraine", visual(size),
                new GrowingTips(20, true, 100), Multiplying.SEEDS);
    }
}
