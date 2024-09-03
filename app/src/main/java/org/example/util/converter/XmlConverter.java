package org.example.util.converter;

import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

@Slf4j
public class XmlConverter {

    public static <T> String toXML(T object) {
        try {
            log.info("Starting conversion of object to XML: {}", object);
            JAXBContext context = JAXBContext.newInstance(object.getClass());

            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            StringWriter writer = new StringWriter();
            marshaller.marshal(object, writer);

            String xml = writer.toString();
            log.info("Successfully converted object to XML: {}", xml);

            return xml;
        } catch (JAXBException e) {
            log.error("Error converting object to XML: {}", object, e);
            return null;
        }
    }

}