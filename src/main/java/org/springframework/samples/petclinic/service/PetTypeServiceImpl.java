package org.springframework.samples.petclinic.service;

import org.springframework.samples.petclinic.model.PetTypes;
import org.springframework.samples.petclinic.repository.PetTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetTypeServiceImpl implements PetTypeService {

	private final PetTypeRepository petTypeRepository;

	public PetTypeServiceImpl(PetTypeRepository petTypeRepository) {
		this.petTypeRepository = petTypeRepository;
	}

	@Override
	public PetTypes save(PetTypes petType) {
		return petTypeRepository.save(petType);
	}

	@Override
	public List<PetTypes> findAll() {
		return petTypeRepository.findAll();
	}
}
