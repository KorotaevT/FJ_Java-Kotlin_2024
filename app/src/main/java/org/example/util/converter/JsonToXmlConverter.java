package org.example.util.converter;

import lombok.extern.slf4j.Slf4j;
import org.example.util.parser.JsonParser;
import org.example.util.writer.CustomFileWriter;

@Slf4j
public class JsonToXmlConverter {

    public static <T> void convertFromJsonToXml(String jsonFilePath, Class<T> clazz) {
        T object = JsonParser.parse(jsonFilePath, clazz);

        if (object == null) {
            log.error("Conversion failed: Parsed object is null for file: {}", jsonFilePath);
            return;
        }

        String xml = XmlConverter.toXML(object);

        if (xml == null) {
            log.error("Conversion failed: XML conversion returned null for object: {}", object);
            return;
        }

        String xmlFilePath = jsonFilePath.replace(".json", ".xml");
        CustomFileWriter.writeToFile(xml, xmlFilePath);
    }

}