package org.springframework.samples.petclinic.pet.service;

import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.owner.PetTypeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Rohit Lalwani
 */
@Service
public class PetTypeService {

	private final PetTypeRepository petTypeRepository;

	public PetTypeService(PetTypeRepository petTypeRepository) {
		this.petTypeRepository = petTypeRepository;
	}

	public PetType findPetTypeById(Integer id) {
		Optional<PetType> petType = petTypeRepository.findById(id);
		return petType.orElse(null);
	}

}
