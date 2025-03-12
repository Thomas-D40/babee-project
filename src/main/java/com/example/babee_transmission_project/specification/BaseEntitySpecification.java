package com.example.babee_transmission_project.specification;

import com.example.babee_transmission_project.entity.BaseEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.UUID;

public abstract class BaseEntitySpecification<T extends BaseEntity> {

    public Specification<T> sameBabeeId(UUID babeeId) {
        return (root, query, builder) ->
                builder.equal(root.get("babeeId"), babeeId);
    }

    public Specification<T> sameDay(LocalDate dateTime) {
        return (root, query, builder) -> builder.equal(root.get("eventDate"), dateTime);

    }

}
