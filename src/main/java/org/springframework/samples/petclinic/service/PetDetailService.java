package org.springframework.samples.petclinic.service;

import org.springframework.samples.petclinic.dto.PetDetailDto;
import org.springframework.samples.petclinic.model.PetDetail;
import org.springframework.stereotype.Service;

@Service
public interface PetDetailService {
	PetDetail savePetDetail(PetDetail petDetail);
	PetDetail getPetDetailByPetId(Integer  petId);
	PetDetail updatePetDetail(Integer petId, PetDetail petDetail);
	void deletePetDetail(Integer petId);
}
