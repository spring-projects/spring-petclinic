package org.springframework.samples.petclinic.owner;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PetViewRepository extends JpaRepository<Pet, Integer>{

	Page<Pet> findByNameStartingWith(String name, Pageable pageable);
	
	
	@Query("SELECT p from Pet p LEFT JOIN  FETCH p.owner LEFT JOIN FETCH p.petAttribute WHERE p.id= :id")
	Optional<Pet> findPetByWithOwnerAndAttribute(@Param("id") int id);
}
