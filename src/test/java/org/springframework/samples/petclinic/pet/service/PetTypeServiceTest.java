package org.springframework.samples.petclinic.pet.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.owner.PetTypeRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetTypeServiceTest {

	@Mock
	private PetTypeRepository petTypeRepository;

	@InjectMocks
	private PetTypeService petTypeService;

	@Test
	void testFindPetTypeById_found() {
		PetType type = new PetType();
		type.setId(1);
		when(petTypeRepository.findById(1)).thenReturn(Optional.of(type));

		PetType found = petTypeService.findPetTypeById(1);
		assertNotNull(found);
		assertEquals(found.getId(), Optional.of(1).get());
	}

	@Test
	void testFindPetTypeById_notFound() {
		when(petTypeRepository.findById(2)).thenReturn(Optional.empty());
		PetType found = petTypeService.findPetTypeById(2);
		assertNull(found);
	}

}
