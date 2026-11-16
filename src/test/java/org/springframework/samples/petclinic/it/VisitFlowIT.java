package org.springframework.samples.petclinic.it;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;

/**
 * Integration tests for the Visit flow (book a visit for a pet).
 */
class VisitFlowIT extends AbstractIntegrationTest {

	private Pet addPet(Owner owner) throws Exception {
		this.mockMvc
			.perform(post("/owners/{ownerId}/pets/new", owner.getId()).param("name", "Mel")
				.param("birthDate", "2021-07-15")
				.param("type", "hamster"))
			.andExpect(status().is3xxRedirection());
		return this.owners.findById(owner.getId()).orElseThrow().getPets().get(0);
	}

	@Test
	void shouldBookVisitForPet() throws Exception {
		Owner owner = saveOwner(uniqueLastName());
		Pet pet = addPet(owner);
		LocalDate date = LocalDate.now().plusDays(7);

		this.mockMvc
			.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", owner.getId(), pet.getId())
				.param("date", date.toString())
				.param("description", "Vacina anual"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/owners/" + owner.getId()));

		Pet reloaded = this.owners.findById(owner.getId()).orElseThrow().getPet(pet.getId());
		assertThat(reloaded.getVisits()).hasSize(1);
		assertThat(reloaded.getVisits().iterator().next().getDescription()).isEqualTo("Vacina anual");
		assertThat(reloaded.getVisits().iterator().next().getDate()).isEqualTo(date);
	}

	@Test
	void shouldRejectVisitWithoutDescription() throws Exception {
		Owner owner = saveOwner(uniqueLastName());
		Pet pet = addPet(owner);

		this.mockMvc
			.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", owner.getId(), pet.getId())
				.param("date", LocalDate.now().plusDays(1).toString())
				.param("description", ""))
			.andExpect(status().isOk())
			.andExpect(view().name("pets/createOrUpdateVisitForm"));

		assertThat(this.owners.findById(owner.getId()).orElseThrow().getPet(pet.getId()).getVisits()).isEmpty();
	}

}
