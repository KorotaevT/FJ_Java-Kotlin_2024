package org.example.config.mapper;

import org.example.dto.response.EventEntityResponse;
import org.example.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "placeId", source = "place.id")
    EventEntityResponse toResponse(Event event);

}