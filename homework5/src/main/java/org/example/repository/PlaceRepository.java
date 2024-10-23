package org.example.repository;

import org.example.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query("""
            select p 
                from Place p 
                left join fetch p.events 
            where p.id = :id
           """
    )
    Optional<Place> findByIdWithEvents(@Param("id") Long id);

}