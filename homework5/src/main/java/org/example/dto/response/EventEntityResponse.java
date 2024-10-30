package org.example.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class EventEntityResponse {

    private Long id;

    private String name;

    private LocalDate date;

    private Long placeId;

}