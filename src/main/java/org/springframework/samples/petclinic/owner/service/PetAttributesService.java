package org.springframework.samples.petclinic.owner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.dto.PetAttributesDTO;
import org.springframework.samples.petclinic.owner.expection.PetNotFoundException;
import org.springframework.samples.petclinic.owner.model.Pet;
import org.springframework.samples.petclinic.owner.model.PetAttributes;
import org.springframework.samples.petclinic.owner.repository.PetAttributesRepository;
import org.springframework.samples.petclinic.owner.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PetAttributesService {

	private final PetAttributesRepository petAttributesRepository;

	private final PetRepository petRepository;

	@Autowired
	public PetAttributesService(PetAttributesRepository petAttributesRepository, PetRepository petRepository) {
		this.petAttributesRepository = petAttributesRepository;
		this.petRepository = petRepository;
	}

	public Optional<PetAttributes> findByPetId(Integer petId) {
		return petAttributesRepository.findByPetId(petId);
	}

	public void savePetAttributes(PetAttributesDTO dto) {
		Optional<Pet> pet = petRepository.findById(dto.getPetId());

		if (pet.isEmpty()) {
			throw new PetNotFoundException("Pet not found");
		}

		PetAttributes attributes = petAttributesRepository.findByPetId(dto.getPetId()).orElse(new PetAttributes());

		attributes.setPet(pet.get());
		attributes.setTemperament(dto.getTemperament());
		attributes.setLengthCm(dto.getLengthCm());
		attributes.setWeightKg(dto.getWeightKg());
		petAttributesRepository.save(attributes);
	}

}
