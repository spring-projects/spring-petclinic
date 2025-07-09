package org.springframework.samples.petclinic.service;

import org.springframework.samples.petclinic.model.ExtendedPetType;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PetService {

	private final PetRepository repository;

	public PetService(PetRepository repository) {
		this.repository = repository;
	}

	public Iterable<ExtendedPetType> findAll() {
		return repository.findAll();
	}

	public Optional<ExtendedPetType> findById(Integer id) {
		return repository.findById(id);
	}

	public ExtendedPetType save(ExtendedPetType petType) {
		return repository.save(petType);
	}

	public void deleteById(Integer id) {
		repository.deleteById(id);
	}

}
