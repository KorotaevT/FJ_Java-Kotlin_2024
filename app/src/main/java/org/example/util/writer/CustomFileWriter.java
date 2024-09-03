package org.example.util.writer;

import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;

@Slf4j
public class CustomFileWriter {

    public static void writeToFile(String text, String path) {
        try (FileWriter writer = new FileWriter(path)) {
            log.info("Writing text to file: {}", path);
            writer.write(text);
            log.info("Successfully wrote text to file: {}", path);
        } catch (IOException e) {
            log.error("Error writing text to file {}: {}", path, e.getMessage(), e);
        }
    }

}