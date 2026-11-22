package ua.edu.oop.greenhouse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

final class TestFiles {
    static final Path XML = Path.of("src/main/resources/flowers.xml");
    static final Path XSD = Path.of("src/main/resources/flowers.xsd");
    static final Path XSL = Path.of("src/main/resources/group-by-soil.xsl");

    private TestFiles() {
    }

    static Path modifiedXml(Path tempDir, String before, String after) throws IOException {
        String text = Files.readString(XML);
        if (!text.contains(before)) {
            throw new IllegalArgumentException("Missing sample fragment: " + before);
        }
        Path file = tempDir.resolve("modified.xml");
        Files.writeString(file, text.replace(before, after));
        return file;
    }
}
