package org.springframework.samples.petclinic.it;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.vet.VetRepository;

/**
 * Integration tests for the Vet listing (HTML and JSON). Expected counts come from the
 * repository; the name checks (James, Carter) rely on the seed data in db/h2/data.sql.
 */
class VetFlowIT extends IntegrationTestSupport {

	@Autowired
	private VetRepository vets;

	@Test
	void shouldListVetsAsHtml() throws Exception {
		this.mockMvc.perform(get("/vets.html").param("page", "1"))
			.andExpect(status().isOk())
			.andExpect(view().name("vets/vetList"))
			.andExpect(model().attribute("totalItems", (long) this.vets.findAll().size()))
			.andExpect(content().string(containsString("James")));
	}

	@Test
	void shouldListVetsAsJson() throws Exception {
		this.mockMvc.perform(get("/vets").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.vetList.length()").value(this.vets.findAll().size()))
			.andExpect(jsonPath("$.vetList[?(@.lastName == 'Carter')]").exists());
	}

}
