package org.springframework.samples.petclinic.owner;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for {@link VaccinationController}.
 */
@WebMvcTest(VaccinationController.class)
@DisabledInNativeImage
@DisabledInAotMode
class VaccinationControllerTests {

	private static final int TEST_OWNER_ID = 1;

	private static final int TEST_PET_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private OwnerRepository owners;

	@BeforeEach
	void init() {
		Owner owner = new Owner();
		Pet pet = new Pet();
		owner.addPet(pet);
		pet.setId(TEST_PET_ID);

		given(this.owners.findById(TEST_OWNER_ID)).willReturn(Optional.of(owner));
	}

	@Test
	void initNewVaccinationForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/vaccinations/new", TEST_OWNER_ID, TEST_PET_ID))
			.andExpect(status().isOk())
			.andExpect(view().name("pets/createOrUpdateVaccinationForm"));
	}

	@Test
	void processNewVaccinationFormSuccess() throws Exception {
		mockMvc
			.perform(post("/owners/{ownerId}/pets/{petId}/vaccinations/new", TEST_OWNER_ID, TEST_PET_ID)
				.param("vaccineName", "Rabies")
				.param("vaccinationDate", LocalDate.now().toString())
				.param("nextDueDate", LocalDate.now().plusYears(1).toString())
				.param("notes", "Annual vaccination"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@Test
	void processNewVaccinationFormHasErrorsWhenVaccineNameIsMissing() throws Exception {
		mockMvc
			.perform(post("/owners/{ownerId}/pets/{petId}/vaccinations/new", TEST_OWNER_ID, TEST_PET_ID)
				.param("vaccinationDate", LocalDate.now().toString())
				.param("nextDueDate", LocalDate.now().plusYears(1).toString())
				.param("notes", "Annual vaccination"))
			.andExpect(model().attributeHasErrors("vaccination"))
			.andExpect(status().isOk())
			.andExpect(view().name("pets/createOrUpdateVaccinationForm"));
	}

	@Test
	void processNewVaccinationFormHasErrorsWhenNextDueDateIsBeforeVaccinationDate() throws Exception {
		LocalDate vaccinationDate = LocalDate.now();
		LocalDate nextDueDate = vaccinationDate.minusDays(1);

		mockMvc
			.perform(post("/owners/{ownerId}/pets/{petId}/vaccinations/new", TEST_OWNER_ID, TEST_PET_ID)
				.param("vaccineName", "Rabies")
				.param("vaccinationDate", vaccinationDate.toString())
				.param("nextDueDate", nextDueDate.toString())
				.param("notes", "Invalid due date"))
			.andExpect(model().attributeHasFieldErrors("vaccination", "nextDueDate"))
			.andExpect(model().attributeHasFieldErrorCode("vaccination", "nextDueDate", "typeMismatch.nextDueDate"))
			.andExpect(status().isOk())
			.andExpect(view().name("pets/createOrUpdateVaccinationForm"));
	}

}
