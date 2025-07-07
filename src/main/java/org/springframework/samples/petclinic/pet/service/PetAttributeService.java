package org.springframework.samples.petclinic.pet.service;

import org.springframework.samples.petclinic.pet.model.PetAttribute;
import org.springframework.samples.petclinic.pet.repository.PetAttributeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Rohit Lalwani
 */
@Service
public class PetAttributeService {

	private final PetAttributeRepository petAttributeRepository;

	public PetAttributeService(PetAttributeRepository repo) {
		this.petAttributeRepository = repo;
	}

	public PetAttribute save(PetAttribute attr) {
		return petAttributeRepository.save(attr);
	}

	public List<PetAttribute> findByPetTypeId(Integer petTypeId) {
		return petAttributeRepository.findByPetTypeId(petTypeId);
	}

}
