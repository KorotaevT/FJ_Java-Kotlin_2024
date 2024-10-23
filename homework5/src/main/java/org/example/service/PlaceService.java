package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.config.mapper.PlaceMapper;
import org.example.dto.response.PlaceResponse;
import org.example.dto.request.PlaceDetailsRequest;
import org.example.model.Place;
import org.example.repository.PlaceRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

    private final PlaceMapper placeMapper;

    public List<PlaceResponse> getAllPlaces() {
        return placeRepository
                .findAll()
                .stream()
                .map(placeMapper::toResponse)
                .collect(Collectors.toList());
    }


    public PlaceResponse getPlaceById(Long id) {
        return placeRepository
                .findByIdWithEvents(id)
                .map(placeMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Place not found"));
    }

    public PlaceResponse createPlace(PlaceDetailsRequest place) {
        var placeEntity = new Place();
        placeEntity.setName(place.getName());
        placeRepository.save(placeEntity);

        return placeMapper.toResponse(placeEntity);
    }

    public PlaceResponse updatePlace(Long id, PlaceDetailsRequest placeDetails) {
        var place = placeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Place not found")
        );

        place.setName(placeDetails.getName());
        placeRepository.save(place);

        return placeMapper.toResponse(place);
    }

    public void deletePlace(Long id) {
        if (!placeRepository.existsById(id)) {
            throw new EntityNotFoundException("Place not found");
        }

        placeRepository.deleteById(id);
    }

}