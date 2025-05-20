package org.springframework.samples.petclinic.owner;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NewPetController.class)
class NewPetControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private NewPetRepository petRepository;

	private NewPet testPet;

	private PetType testPetType;

	@BeforeEach
	void setup() {

		testPetType = new PetType();
		testPetType.setId(1);
		testPetType.setName("Test");

		testPet = new NewPet();
		testPet.setId(1);
		testPet.setName("Buddy");
		testPet.setType(testPetType);
	}

	@Test
	void getPetById_found() throws Exception {
		when(petRepository.findById(1)).thenReturn(testPet);

		mockMvc.perform(get("/owners/1/pet/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(1)))
			.andExpect(jsonPath("$.name", is("Buddy")))
			.andExpect(jsonPath("$.type.name", is("Test")))
			.andExpect(jsonPath("$._links.self.href").exists())
			.andExpect(jsonPath("$._links.create-new-pet.href").exists());
	}

	@Test
	void getPetById_notFound() throws Exception {
		when(petRepository.findById(99)).thenReturn(null);

		mockMvc.perform(get("/owners/1/pet/99")).andExpect(status().isNotFound());
	}

	@Test
	void createNewPet_success() throws Exception {

		testPetType = new PetType();
		testPetType.setId(1);
		testPetType.setName("Test1");

		NewPet newPet = new NewPet();
		newPet.setId(2);
		newPet.setName("Whiskers");
		newPet.setType(testPetType);

		when(petRepository.save(any(NewPet.class))).thenReturn(newPet);

		mockMvc.perform(post("/owners/1/pet").contentType(MediaType.APPLICATION_JSON).content(asJsonString(newPet)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id", is(2)))
			.andExpect(jsonPath("$.name", is("Whiskers")))
			.andExpect(jsonPath("$.type.name", is("Test1")))
			.andExpect(jsonPath("$._links.self.href").exists());
	}

	// Utility method to convert objects to JSON string
	private static String asJsonString(Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to convert object to JSON string", e);
		}
	}

}
