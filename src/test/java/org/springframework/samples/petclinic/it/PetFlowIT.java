package org.springframework.samples.petclinic.it;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;

/**
 * Integration tests for the Pet flows (add and edit a pet of an owner).
 */
class PetFlowIT extends AbstractIntegrationTest {

	@Test
	void shouldAddPetToOwner() throws Exception {
		Owner owner = saveOwner(uniqueLastName());

		this.mockMvc
			.perform(post("/owners/{ownerId}/pets/new", owner.getId()).param("name", "Rex")
				.param("birthDate", "2020-05-10")
				.param("type", "dog"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/owners/" + owner.getId()));

		Owner reloaded = this.owners.findById(owner.getId()).orElseThrow();
		assertThat(reloaded.getPets()).hasSize(1);
		Pet pet = reloaded.getPets().get(0);
		assertThat(pet.getName()).isEqualTo("Rex");
		assertThat(pet.getType().getName()).isEqualTo("dog");
	}

	@Test
	void shouldEditPet() throws Exception {
		Owner owner = saveOwner(uniqueLastName());
		this.mockMvc
			.perform(post("/owners/{ownerId}/pets/new", owner.getId()).param("name", "Bolt")
				.param("birthDate", "2019-03-01")
				.param("type", "dog"))
			.andExpect(status().is3xxRedirection());
		Pet pet = this.owners.findById(owner.getId()).orElseThrow().getPets().get(0);

		this.mockMvc
			.perform(post("/owners/{ownerId}/pets/{petId}/edit", owner.getId(), pet.getId()).param("name", "Thunder")
				.param("birthDate", "2019-03-02")
				.param("type", "cat"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/owners/" + owner.getId()));

		Pet edited = this.owners.findById(owner.getId()).orElseThrow().getPets().get(0);
		assertThat(edited.getId()).isEqualTo(pet.getId());
		assertThat(edited.getName()).isEqualTo("Thunder");
		assertThat(edited.getType().getName()).isEqualTo("cat");
		assertThat(edited.getBirthDate()).hasToString("2019-03-02");
	}

}
