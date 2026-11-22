package ua.edu.oop.greenhouse.parser;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import ua.edu.oop.greenhouse.model.Soil;
import static org.junit.jupiter.api.Assertions.*;

class PlantHandlerTest {
    @Test
    void commonHandlerBuildsTwoSeparatePlantsWithoutLeakingValues() {
        PlantHandler handler = new PlantHandler();
        addPlant(handler, "plant_1", "Aloe", "ground", "true");
        addPlant(handler, "plant_2", "Rose", "podzolic", "false");
        assertEquals(2, handler.getPlants().size());
        assertEquals("Aloe", handler.getPlants().get(0).name());
        assertEquals("Rose", handler.getPlants().get(1).name());
        assertEquals(Soil.PODZOLIC, handler.getPlants().get(1).soil());
        assertFalse(handler.getPlants().get(1).growingTips().lightLoving());
        assertEquals(new BigDecimal("20.0"), handler.getPlants().get(1).visualParameters().averageSizeCm());
    }

    @Test
    void handlerRejectsInvalidLifecycleAndUnknownFields() {
        PlantHandler handler = new PlantHandler();
        assertThrows(IllegalStateException.class, handler::endPlant);
        assertThrows(IllegalStateException.class, () -> handler.acceptField("Name", "Rose"));
        handler.startPlant("p_1");
        assertThrows(IllegalStateException.class, () -> handler.startPlant("p_2"));
        assertThrows(IllegalArgumentException.class, () -> handler.acceptField("Unknown", "x"));
        assertThrows(IllegalStateException.class, handler::getPlants);
    }

    private static void addPlant(PlantHandler handler, String id, String name, String soil, String lightLoving) {
        handler.startPlant(id);
        handler.acceptField("Name", name);
        handler.acceptField("Soil", soil);
        handler.acceptField("Origin", "Ukraine");
        handler.acceptField("StemColor", "green");
        handler.acceptField("LeafColor", "green");
        handler.acceptField("AverageSizeCm", "20.0");
        handler.acceptField("TemperatureCelsius", "22");
        handler.acceptField("LightLoving", lightLoving);
        handler.acceptField("WateringMlPerWeek", "100");
        handler.acceptField("Multiplying", "seeds");
        handler.endPlant();
    }
}
