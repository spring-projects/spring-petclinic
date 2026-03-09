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
package org.springframework.samples.petclinic.vet;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for {@link VetController}
 *
 * @author Andrew Huebner
 */
@SpringBootTest
@AutoConfigureMockMvc
class VetControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private VetRepository vetRepository;

	@Test
	void testShowVetList() throws Exception {
		Vet vet = new Vet();
		vet.setFirstName("John");
		vet.setLastName("Doe");
		Page<Vet> page = new PageImpl<>(List.of(vet), PageRequest.of(0, 5), 1);

		when(vetRepository.findAll(PageRequest.of(0, 5))).thenReturn(page);

		mockMvc.perform(get("/vets.html"))
			.andExpect(status().isOk())
			.andExpect(view().name("vets/vetList"))
			.andExpect(model().attributeExists("currentPage", "totalPages", "totalItems", "listVets"));
	}

	@Test
	void testShowResourcesVetList() throws Exception {
		Vet vet = new Vet();
		vet.setFirstName("Jane");
		vet.setLastName("Smith");

		when(vetRepository.findAll()).thenReturn(List.of(vet));

		mockMvc.perform(get("/vets"))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$.vetList[0].firstName").value("Jane"));
	}

}