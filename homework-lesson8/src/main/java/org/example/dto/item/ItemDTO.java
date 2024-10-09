package org.example.dto.item;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDTO {

    @JacksonXmlProperty(localName = "Name")
    private String name;

    @JacksonXmlProperty(localName = "EngName")
    private String engName;

    @JacksonXmlProperty(localName = "Nominal")
    private int nominal;

    @JacksonXmlProperty(localName = "ParentCode")
    private String parentCode;

    @JacksonXmlProperty(localName = "ISO_Num_Code")
    private String isoNumCode;

    @JacksonXmlProperty(localName = "ISO_Char_Code")
    private String isoCharCode;

}