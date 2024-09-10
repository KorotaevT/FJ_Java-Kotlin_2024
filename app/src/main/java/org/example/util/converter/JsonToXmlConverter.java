package org.example.util.converter;

import lombok.extern.slf4j.Slf4j;
import org.example.util.parser.JsonParser;
import org.example.util.writer.CustomFileWriter;

@Slf4j
public class JsonToXmlConverter {

    private final JsonParser jsonParser;
    private final XmlConverter xmlConverter;
    private final CustomFileWriter customFileWriter;

    public JsonToXmlConverter(JsonParser jsonParser, XmlConverter xmlConverter, CustomFileWriter customFileWriter) {
        this.jsonParser = jsonParser;
        this.xmlConverter = xmlConverter;
        this.customFileWriter = customFileWriter;
    }

    public <T> void convertFromJsonToXml(String jsonFilePath, Class<T> clazz) {
        T object = jsonParser.parse(jsonFilePath, clazz);

        if (object == null) {
            log.error("Conversion failed: Parsed object is null for file: {}", jsonFilePath);
            return;
        }

        String xml = xmlConverter.toXML(object);

        if (xml == null) {
            log.error("Conversion failed: XML conversion returned null for object: {}", object);
            return;
        }

        String xmlFilePath = jsonFilePath.replace(".json", ".xml");
        customFileWriter.writeToFile(xml, xmlFilePath);
    }

}