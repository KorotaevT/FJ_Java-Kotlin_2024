package org.example.repository;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.example.model.Event;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAll(Specification<Event> specification);

    static Specification<Event> buildSpecification(String name, String placeName, LocalDate fromDate, LocalDate toDate) {
        return (root, query, criteriaBuilder) -> {
            root.fetch("place", JoinType.LEFT);

            List<Predicate> predicates = new ArrayList<>();

            Optional.ofNullable(name)
                    .ifPresent(n -> predicates.add(criteriaBuilder.equal(root.get("name"), n)));

            Optional.ofNullable(placeName)
                    .ifPresent(pn -> predicates.add(criteriaBuilder.equal(root.get("place").get("name"), pn)));

            Optional.ofNullable(fromDate)
                    .ifPresent(fd -> predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), fd)));

            Optional.ofNullable(toDate)
                    .ifPresent(td -> predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), td)));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}