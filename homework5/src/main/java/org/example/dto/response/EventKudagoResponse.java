package org.example.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EventKudagoResponse {
    private long count;
    private String next;
    private String previous;
    private List<EventKudagoResult> results;
}