package org.springframework.samples.petclinic.owner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PetAttributesServiceImpl implements PetAttributesService {

	private final PetAttributesRepository petAttributesRepository;

	@Autowired
	public PetAttributesServiceImpl(PetAttributesRepository repository) {
		this.petAttributesRepository = repository;
	}

	@Override
	@Transactional
	public PetAttributes save(PetAttributes attributes) {
		return petAttributesRepository.save(attributes);
	}

	@Override
	@Transactional(readOnly = true)
	public PetAttributes findByPetId(int petId) {
		return petAttributesRepository.findByPetId(petId);
	}

}
