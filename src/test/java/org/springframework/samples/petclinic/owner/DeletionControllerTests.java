/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for {@link DeletionController} (MLG-13).
 */
@WebMvcTest(DeletionController.class)
@DisabledInNativeImage
@DisabledInAotMode
class DeletionControllerTests {

	private static final int OWNER_ID = 1;

	private static final int PET_ID = 10;

	private static final int VISIT_ID = 100;

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private DeletionService deletion;

	@Test
	void confirmDeleteOwnerShowsConfirmationPage() throws Exception {
		Owner owner = new Owner();
		owner.setFirstName("George");
		owner.setLastName("Franklin");
		given(deletion.getOwner(OWNER_ID)).willReturn(owner);

		mockMvc.perform(get("/owners/{ownerId}/delete", OWNER_ID))
			.andExpect(status().isOk())
			.andExpect(view().name("confirmDelete"))
			.andExpect(model().attribute("entityName", "Owner"))
			.andExpect(model().attribute("entityId", OWNER_ID))
			.andExpect(model().attribute("postUrl", "/owners/1/delete"))
			.andExpect(model().attribute("cancelUrl", "/owners/1"))
			.andExpect(model().attributeExists("warningMessage"));
	}

	@Test
	void confirmDeleteOwnerNotFoundRedirectsToOwnersList() throws Exception {
		given(deletion.getOwner(99)).willThrow(new ResourceNotFoundException("Owner", 99));

		mockMvc.perform(get("/owners/{ownerId}/delete", 99))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners"))
			.andExpect(flash().attribute("errorMessage", "Owner not found with id: 99"));
	}

	@Test
	void deleteOwnerSuccess() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/delete", OWNER_ID))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners"))
			.andExpect(flash().attributeExists("successMessage"));

		verify(deletion).deleteOwner(OWNER_ID);
	}

	@Test
	void deleteOwnerBlockedRedirectsToOwnerDetails() throws Exception {
		willThrow(new DeleteBlockedException("Owner", OWNER_ID, "the owner still has 1 pet(s)")).given(deletion)
			.deleteOwner(OWNER_ID);

		mockMvc.perform(post("/owners/{ownerId}/delete", OWNER_ID))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners/1"))
			.andExpect(flash().attributeExists("errorMessage"))
			.andExpect(flash().attributeCount(1));
	}

	@Test
	void deleteOwnerNotFound() throws Exception {
		willThrow(new ResourceNotFoundException("Owner", 99)).given(deletion).deleteOwner(99);

		mockMvc.perform(post("/owners/{ownerId}/delete", 99))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners"))
			.andExpect(flash().attributeExists("errorMessage"));
	}

	@Test
	void confirmDeletePetShowsConfirmationPage() throws Exception {
		Pet pet = new Pet();
		pet.setName("Leo");
		given(deletion.getPet(OWNER_ID, PET_ID)).willReturn(pet);

		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/delete", OWNER_ID, PET_ID))
			.andExpect(status().isOk())
			.andExpect(view().name("confirmDelete"))
			.andExpect(model().attribute("entityName", "Pet"))
			.andExpect(model().attribute("postUrl", "/owners/1/pets/10/delete"))
			.andExpect(model().attribute("cancelUrl", "/owners/1"));
	}

	@Test
	void deletePetSuccess() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/delete", OWNER_ID, PET_ID))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners/1"))
			.andExpect(flash().attributeExists("successMessage"));

		verify(deletion).deletePet(OWNER_ID, PET_ID);
	}

	@Test
	void deletePetBlockedWhenVisitsExist() throws Exception {
		willThrow(new DeleteBlockedException("Pet", PET_ID, "the pet still has 2 visit(s)")).given(deletion)
			.deletePet(OWNER_ID, PET_ID);

		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/delete", OWNER_ID, PET_ID))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners/1"))
			.andExpect(flash().attributeExists("errorMessage"))
			.andExpect(flash().attributeCount(1));
	}

	@Test
	void deletePetNotFoundRedirectsToOwnerDetails() throws Exception {
		willThrow(new ResourceNotFoundException("Pet", PET_ID)).given(deletion).deletePet(OWNER_ID, PET_ID);

		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/delete", OWNER_ID, PET_ID))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners/1"))
			.andExpect(flash().attribute("errorMessage", "Pet not found with id: 10"));
	}

	@Test
	void deletePetWithMissingOwnerRedirectsToOwnersList() throws Exception {
		willThrow(new ResourceNotFoundException("Owner", 99)).given(deletion).deletePet(99, PET_ID);

		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/delete", 99, PET_ID))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners"));
	}

	@Test
	void confirmDeleteVisitShowsConfirmationPage() throws Exception {
		given(deletion.getVisit(OWNER_ID, PET_ID, VISIT_ID)).willReturn(new Visit());

		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/visits/{visitId}/delete", OWNER_ID, PET_ID, VISIT_ID))
			.andExpect(status().isOk())
			.andExpect(view().name("confirmDelete"))
			.andExpect(model().attribute("entityName", "Visit"))
			.andExpect(model().attribute("postUrl", "/owners/1/pets/10/visits/100/delete"))
			.andExpect(model().attribute("cancelUrl", "/owners/1"));
	}

	@Test
	void deleteVisitSuccess() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/visits/{visitId}/delete", OWNER_ID, PET_ID, VISIT_ID))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners/1"))
			.andExpect(flash().attributeExists("successMessage"));

		verify(deletion).deleteVisit(OWNER_ID, PET_ID, VISIT_ID);
	}

	@Test
	void deleteVisitNotFoundRedirectsToOwnerDetails() throws Exception {
		willThrow(new ResourceNotFoundException("Visit", VISIT_ID)).given(deletion)
			.deleteVisit(OWNER_ID, PET_ID, VISIT_ID);

		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/visits/{visitId}/delete", OWNER_ID, PET_ID, VISIT_ID))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners/1"))
			.andExpect(flash().attribute("errorMessage", "Visit not found with id: 100"));
	}

}
