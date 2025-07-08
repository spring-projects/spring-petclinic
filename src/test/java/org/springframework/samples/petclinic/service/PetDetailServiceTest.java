package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.PetDetail;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.repository.PetDetailRepository;

@SpringBootTest
 class PetDetailServiceTest {

	@Autowired
	private PetDetailService service;

	@Autowired
	private PetDetailRepository petRepo;

	@Test
	void testSaveAndFetchPetDetail() {
		Pet pet = petRepo.findAll().get(0).getPet();

		PetDetail detail = PetDetail.builder()
			.pet(pet)
			.temperament("Aggressive")
			.weight(20.0)
			.length(30.0)
			.build();

		PetDetail saved = service.savePetDetail(detail);
		Assertions.assertNotNull(saved.getId());

		PetDetail fetched = service.getPetDetailByPetId(pet.getId());
		Assertions.assertEquals("Aggressive", fetched.getTemperament());
	}
}

