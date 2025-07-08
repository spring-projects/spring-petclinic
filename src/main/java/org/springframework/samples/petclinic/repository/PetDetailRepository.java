package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.expression.spel.ast.OpAnd;
import org.springframework.samples.petclinic.model.PetDetail;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PetDetailRepository extends JpaRepository<PetDetail, Integer> {
	Optional<PetDetail> findByPetId(int petId);
}

