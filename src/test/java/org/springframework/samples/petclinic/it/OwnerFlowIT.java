package org.springframework.samples.petclinic.it;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.owner.Owner;

/**
 * Integration tests for the Owner flows (create, search, edit, validation).
 */
class OwnerFlowIT extends AbstractIntegrationTest {

	@Test
	void shouldCreateOwnerAndPersistIt() throws Exception {
		String lastName = uniqueLastName();

		this.mockMvc
			.perform(post("/owners/new").param("firstName", "Joao")
				.param("lastName", lastName)
				.param("address", "Av. Afonso Pena, 1000")
				.param("city", "Belo Horizonte")
				.param("telephone", "3133334444"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrlPattern("/owners/*"));

		List<Owner> found = this.owners.findByLastNameStartingWith(lastName, Pageable.unpaged()).getContent();
		assertThat(found).hasSize(1);
		assertThat(found.get(0).getFirstName()).isEqualTo("Joao");
		assertThat(found.get(0).getCity()).isEqualTo("Belo Horizonte");
	}

	@Test
	void shouldFindOwnerByLastNameAndRedirectToDetails() throws Exception {
		Owner owner = saveOwner(uniqueLastName());

		this.mockMvc.perform(get("/owners").param("page", "1").param("lastName", owner.getLastName()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/owners/" + owner.getId()));
	}

	@Test
	void shouldListSeveralOwnersWhenLastNameMatchesMany() throws Exception {
		String prefix = uniqueLastName();
		saveOwner(prefix + "A");
		saveOwner(prefix + "B");

		this.mockMvc.perform(get("/owners").param("page", "1").param("lastName", prefix))
			.andExpect(status().isOk())
			.andExpect(view().name("owners/ownersList"))
			.andExpect(model().attribute("totalItems", 2L));
	}

	@Test
	void shouldReportNotFoundWhenNoOwnerMatches() throws Exception {
		this.mockMvc.perform(get("/owners").param("page", "1").param("lastName", "NoSuch" + uniqueLastName()))
			.andExpect(status().isOk())
			.andExpect(view().name("owners/findOwners"))
			.andExpect(model().attributeHasFieldErrors("owner", "lastName"));
	}

	@Test
	void shouldEditOwner() throws Exception {
		Owner owner = saveOwner(uniqueLastName());

		this.mockMvc
			.perform(post("/owners/{id}/edit", owner.getId()).param("firstName", "Mariana")
				.param("lastName", owner.getLastName())
				.param("address", "Rua Nova, 42")
				.param("city", "Contagem")
				.param("telephone", "3199998888"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/owners/" + owner.getId()));

		Owner updated = this.owners.findById(owner.getId()).orElseThrow();
		assertThat(updated.getFirstName()).isEqualTo("Mariana");
		assertThat(updated.getCity()).isEqualTo("Contagem");
		assertThat(updated.getTelephone()).isEqualTo("3199998888");
	}

	@Test
	void shouldRejectOwnerWithMissingRequiredFields() throws Exception {
		this.mockMvc
			.perform(post("/owners/new").param("firstName", "")
				.param("lastName", "")
				.param("address", "")
				.param("city", "")
				.param("telephone", ""))
			.andExpect(status().isOk())
			.andExpect(view().name("owners/createOrUpdateOwnerForm"))
			.andExpect(
					model().attributeHasFieldErrors("owner", "firstName", "lastName", "address", "city", "telephone"));
	}

}
