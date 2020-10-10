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

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.common.CommonAttribute;
import org.springframework.samples.petclinic.common.CommonEndPoint;
import org.springframework.samples.petclinic.common.CommonView;
import org.springframework.samples.petclinic.dto.PetDTO;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for {@link VisitController}
 *
 * @author Colin But
 */
@WebMvcTest(VisitController.class)
class VisitControllerTests {

	private static final int TEST_PET_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private VisitService visitService;

	@MockBean
	private PetService petService;

	@BeforeEach
	void init() {
		given(this.petService.findById(TEST_PET_ID)).willReturn(new PetDTO());
	}

	@Test
	@Tag("initNewVisitForm")
	void testInitNewVisitForm() throws Exception {
		mockMvc.perform(get(CommonEndPoint.VISITS_NEW, TEST_PET_ID)).andExpect(status().isOk())
				.andExpect(view().name(CommonView.PET_CREATE_OR_UPDATE));
	}

	@Test
	@Tag("processNewVisitForm")
	void testProcessNewVisitFormSuccess() throws Exception {
		mockMvc.perform(post(CommonEndPoint.VISITS_NEW, TEST_PET_ID).param(CommonAttribute.NAME, "George")
				.param(CommonAttribute.DESCRIPTION, "Visit Description")).andExpect(status().is3xxRedirection())
				.andExpect(view().name(CommonView.OWNER_OWNERS_ID_R));
	}

	@Test
	@Tag("processNewVisitForm")
	void testProcessNewVisitFormHasErrors() throws Exception {
		mockMvc.perform(post(CommonEndPoint.VISITS_NEW, TEST_PET_ID).param(CommonAttribute.NAME, "George"))
				.andExpect(model().attributeHasErrors(CommonAttribute.VISIT)).andExpect(status().isOk())
				.andExpect(view().name(CommonView.VISIT_CREATE_OR_UPDATE));
	}

}
