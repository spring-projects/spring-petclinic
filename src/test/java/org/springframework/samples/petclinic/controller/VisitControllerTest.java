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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.samples.petclinic.common.CommonAttribute;
import org.springframework.samples.petclinic.common.CommonEndPoint;
import org.springframework.samples.petclinic.common.CommonView;
import org.springframework.samples.petclinic.dto.PetDTO;
import org.springframework.samples.petclinic.dto.PetTypeDTO;
import org.springframework.samples.petclinic.dto.VisitDTO;
import org.springframework.samples.petclinic.model.business.Visit;
import org.springframework.samples.petclinic.service.business.PetService;
import org.springframework.samples.petclinic.service.business.VisitService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Test class for {@link VisitController}
 *
 * @author Colin But
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@WebMvcTest(VisitController.class)
@ExtendWith(MockitoExtension.class)
class VisitControllerTest {

	private final static Integer OWNER_ID = 5;

	private final static Integer PET_ID = 14;

	private final static Integer PET_TYPE_ID = 6;

	private final static String PET_TYPE_NAME = "dragon";

	private final static String PET_NAME = "bowser";

	private final static String PET_BIRTH_DATE = "2020-07-11";

	private final static String VISIT_DESCRIPTION = "Visit Description";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	SimpMessagingTemplate simpMessagingTemplate;

	@MockBean
	private VisitService visitService;

	@MockBean
	private PetService petService;

	static private PetDTO petDTO;

	private Visit visit;

	private VisitDTO visitDTO;

	@BeforeAll
	static void beforeAll() {
		PetTypeDTO petTypeDTO = new PetTypeDTO();
		petTypeDTO.setName(PET_TYPE_NAME);
		petTypeDTO.setId(PET_TYPE_ID);
		petDTO = new PetDTO();
		petDTO.setId(PET_ID);
		petDTO.setName(PET_NAME);
		petDTO.setType(petTypeDTO);
		petDTO.setBirthDate(LocalDate.parse(PET_BIRTH_DATE));
	}

	@BeforeEach
	void beforeEach() {
		visitDTO = new VisitDTO();
		visitDTO.setPetId(PET_ID);
		visitDTO.setDate(LocalDate.now());
		visitDTO.setDescription(VISIT_DESCRIPTION);
	}

	@Test
	@Tag("initNewVisitForm")
	@DisplayName("Verify that return form for new Visit with right Pet")
	void testInitNewVisitForm() throws Exception {
		given(this.petService.findById(PET_ID)).willReturn(petDTO);

		final MvcResult result = mockMvc.perform(get(CommonEndPoint.VISITS_NEW, PET_ID))
				.andExpect(status().is2xxSuccessful()).andExpect(view().name(CommonView.VISIT_CREATE_OR_UPDATE))
				.andReturn();

		PetDTO found = (PetDTO) Objects.requireNonNull(result.getModelAndView()).getModel().get(CommonAttribute.PET);

		assertThat(found).isEqualToIgnoringGivenFields(petDTO, CommonAttribute.VISITS);

		VisitDTO expected = new VisitDTO();
		expected.setDate(LocalDate.now());
		expected.setPetId(PET_ID);

		assertThat(found.getVisits()).contains(expected);
	}

	@Test
	@Tag("processNewVisitForm")
	@DisplayName("Verify that save Visit")
	void givenVisitAndOwnerIDAndPetId_whenSaveVisit_thenSaveVisit() throws Exception {
		ArgumentCaptor<VisitDTO> captor = ArgumentCaptor.forClass(VisitDTO.class);

		mockMvc.perform(post(CommonEndPoint.VISITS_EDIT, OWNER_ID, PET_ID).flashAttr(CommonAttribute.VISIT, visitDTO))
				.andExpect(status().is3xxRedirection()).andExpect(view().name(CommonView.OWNER_OWNERS_ID_R))
				.andReturn();

		verify(visitService, times(1)).save(captor.capture());

		assertThat(captor.getValue()).isEqualTo(visitDTO);
	}

	@Test
	@Tag("processNewVisitForm")
	@DisplayName("Verify that return to update Visit view if Visit has no date")
	void givenVisitAndOwnerIDAndPetIdAndVisitWithoutDate_whenSaveVisit_thenReturnToCreationView() throws Exception {
		visitDTO.setDate(null);

		mockMvc.perform(post(CommonEndPoint.VISITS_EDIT, OWNER_ID, PET_ID).flashAttr(CommonAttribute.PET, petDTO)
				.flashAttr(CommonAttribute.VISIT, visitDTO)).andExpect(status().is2xxSuccessful())
				.andExpect(view().name(CommonView.VISIT_CREATE_OR_UPDATE));
	}

	@Test
	@Tag("processNewVisitForm")
	@DisplayName("Verify that return to update Visit view if Visit has no description")
	void givenVisitAndOwnerIDAndPetIdAndVisitWithoutDescription_whenSaveVisit_thenReturnToCreationView()
			throws Exception {
		visitDTO.setDescription("");

		mockMvc.perform(post(CommonEndPoint.VISITS_EDIT, OWNER_ID, PET_ID).flashAttr(CommonAttribute.PET, petDTO)
				.flashAttr(CommonAttribute.VISIT, visitDTO)).andExpect(status().is2xxSuccessful())
				.andExpect(view().name(CommonView.VISIT_CREATE_OR_UPDATE));
	}

}
