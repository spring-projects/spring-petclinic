package org.springframework.samples.petclinic.pet.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.pet.model.PetAttribute;
import org.springframework.samples.petclinic.pet.service.PetAttributeService;
import org.springframework.samples.petclinic.pet.service.PetTypeService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PetAttributeControllerTest {

	@Mock
	private PetTypeService petTypeService;

	@Mock
	private PetAttributeService petAttributeService;

	@Mock
	private PetAttributeModelAssembler assembler;

	@InjectMocks
	private PetAttributeController controller;

	@Test
	void testCreateAttribute_returnsCreated() {
		PetType type = new PetType();
		PetAttribute attr = new PetAttribute();
		attr.setTemperament("Energetic");

		when(petTypeService.findPetTypeById(1)).thenReturn(type);
		when(petAttributeService.save(attr)).thenReturn(attr);
		when(assembler.toModel(attr)).thenReturn(EntityModel.of(attr));

		ResponseEntity<EntityModel<PetAttribute>> response = controller.createAttribute(1, attr);

		assertEquals(201, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("Energetic", Objects.requireNonNull(response.getBody().getContent()).getTemperament());
		verify(petAttributeService).save(attr);
	}

	@Test
	void testCreateAttribute_petTypeNotFound() {
		PetAttribute attr = new PetAttribute();

		when(petTypeService.findPetTypeById(999)).thenReturn(null);

		ResponseEntity<EntityModel<PetAttribute>> response = controller.createAttribute(999, attr);

		assertEquals(404, response.getStatusCode().value());
		assertNull(response.getBody());
	}

	@Test
	void testGetAttributes_returnsList() {
		PetAttribute attr = new PetAttribute();
		attr.setTemperament("Curious");

		List<PetAttribute> attributes = Collections.singletonList(attr);
		EntityModel<PetAttribute> model = EntityModel.of(attr);

		when(petAttributeService.findByPetTypeId(1)).thenReturn(attributes);
		when(assembler.toModel(attr)).thenReturn(model);

		ResponseEntity<CollectionModel<EntityModel<PetAttribute>>> response = controller.getAttributes(1);

		assertEquals(200, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals(1, response.getBody().getContent().size());
	}

}
