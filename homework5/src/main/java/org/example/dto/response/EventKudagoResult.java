package org.example.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EventKudagoResult {
    private List<EventKudagoDates> dates;
    private String title;
    private EventKudagoLocation location;
    private String price;
}