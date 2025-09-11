package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.petclinic.model.PetTypeAttribute;
import org.springframework.stereotype.Repository;

/*
 * Repository for PetTypeAttribute entity.
 * Purpose: Provides CRUD and custom finder methods.
 * @Author: Sujeet Sharma
 * @Date: 08--9-2025
 */

@Repository
public interface PetTypeAttributeRepository extends JpaRepository<PetTypeAttribute, Integer> {

	/**
	 * Find all attributes for a given PetType by petType.id.
	 */
	List<PetTypeAttribute> findByPetType_Id(Integer petTypeId);

}