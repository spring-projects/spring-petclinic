package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.samples.petclinic.dto.PetAttributeDto;
import org.springframework.samples.petclinic.model.PetTypeAttribute;

/*
 * Service interface layer for PetTypeAttribute operations.
 * Purpose: Provide loose coupling and implement to service.
 * @Author: Sujeet Sharma
 * @Date: 08--9-2025
 */


public interface PetAttributeService {

	/* Create a new attribute for a given PetType */
	PetAttributeDto create(Integer typeId, PetAttributeDto attr);

	/* Fetch a single attribute by its ID. */
	PetAttributeDto getById(Integer id);

	/* Fetch all attributes for a given PetType */
	List<PetAttributeDto> getByTypeId(Integer typeId);

	/* Update an existing attribute. */
	PetAttributeDto update(Integer id, PetAttributeDto payload);

	/* delete by id */
	void delete(Integer id);

}
