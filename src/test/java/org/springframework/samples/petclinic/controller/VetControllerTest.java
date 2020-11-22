/*
 * Copyright 2012-2019 the original author or authors.
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

package org.springframework.samples.petclinic.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.samples.petclinic.common.CommonAttribute;
import org.springframework.samples.petclinic.common.CommonEndPoint;
import org.springframework.samples.petclinic.common.CommonView;
import org.springframework.samples.petclinic.dto.SpecialtyDTO;
import org.springframework.samples.petclinic.dto.VetDTO;
import org.springframework.samples.petclinic.dto.VetsDTO;
import org.springframework.samples.petclinic.service.business.VetService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * Test class for the {@link VetController}
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@WebMvcTest(VetController.class)
class VetControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	SimpMessagingTemplate simpMessagingTemplate;

	@MockBean
	private VetService vetService;

	static List<VetDTO> vetDTOS;

	@BeforeAll
	static void beforeAll() {
		VetDTO james = new VetDTO();
		james.setFirstName("James");
		james.setLastName("Carter");
		james.setId(1);
		VetDTO helen = new VetDTO();
		helen.setFirstName("Helen");
		helen.setLastName("Leary");
		helen.setId(2);
		SpecialtyDTO radiology = new SpecialtyDTO();
		radiology.setId(1);
		radiology.setName("radiology");
		helen.addSpecialty(radiology);

		vetDTOS = Lists.newArrayList(james, helen);
	}

	@BeforeEach
	void beforeEach() {
		given(this.vetService.findAll()).willReturn(vetDTOS);
	}

	@Test
	@Tag("showVetList")
	@DisplayName("When asking vets get String containing Vets")
	void whenGetVets_thenReturnStringOfVets() throws Exception {
		final MvcResult result = mockMvc.perform(get(CommonEndPoint.VETS_HTML)).andExpect(status().is2xxSuccessful())
				.andExpect(model().attributeExists(CommonAttribute.VETS))
				.andExpect(view().name(CommonView.VET_VETS_LIST)).andReturn();

		VetsDTO found = (VetsDTO) Objects.requireNonNull(result.getModelAndView()).getModel().get(CommonAttribute.VETS);

		assertThat(found.getVetList()).isEqualTo(vetDTOS);
	}

	@Test
	@Tag("showResourcesVetList")
	@DisplayName("When asking vets get Vets DTO object containing Vets")
	void whenGetVets_thenReturnVetsDTO() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		final MvcResult result = mockMvc.perform(get(CommonEndPoint.VETS).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful()).andReturn();

		String json = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

		VetsDTO found = mapper.readValue(json, VetsDTO.class);

		assertThat(found.getVetList()).isEqualTo(vetDTOS);
	}

}
