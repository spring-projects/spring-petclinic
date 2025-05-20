package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.samples.petclinic.owner.dto.PetAttributesDTO;
import org.springframework.samples.petclinic.owner.expection.PetNotFoundException;
import org.springframework.samples.petclinic.owner.model.Pet;
import org.springframework.samples.petclinic.owner.model.PetAttributes;
import org.springframework.samples.petclinic.owner.repository.PetAttributesRepository;
import org.springframework.samples.petclinic.owner.repository.PetRepository;
import org.springframework.samples.petclinic.owner.service.PetAttributesService;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PetAttributesServiceTest {

	@Mock
	private PetAttributesRepository petAttributesRepository;

	@Mock
	private PetRepository petRepository;

	@InjectMocks
	private PetAttributesService petAttributesService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindByPetId_ReturnsAttributes() {
		PetAttributes attributes = new PetAttributes();
		when(petAttributesRepository.findByPetId(1)).thenReturn(Optional.of(attributes));
		Optional<PetAttributes> result = petAttributesService.findByPetId(1);
		assertTrue(result.isPresent());
		assertEquals(attributes, result.get());
	}

	@Test
	void testFindByPetId_ReturnsEmpty() {
		when(petAttributesRepository.findByPetId(1)).thenReturn(Optional.empty());
		Optional<PetAttributes> result = petAttributesService.findByPetId(1);
		assertFalse(result.isPresent());
	}

	@Test
	void testSavePetAttributes_Success_NewRecord() {
		Pet pet = new Pet();
		pet.setId(1);

		PetAttributesDTO dto = new PetAttributesDTO();
		dto.setPetId(1);
		dto.setTemperament("Calm");
		dto.setLengthCm(BigDecimal.valueOf(40.0));
		dto.setWeightKg(BigDecimal.valueOf(8.5));

		when(petRepository.findById(1)).thenReturn(Optional.of(pet));
		when(petAttributesRepository.findByPetId(1)).thenReturn(Optional.empty());

		petAttributesService.savePetAttributes(dto);

		// Expect a new PetAttributes to be created and saved
		ArgumentCaptor<PetAttributes> captor = ArgumentCaptor.forClass(PetAttributes.class);
		verify(petAttributesRepository).save(captor.capture());

		PetAttributes saved = captor.getValue();
		assertEquals("Calm", saved.getTemperament());
		assertEquals(BigDecimal.valueOf(40.0), saved.getLengthCm());
		assertEquals(BigDecimal.valueOf(8.5), saved.getWeightKg());
		assertEquals(pet, saved.getPet());
	}

	@Test
	void testSavePetAttributes_Success_UpdateExisting() {
		Pet pet = new Pet();
		pet.setId(1);

		PetAttributes existing = new PetAttributes();
		existing.setPet(pet);

		PetAttributesDTO dto = new PetAttributesDTO();
		dto.setPetId(1);
		dto.setTemperament("Playful");
		dto.setLengthCm(BigDecimal.valueOf(45.0));
		dto.setWeightKg(BigDecimal.valueOf(9.0));

		when(petRepository.findById(1)).thenReturn(Optional.of(pet));
		when(petAttributesRepository.findByPetId(1)).thenReturn(Optional.of(existing));

		petAttributesService.savePetAttributes(dto);

		verify(petAttributesRepository).save(existing);
		assertEquals("Playful", existing.getTemperament());
		assertEquals(BigDecimal.valueOf(45.0), existing.getLengthCm());
		assertEquals(BigDecimal.valueOf(9.0), existing.getWeightKg());
	}

	@Test
	void testSavePetAttributes_PetNotFound_ThrowsException() {
		PetAttributesDTO dto = new PetAttributesDTO();
		dto.setPetId(1);

		when(petRepository.findById(1)).thenReturn(Optional.empty());

		PetNotFoundException ex = assertThrows(PetNotFoundException.class, () -> {
			petAttributesService.savePetAttributes(dto);
		});

		assertEquals("Pet not found", ex.getMessage());
		verify(petAttributesRepository, never()).save(any());
	}

}
