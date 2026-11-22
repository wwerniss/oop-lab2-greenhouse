package ua.edu.oop.greenhouse;

import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ua.edu.oop.greenhouse.model.Plant;
import ua.edu.oop.greenhouse.model.PlantComparators;
import ua.edu.oop.greenhouse.service.GreenhouseService;
import ua.edu.oop.greenhouse.service.XsltGrouper;
import ua.edu.oop.greenhouse.util.LogConfig;

public final class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private Main() {
    }

    public static void main(String[] args) {
        int status = run(args);
        if (status != 0) {
            System.exit(status);
        }
    }

    /** 0 = success, 1 = processing error, 2 = incorrect command arguments. */
    public static int run(String[] args) {
        if (args.length != 0 && args.length != 4) {
            System.err.println("Usage: java -jar greenhouse-xml-lab2-1.0.0.jar [xml xsd xsl output]");
            return 2;
        }

        Path xml = Path.of(args.length == 0 ? "src/main/resources/flowers.xml" : args[0]);
        Path xsd = Path.of(args.length == 0 ? "src/main/resources/flowers.xsd" : args[1]);
        Path xsl = Path.of(args.length == 0 ? "src/main/resources/group-by-soil.xsl" : args[2]);
        Path result = Path.of(args.length == 0 ? "target/flowers-grouped.xml" : args[3]);
        try {
            LogConfig.configure();
            List<Plant> plants = new GreenhouseService(xsd).loadPlants(xml);
            LOGGER.info("Sorted plants by name:");
            plants.stream().sorted(PlantComparators.BY_NAME)
                    .forEach(plant -> LOGGER.info(() -> plant.name() + " (" + plant.soil().xmlValue() + ")"));
            LOGGER.info("Sorted plants by average size:");
            plants.stream().sorted(PlantComparators.BY_AVERAGE_SIZE)
                    .forEach(plant -> LOGGER.info(() -> plant.name() + " ("
                            + plant.visualParameters().averageSizeCm() + " cm)"));
            new XsltGrouper().groupBySoil(xml, xsl, result);
            LOGGER.info(() -> "Grouped XML written to: " + result.toAbsolutePath());
            return 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Greenhouse processing failed", e);
            return 1;
        }
    }
}
