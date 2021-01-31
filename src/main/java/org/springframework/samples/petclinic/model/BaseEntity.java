package org.springframework.samples.petclinic.model;

public interface BaseEntity {
    Integer getId();

    default boolean isNew() {
        return getId() == null;
    }
}
