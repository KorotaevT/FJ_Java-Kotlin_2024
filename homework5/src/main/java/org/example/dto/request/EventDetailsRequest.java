package org.example.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class EventDetailsRequest {

    @NotNull
    private String name;

    @NotNull
    private LocalDate date;

    @NotNull
    private Long placeId;

}