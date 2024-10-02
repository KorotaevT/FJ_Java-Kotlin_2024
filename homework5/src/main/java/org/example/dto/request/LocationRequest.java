package org.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LocationRequest {

    @NotBlank(message = "Slug cannot be empty")
    private String slug;

    @NotBlank(message = "Name cannot be empty")
    private String name;
}