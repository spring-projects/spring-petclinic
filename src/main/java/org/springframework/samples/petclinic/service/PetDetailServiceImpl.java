package org.springframework.samples.petclinic.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import org.springframework.samples.petclinic.exception.ResourceNotFoundException;
import org.springframework.samples.petclinic.model.PetDetail;
import org.springframework.samples.petclinic.repository.PetDetailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PetDetailServiceImpl implements PetDetailService {

	private final PetDetailRepository petRepo;

	@Autowired
	public PetDetailServiceImpl(PetDetailRepository petRepo) {
		this.petRepo = petRepo;
	}

	@Override
	@Transactional
	public PetDetail savePetDetail(PetDetail petDetail) {
		return petRepo.save(petDetail);
	}

	@Override
	@Cacheable(value = "petDetails", key = "#petId")
	@Transactional
	public PetDetail getPetDetailByPetId(Integer petId) {
		return petRepo.findByPetId(petId)
			.orElseThrow(() -> new ResourceNotFoundException("Pet details not found for the pet id : " +petId));
	}

	@Override
	@CachePut(value = "petDetails", key = "#petId")
	@Transactional
	public PetDetail updatePetDetail(Integer petId, PetDetail updatedDetail) {
		PetDetail existing = petRepo.findByPetId(petId)
			.orElseThrow(() -> new ResourceNotFoundException("Pet detail not found for the pet id: " + petId));

		existing.setTemperament(updatedDetail.getTemperament());
		existing.setWeight(updatedDetail.getWeight());
		existing.setLength(updatedDetail.getLength());
		return petRepo.save(existing);
	}

	@Override
	@CacheEvict(value = "petDetails", key = "#petId")
	@Transactional
	public void deletePetDetail(Integer petId) {
		PetDetail detail = petRepo.findByPetId(petId)
			.orElseThrow(() -> new ResourceNotFoundException("Pet details not found for the for pet id: " + petId));
		petRepo.delete(detail);
	}
}
