package org.example.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventResponse {
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private double price;
}
