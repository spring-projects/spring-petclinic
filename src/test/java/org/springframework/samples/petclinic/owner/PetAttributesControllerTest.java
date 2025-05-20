package org.springframework.samples.petclinic.owner;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.owner.controller.PetAttributesController;
import org.springframework.samples.petclinic.owner.dto.PetAttributesDTO;
import org.springframework.samples.petclinic.owner.expection.PetNotFoundException;
import org.springframework.samples.petclinic.owner.model.PetAttributes;
import org.springframework.samples.petclinic.owner.repository.PetRepository;
import org.springframework.samples.petclinic.owner.service.PetAttributesService;
import org.springframework.samples.petclinic.owner.validation.PetIdExistsValidator;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PetAttributesController.class)
public class PetAttributesControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PetAttributesService petAttributesService;

	@MockBean
	private PetIdExistsValidator petIdExistsValidator;

	@MockBean
	private PetRepository petRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testGetPetAttributes_Found() throws Exception {
		int petId = 1;
		PetAttributes attributes = new PetAttributes();

		Mockito.when(petRepository.existsById(petId)).thenReturn(true);
		Mockito.when(petAttributesService.findByPetId(petId)).thenReturn(Optional.of(attributes));

		mockMvc.perform(get("/pets/{petId}/attributes", petId)).andExpect(status().isOk());
	}

	@Test
	void testGetPetAttributes_PetNotFound() throws Exception {
		int petId = 1;

		Mockito.when(petRepository.existsById(petId)).thenReturn(false);

		mockMvc.perform(get("/pets/{petId}/attributes", petId))
			.andExpect(status().isNotFound())
			.andExpect(content().string("Pet not found"));
	}

	@Test
	void testGetPetAttributes_AttributesNotFound() throws Exception {
		int petId = 1;

		Mockito.when(petRepository.existsById(petId)).thenReturn(true);
		Mockito.when(petAttributesService.findByPetId(petId)).thenReturn(Optional.empty());

		mockMvc.perform(get("/pets/{petId}/attributes", petId))
			.andExpect(status().isNotFound())
			.andExpect(content().string("Attributes not found"));
	}

	@Test
	void testSavePetAttributes_Success() throws Exception {
		int petId = 1;
		PetAttributesDTO dto = new PetAttributesDTO();
		dto.setTemperament("Calm");
		dto.setWeightKg(BigDecimal.valueOf(12.5));
		dto.setLengthCm(BigDecimal.valueOf(60.0));

		Mockito.doNothing().when(petIdExistsValidator).validate(eq(petId), any(BindingResult.class));
		Mockito.doNothing().when(petAttributesService).savePetAttributes(any(PetAttributesDTO.class));

		mockMvc
			.perform(post("/pets/{petId}/attributes", petId).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isCreated())
			.andExpect(content().string("Pet attributes saved"));
	}

	@Test
	void testSavePetAttributes_PetNotFoundException() throws Exception {
		int petId = 1;
		PetAttributesDTO dto = new PetAttributesDTO();
		dto.setTemperament("Aggressive");
		dto.setWeightKg(BigDecimal.valueOf(12.5));
		dto.setLengthCm(BigDecimal.valueOf(60.0));

		Mockito.doNothing().when(petIdExistsValidator).validate(eq(petId), any(BindingResult.class));
		Mockito.doThrow(new PetNotFoundException("Pet not found"))
			.when(petAttributesService)
			.savePetAttributes(any(PetAttributesDTO.class));

		mockMvc
			.perform(post("/pets/{petId}/attributes", petId).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isNotFound())
			.andExpect(content().string("Pet not found"));
	}

	@Test
	void testSavePetAttributes_ValidationErrors() throws Exception {
		int petId = 1;
		PetAttributesDTO dto = new PetAttributesDTO();
		dto.setTemperament("Friendly");
		dto.setWeightKg(BigDecimal.valueOf(10.0));
		dto.setLengthCm(BigDecimal.valueOf(50.0));

		// Simulate petId validation failure
		Mockito.doAnswer(invocation -> {
			BindingResult result = invocation.getArgument(1);
			result.reject("petId", "Validation failed");
			return null;
		}).when(petIdExistsValidator).validate(eq(petId), any(BindingResult.class));

		mockMvc
			.perform(post("/pets/{petId}/attributes", petId).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$[0].code").value("petId"));
	}

}
