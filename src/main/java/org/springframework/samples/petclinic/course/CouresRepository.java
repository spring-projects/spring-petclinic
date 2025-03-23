package org.springframework.samples.petclinic.course;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CouresRepository extends JpaRepository<Course, Integer> {
}
