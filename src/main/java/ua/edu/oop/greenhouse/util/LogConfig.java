package ua.edu.oop.greenhouse.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.LogManager;

public final class LogConfig {
    private LogConfig() {
    }

    public static void configure() throws IOException {
        Files.createDirectories(Path.of("target"));
        try (InputStream config = LogConfig.class.getResourceAsStream("/logging.properties")) {
            if (config == null) {
                throw new IOException("Missing logging.properties in classpath");
            }
            LogManager.getLogManager().readConfiguration(config);
        }
    }
}
