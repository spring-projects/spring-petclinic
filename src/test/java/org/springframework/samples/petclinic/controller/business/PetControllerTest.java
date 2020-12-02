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

package org.springframework.samples.petclinic.controller.business;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.samples.petclinic.common.CommonAttribute;
import org.springframework.samples.petclinic.common.CommonEndPoint;
import org.springframework.samples.petclinic.common.CommonError;
import org.springframework.samples.petclinic.common.CommonView;
import org.springframework.samples.petclinic.controller.WebSecurityConfig;
import org.springframework.samples.petclinic.controller.business.PetController;
import org.springframework.samples.petclinic.dto.business.OwnerDTO;
import org.springframework.samples.petclinic.dto.business.PetDTO;
import org.springframework.samples.petclinic.dto.business.PetTypeDTO;
import org.springframework.samples.petclinic.formatter.PetTypeFormatter;
import org.springframework.samples.petclinic.service.business.OwnerService;
import org.springframework.samples.petclinic.service.business.PetService;
import org.springframework.samples.petclinic.service.business.PetTypeService;
import org.springframework.samples.petclinic.service.common.UserDetailsServiceImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for the {@link PetController}
 *
 * @author Colin But
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@WebMvcTest(value = PetController.class,
		includeFilters = @ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE))
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class PetControllerTest {

	private static final int TEST_OWNER_ID = 1;

	private static final int TEST_PET_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserDetailsServiceImpl userDetailsService;

	@MockBean
	SimpMessagingTemplate simpMessagingTemplate;

	@MockBean
	private PetService petService;

	@MockBean
	private PetTypeService petTypeService;

	@MockBean
	private OwnerService ownerService;

	@BeforeEach
	void beforeEach() {
		PetTypeDTO cat = new PetTypeDTO();
		cat.setId(3);
		cat.setName("hamster");

		given(this.ownerService.findById(TEST_OWNER_ID)).willReturn(new OwnerDTO());
		given(this.petService.findById(TEST_PET_ID)).willReturn(new PetDTO());
		given(this.petService.findPetTypes()).willReturn(Lists.newArrayList(cat));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("initCreationForm")
	@DisplayName("Verify that Pet creation form is initialized")
	void givenOwnerId_whenAskToCreatePet_thenDisplayCreationViewWithRightPet() throws Exception {
		mockMvc.perform(get(CommonEndPoint.OWNERS_ID + CommonEndPoint.PETS_NEW, TEST_OWNER_ID))
				.andExpect(status().is2xxSuccessful()).andExpect(view().name(CommonView.PET_CREATE_OR_UPDATE))
				.andExpect(model().attributeExists(CommonAttribute.PET));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("processCreationForm")
	@DisplayName("Verify that call the right view with parameters when attempt to create Pet")
	void givenNewPet_whenPostNewPet_thenSavePetAndRedirectToOwnerView() throws Exception {
		mockMvc.perform(post(CommonEndPoint.OWNERS_ID + CommonEndPoint.PETS_NEW, TEST_OWNER_ID)
				.param(CommonAttribute.PET_NAME, "Betty").param(CommonAttribute.PET_TYPE, "hamster")
				.param(CommonAttribute.PET_BIRTH_DATE, "2015-02-12")).andExpect(status().is3xxRedirection())
				.andExpect(view().name(CommonView.OWNER_OWNERS_ID_R));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("processCreationForm")
	@DisplayName("Verify that return to Pet creation form when pet has no name")
	void givenNewPetWithoutName_whenPostNewPet_thenRedirectToPetUpdate() throws Exception {
		mockMvc.perform(post(CommonEndPoint.OWNERS_ID + CommonEndPoint.PETS_NEW, TEST_OWNER_ID)
				.param(CommonAttribute.PET_TYPE, "hamster").param(CommonAttribute.PET_BIRTH_DATE, "2015-02-12"))
				.andExpect(model().attributeHasNoErrors(CommonAttribute.OWNER))
				.andExpect(model().attributeHasErrors(CommonAttribute.PET))
				.andExpect(model().attributeHasFieldErrors(CommonAttribute.PET, CommonAttribute.PET_NAME))
				.andExpect(model().attributeHasFieldErrorCode(CommonAttribute.PET, CommonAttribute.PET_NAME,
						CommonError.REQUIRED_ARGS))
				.andExpect(status().isOk()).andExpect(view().name(CommonView.PET_CREATE_OR_UPDATE));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("processCreationForm")
	@DisplayName("Verify that return to Pet creation form when pet has no type")
	void givenNewPetWithoutType_whenPostNewPet_thenRedirectToPetUpdate() throws Exception {

		mockMvc.perform(post(CommonEndPoint.OWNERS_ID + CommonEndPoint.PETS_NEW, TEST_OWNER_ID)
				.param(CommonAttribute.PET_NAME, "Betty").param(CommonAttribute.PET_BIRTH_DATE, "2015-02-12"))
				.andExpect(model().attributeHasNoErrors(CommonAttribute.OWNER))
				.andExpect(model().attributeHasErrors(CommonAttribute.PET))
				.andExpect(model().attributeHasFieldErrors(CommonAttribute.PET, CommonAttribute.PET_TYPE))
				.andExpect(model().attributeHasFieldErrorCode(CommonAttribute.PET, CommonAttribute.PET_TYPE,
						CommonError.REQUIRED_ARGS))
				.andExpect(status().isOk()).andExpect(view().name(CommonView.PET_CREATE_OR_UPDATE));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("processCreationForm")
	@DisplayName("Verify that return to Pet creation form when pet has wrong Owner ID")
	void givenNewPetWithWrongOwner_whenPostNewPet_thenRedirectToPetUpdate() throws Exception {
		mockMvc.perform(
				post(CommonEndPoint.OWNERS_ID + CommonEndPoint.PETS_NEW, 22).param(CommonAttribute.PET_NAME, "Betty")
						.param(CommonAttribute.PET_TYPE, "hamster").param(CommonAttribute.PET_BIRTH_DATE, "2015-02-12"))
				.andExpect(model().attributeHasErrors(CommonAttribute.PET))
				.andExpect(model().attributeHasFieldErrorCode(CommonAttribute.PET, CommonAttribute.OWNER,
						CommonError.NOT_FOUND_ARGS))
				.andExpect(status().isOk()).andExpect(view().name(CommonView.PET_CREATE_OR_UPDATE));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("processCreationForm")
	@DisplayName("Verify that return to Pet creation form when pet has no birth date")
	void givenNewPetWithoutBirthDate_whenPostNewPet_thenRedirectToPetUpdate() throws Exception {

		mockMvc.perform(post(CommonEndPoint.OWNERS_ID + CommonEndPoint.PETS_NEW, TEST_OWNER_ID)
				.param(CommonAttribute.PET_NAME, "Betty").param(CommonAttribute.PET_TYPE, "hamster"))
				.andExpect(model().attributeHasNoErrors(CommonAttribute.OWNER))
				.andExpect(model().attributeHasErrors(CommonAttribute.PET))
				.andExpect(model().attributeHasFieldErrors(CommonAttribute.PET, CommonAttribute.PET_BIRTH_DATE))
				.andExpect(model().attributeHasFieldErrorCode(CommonAttribute.PET, CommonAttribute.PET_BIRTH_DATE,
						CommonError.REQUIRED_ARGS))
				.andExpect(status().isOk()).andExpect(view().name(CommonView.PET_CREATE_OR_UPDATE));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("initUpdateForm")
	@DisplayName("Verify that Pet update form is initialized with the right Pet")
	void givenPetId_whenGetUpdatePet_thenReturnUpdateViewWithPet() throws Exception {
		mockMvc.perform(get(CommonEndPoint.OWNERS_ID + CommonEndPoint.PETS_ID_EDIT, TEST_OWNER_ID, TEST_PET_ID))
				.andExpect(status().isOk()).andExpect(model().attributeExists(CommonAttribute.PET))
				.andExpect(view().name(CommonView.PET_CREATE_OR_UPDATE));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("processUpdateForm")
	void givenOwnerAndModifiedPet_whenAskToUpdatePet_thenUpdatePetAndDisplayOwnerView() throws Exception {
		mockMvc.perform(post(CommonEndPoint.OWNERS_ID + CommonEndPoint.PETS_ID_EDIT, TEST_OWNER_ID, TEST_PET_ID)
				.param(CommonAttribute.PET_NAME, "Betty").param(CommonAttribute.PET_TYPE, "hamster")
				.param(CommonAttribute.PET_BIRTH_DATE, "2015-02-12")).andExpect(status().is3xxRedirection())
				.andExpect(view().name(CommonView.OWNER_OWNERS_ID_R));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("processUpdateForm")
	void testProcessUpdateFormHasErrors() throws Exception {
		mockMvc.perform(post(CommonEndPoint.OWNERS_ID + CommonEndPoint.PETS_ID_EDIT, TEST_OWNER_ID, TEST_PET_ID)
				.param(CommonAttribute.PET_NAME, "Betty").param(CommonAttribute.PET_BIRTH_DATE, "2015-02-12"))
				.andExpect(model().attributeHasNoErrors(CommonAttribute.OWNER))
				.andExpect(model().attributeHasErrors(CommonAttribute.PET)).andExpect(status().isOk())
				.andExpect(view().name(CommonView.PET_CREATE_OR_UPDATE));
	}

}
