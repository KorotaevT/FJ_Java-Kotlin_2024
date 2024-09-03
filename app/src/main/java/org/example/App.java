package org.example;

import org.example.entity.City;
import org.example.util.converter.JsonToXmlConverter;

public class App {

    public static void main(String[] args) {
        JsonToXmlConverter.convertFromJsonToXml("app/src/main/resources/cities/city-error.json", City.class);
        JsonToXmlConverter.convertFromJsonToXml("app/src/main/resources/cities/city.json", City.class);
    }

}