package org.example.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PlaceResponse {

    private Long id;

    private String name;

}