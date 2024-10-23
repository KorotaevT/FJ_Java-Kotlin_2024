package org.example.config.mapper;

import org.example.dto.response.PlaceResponse;
import org.example.model.Place;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlaceMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    PlaceResponse toResponse(Place place);

}