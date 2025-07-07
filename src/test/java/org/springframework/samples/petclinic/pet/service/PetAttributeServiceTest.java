package org.springframework.samples.petclinic.pet.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.pet.model.PetAttribute;
import org.springframework.samples.petclinic.pet.repository.PetAttributeRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetAttributeServiceTest {

	@Mock
	private PetAttributeRepository repository;

	@InjectMocks
	private PetAttributeService service;

	@Test
	void testSave() {
		PetAttribute attr = new PetAttribute();
		attr.setTemperament("Calm");
		attr.setWeight(10.0);
		attr.setLength(20.0);

		when(repository.save(attr)).thenReturn(attr);

		PetAttribute saved = service.save(attr);
		assertEquals("Calm", saved.getTemperament());
		verify(repository, times(1)).save(attr);
	}

	@Test
	void testFindByPetTypeId() {
		PetAttribute attr = new PetAttribute();
		attr.setTemperament("Playful");
		when(repository.findByPetTypeId(1)).thenReturn(Arrays.asList(attr));

		List<PetAttribute> result = service.findByPetTypeId(1);
		assertEquals(1, result.size());
		verify(repository, times(1)).findByPetTypeId(1);
	}

}
