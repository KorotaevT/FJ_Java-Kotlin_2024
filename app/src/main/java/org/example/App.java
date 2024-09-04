package org.example;

import org.example.entity.City;
import org.example.util.converter.JsonToXmlConverter;
import org.example.util.converter.XmlConverter;
import org.example.util.parser.JsonParser;
import org.example.util.writer.CustomFileWriter;

public class App {

    public static void main(String[] args) {
        JsonParser jsonParser = new JsonParser();
        XmlConverter xmlConverter = new XmlConverter();
        CustomFileWriter customFileWriter = new CustomFileWriter();
        JsonToXmlConverter converter = new JsonToXmlConverter(jsonParser, xmlConverter, customFileWriter);
        converter.convertFromJsonToXml("app/src/main/resources/cities/city-error.json", City.class);
        converter.convertFromJsonToXml("app/src/main/resources/cities/city.json", City.class);
    }
}