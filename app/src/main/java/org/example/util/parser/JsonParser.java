package org.example.util.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public class JsonParser {

    private final ObjectMapper mapper;

    public JsonParser() {
        this.mapper = new ObjectMapper();
    }

    public <T> T parse(String path, Class<T> clazz) {
        try {
            log.info("Parsing file: {}", path);
            T object = mapper.readValue(new File(path), clazz);
            log.info("Successfully parsed object: {}", object);
            return object;
        } catch (IOException e) {
            log.error("Error while parsing file {}: {}", path, e.getMessage(), e);
            return null;
        }
    }

}