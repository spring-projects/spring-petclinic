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

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.assertj.core.util.Lists;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.samples.petclinic.common.CommonAttribute;
import org.springframework.samples.petclinic.common.CommonEndPoint;
import org.springframework.samples.petclinic.common.CommonError;
import org.springframework.samples.petclinic.common.CommonView;
import org.springframework.samples.petclinic.controller.WebSecurityConfig;
import org.springframework.samples.petclinic.controller.business.OwnerController;
import org.springframework.samples.petclinic.controller.common.WebSocketSender;
import org.springframework.samples.petclinic.dto.business.OwnerDTO;
import org.springframework.samples.petclinic.dto.business.PetDTO;
import org.springframework.samples.petclinic.dto.business.PetTypeDTO;
import org.springframework.samples.petclinic.dto.business.VisitDTO;
import org.springframework.samples.petclinic.service.business.OwnerService;
import org.springframework.samples.petclinic.service.business.VisitService;
import org.springframework.samples.petclinic.service.common.UserDetailsServiceImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Test class for {@link OwnerController}
 *
 * @author Colin But
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@RunWith(SpringRunner.class)
@WebMvcTest(OwnerController.class)
class OwnerControllerTest extends WebSocketSender {

	private static final int OWNER_ID = 15;

	private static final String OWNER_FIRST_NAME = "Joe";

	private static final String OWNER_LAST_NAME = "BLOGGS";

	private static final String OWNER_ADDRESS = "123 Caramel Street";

	private static final String OWNER_CITY = "London";

	private static final String OWNER_PHONE = "6085551023";

	private final static Integer PET_ID = 14;

	private final static String PET_NAME = "bowser";

	private final static LocalDate PET_BIRTH_DATE = LocalDate.of(2020, 7, 11);

	private final static Integer PET_TYPE_ID = 4;

	private final static String PET_TYPE_NAME = "dinausor";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserDetailsServiceImpl userDetailsService;

	@MockBean
	SimpMessagingTemplate simpMessagingTemplate;

	@MockBean
	private OwnerService owners;

	@MockBean
	private VisitService visits;

	private OwnerDTO ownerDTO;

	@BeforeEach
	void setup() {
		ownerDTO = new OwnerDTO();
		ownerDTO.setId(OWNER_ID);
		ownerDTO.setFirstName(OWNER_FIRST_NAME);
		ownerDTO.setLastName(OWNER_LAST_NAME);
		ownerDTO.setAddress(OWNER_ADDRESS);
		ownerDTO.setCity(OWNER_CITY);
		ownerDTO.setTelephone(OWNER_PHONE);
		PetDTO petDTO = new PetDTO();
		PetTypeDTO petTypeDTO = new PetTypeDTO();
		petTypeDTO.setId(PET_TYPE_ID);
		petTypeDTO.setName(PET_TYPE_NAME);
		petDTO.setId(PET_ID);
		petDTO.setName(PET_NAME);
		petDTO.setType(petTypeDTO);
		petDTO.setBirthDate(PET_BIRTH_DATE);
		ownerDTO.setPetsInternal(Collections.singleton(petDTO));

		VisitDTO visit = new VisitDTO();
		visit.setDate(LocalDate.now());
		given(this.owners.findById(OWNER_ID)).willReturn(ownerDTO);
		given(this.owners.save(any(OwnerDTO.class))).willReturn(ownerDTO);
		given(this.visits.findByPetId(petDTO.getId())).willReturn(Collections.singletonList(visit));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("initCreationForm")
	@DisplayName("Verify that we get the right creation view and the right attribute name")
	void whenGetNewOwner_thenReturnCreationViewWithNewOwner() throws Exception {
		mockMvc.perform(get(CommonEndPoint.OWNERS_NEW)).andExpect(status().is2xxSuccessful())
				.andExpect(view().name(CommonView.OWNER_CREATE_OR_UPDATE))
				.andExpect(model().attributeExists(CommonAttribute.OWNER));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("processCreationForm")
	@DisplayName("Verify that call the right view with parameters when attempt to create Owner")
	void givenNewOwner_whenPostNewOwner_thenSaveOwnerAndRedirectToOwnerView() throws Exception {
		// Put Owner ID in endpoint
		String endPoint = CommonView.OWNER_OWNERS_ID_R.replace("{ownerId}", String.valueOf(OWNER_ID));

		mockMvc.perform(post(CommonEndPoint.OWNERS_NEW).param(CommonAttribute.OWNER_FIRST_NAME, OWNER_FIRST_NAME)
				.param(CommonAttribute.OWNER_LAST_NAME, OWNER_LAST_NAME)
				.param(CommonAttribute.OWNER_ADDRESS, OWNER_ADDRESS).param(CommonAttribute.OWNER_CITY, OWNER_CITY)
				.param(CommonAttribute.OWNER_PHONE, OWNER_PHONE)).andExpect(status().is3xxRedirection())
				.andExpect(view().name(endPoint));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("processCreationForm")
	@DisplayName("Verify that return to Owner creation form when Owner has no firstName")
	void givenNewOwnerWithoutFirstName_whenPostNewOwner_thenRedirectToOwnerUpdateView() throws Exception {

		mockMvc.perform(post(CommonEndPoint.OWNERS_NEW).param(CommonAttribute.OWNER_LAST_NAME, OWNER_LAST_NAME)
				.param(CommonAttribute.OWNER_ADDRESS, OWNER_ADDRESS).param(CommonAttribute.OWNER_CITY, OWNER_CITY)
				.param(CommonAttribute.OWNER_PHONE, OWNER_PHONE)).andExpect(status().isOk())
				.andExpect(model().attributeHasErrors(CommonAttribute.OWNER))
				.andExpect(model().attributeHasFieldErrors(CommonAttribute.OWNER, CommonAttribute.OWNER_FIRST_NAME))
				.andExpect(view().name(CommonView.OWNER_CREATE_OR_UPDATE));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("processCreationForm")
	@DisplayName("Verify that return to Owner creation form when Owner has no lastName")
	void givenNewOwnerWithoutLastName_whenPostNewOwner_thenRedirectToOwnerUpdateView() throws Exception {

		mockMvc.perform(post(CommonEndPoint.OWNERS_NEW).param(CommonAttribute.OWNER_FIRST_NAME, OWNER_FIRST_NAME)
				.param(CommonAttribute.OWNER_ADDRESS, OWNER_ADDRESS).param(CommonAttribute.OWNER_CITY, OWNER_CITY)
				.param(CommonAttribute.OWNER_PHONE, OWNER_PHONE)).andExpect(status().isOk())
				.andExpect(model().attributeHasErrors(CommonAttribute.OWNER))
				.andExpect(model().attributeHasFieldErrors(CommonAttribute.OWNER, CommonAttribute.OWNER_LAST_NAME))
				.andExpect(view().name(CommonView.OWNER_CREATE_OR_UPDATE));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("processCreationForm")
	@DisplayName("Verify that return to Owner creation form when Owner has no address")
	void givenNewOwnerWithoutAddress_whenPostNewOwner_thenRedirectToOwnerUpdateView() throws Exception {

		mockMvc.perform(post(CommonEndPoint.OWNERS_NEW).param(CommonAttribute.OWNER_FIRST_NAME, OWNER_FIRST_NAME)
				.param(CommonAttribute.OWNER_LAST_NAME, OWNER_LAST_NAME).param(CommonAttribute.OWNER_CITY, OWNER_CITY)
				.param(CommonAttribute.OWNER_PHONE, OWNER_PHONE)).andExpect(status().isOk())
				.andExpect(model().attributeHasErrors(CommonAttribute.OWNER))
				.andExpect(model().attributeHasFieldErrors(CommonAttribute.OWNER, CommonAttribute.OWNER_ADDRESS))
				.andExpect(view().name(CommonView.OWNER_CREATE_OR_UPDATE));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("processCreationForm")
	@DisplayName("Verify that return to Owner creation form when Owner has no phone")
	void givenNewOwnerWithoutPhone_whenPostNewOwner_thenRedirectToOwnerUpdateView() throws Exception {

		mockMvc.perform(post(CommonEndPoint.OWNERS_NEW).param(CommonAttribute.OWNER_FIRST_NAME, OWNER_FIRST_NAME)
				.param(CommonAttribute.OWNER_LAST_NAME, OWNER_LAST_NAME)
				.param(CommonAttribute.OWNER_ADDRESS, OWNER_ADDRESS).param(CommonAttribute.OWNER_CITY, OWNER_CITY))
				.andExpect(status().isOk()).andExpect(model().attributeHasErrors(CommonAttribute.OWNER))
				.andExpect(model().attributeHasFieldErrors(CommonAttribute.OWNER, CommonAttribute.OWNER_PHONE))
				.andExpect(view().name(CommonView.OWNER_CREATE_OR_UPDATE));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("initFindForm")
	@DisplayName("Verify that we get the right find view and the right attribute name")
	void whenGetFindOwner_thenReturnFindViewWithNewOwner() throws Exception {
		mockMvc.perform(get(CommonEndPoint.OWNERS_FIND)).andExpect(status().is2xxSuccessful())
				.andExpect(view().name(CommonView.OWNER_FIND_OWNERS))
				.andExpect(model().attributeExists(CommonAttribute.OWNER));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("processFindForm")
	@DisplayName("Verify that we get the right view and all Owners list")
	void whenGetFindOwner_thenReturnFindViewWithAllOwners() throws Exception {
		given(this.owners.findByLastName("")).willReturn(Lists.newArrayList(ownerDTO, new OwnerDTO()));

		mockMvc.perform(get(CommonEndPoint.OWNERS)).andExpect(status().is2xxSuccessful())
				.andExpect(view().name(CommonView.OWNER_OWNERS_LIST));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("processFindForm")
	@DisplayName("Verify that we get the right view and the Owner with specified firstName")
	void givenOwnerLastName_whenGetFindOwner_thenReturnViewWithRightOwner() throws Exception {
		given(this.owners.findByLastName(ownerDTO.getLastName())).willReturn(Lists.newArrayList(ownerDTO));

		mockMvc.perform(get(CommonEndPoint.OWNERS).param(CommonAttribute.OWNER_LAST_NAME, OWNER_LAST_NAME))
				.andExpect(status().is3xxRedirection()).andExpect(view().name(CommonView.OWNER_OWNERS_R + OWNER_ID));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("processFindForm")
	@DisplayName("Verify that we get empty view and errors with specified wrong firstName")
	void givenWrongOwnerLastName_whenGetFindOwner_thenReturnViewWithoutOwner() throws Exception {
		mockMvc.perform(get(CommonEndPoint.OWNERS).param(CommonAttribute.OWNER_LAST_NAME, "Unknown Surname"))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasFieldErrors(CommonAttribute.OWNER, CommonAttribute.OWNER_LAST_NAME))
				.andExpect(model().attributeHasFieldErrorCode(CommonAttribute.OWNER, CommonAttribute.OWNER_LAST_NAME,
						CommonError.NOT_FOUND_ARGS))
				.andExpect(view().name(CommonView.OWNER_FIND_OWNERS));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("initUpdateOwnerForm")
	@DisplayName("Verify that we get the right update view and the right Owner")
	void whenGetUpdateOwner_thenReturnUpdateViewWithRightOwner() throws Exception {
		mockMvc.perform(get(CommonEndPoint.OWNERS_ID_EDIT, OWNER_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists(CommonAttribute.OWNER))
				.andExpect(model().attribute(CommonAttribute.OWNER,
						hasProperty(CommonAttribute.OWNER_LAST_NAME, is(OWNER_LAST_NAME))))
				.andExpect(model().attribute(CommonAttribute.OWNER,
						hasProperty(CommonAttribute.OWNER_FIRST_NAME, is(OWNER_FIRST_NAME))))
				.andExpect(model().attribute(CommonAttribute.OWNER,
						hasProperty(CommonAttribute.OWNER_ADDRESS, is(OWNER_ADDRESS))))
				.andExpect(model().attribute(CommonAttribute.OWNER,
						hasProperty(CommonAttribute.OWNER_CITY, is(OWNER_CITY))))
				.andExpect(model().attribute(CommonAttribute.OWNER,
						hasProperty(CommonAttribute.OWNER_PHONE, is(OWNER_PHONE))))
				.andExpect(view().name(CommonView.OWNER_CREATE_OR_UPDATE));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("processUpdateOwnerForm")
	@DisplayName("Verify that call the right view with parameters when attempt to update Owner")
	void givenUpdatedOwner_whenPostOwner_thenSaveOwnerAndRedirectToOwnerView() throws Exception {
		mockMvc.perform(
				post(CommonEndPoint.OWNERS_ID_EDIT, OWNER_ID).param(CommonAttribute.OWNER_FIRST_NAME, OWNER_FIRST_NAME)
						.param(CommonAttribute.OWNER_LAST_NAME, OWNER_LAST_NAME)
						.param(CommonAttribute.OWNER_ADDRESS, OWNER_ADDRESS)
						.param(CommonAttribute.OWNER_CITY, OWNER_CITY).param(CommonAttribute.OWNER_PHONE, OWNER_PHONE))
				.andExpect(status().is3xxRedirection()).andExpect(view().name(CommonView.OWNER_OWNERS_ID_R));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("processUpdateOwnerForm")
	@DisplayName("Verify that we return to update view if the Owner firsName is wrong")
	void givenUpdatedOwnerWithoutFirstName_whenPostOwner_thenRedirectToUpdateOwnerView() throws Exception {
		mockMvc.perform(
				post(CommonEndPoint.OWNERS_ID_EDIT, OWNER_ID).param(CommonAttribute.OWNER_LAST_NAME, OWNER_LAST_NAME)
						.param(CommonAttribute.OWNER_ADDRESS, OWNER_ADDRESS)
						.param(CommonAttribute.OWNER_CITY, OWNER_CITY).param(CommonAttribute.OWNER_PHONE, OWNER_PHONE))
				.andExpect(status().isOk()).andExpect(model().attributeHasErrors(CommonAttribute.OWNER))
				.andExpect(model().attributeHasFieldErrors(CommonAttribute.OWNER, CommonAttribute.OWNER_FIRST_NAME))
				.andExpect(view().name(CommonView.OWNER_CREATE_OR_UPDATE));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("processUpdateOwnerForm")
	@DisplayName("Verify that we return to update view if the Owner lastName is wrong")
	void givenUpdatedOwnerWithoutLastName_whenPostOwner_thenRedirectToUpdateOwnerView() throws Exception {
		mockMvc.perform(
				post(CommonEndPoint.OWNERS_ID_EDIT, OWNER_ID).param(CommonAttribute.OWNER_FIRST_NAME, OWNER_FIRST_NAME)
						.param(CommonAttribute.OWNER_ADDRESS, OWNER_ADDRESS)
						.param(CommonAttribute.OWNER_CITY, OWNER_CITY).param(CommonAttribute.OWNER_PHONE, OWNER_PHONE))
				.andExpect(status().isOk()).andExpect(model().attributeHasErrors(CommonAttribute.OWNER))
				.andExpect(model().attributeHasFieldErrors(CommonAttribute.OWNER, CommonAttribute.OWNER_LAST_NAME))
				.andExpect(view().name(CommonView.OWNER_CREATE_OR_UPDATE));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("processUpdateOwnerForm")
	@DisplayName("Verify that we return to update view if the Owner address is wrong")
	void givenUpdatedOwnerWithoutAddress_whenPostOwner_thenRedirectToUpdateOwnerView() throws Exception {
		mockMvc.perform(
				post(CommonEndPoint.OWNERS_ID_EDIT, OWNER_ID).param(CommonAttribute.OWNER_FIRST_NAME, OWNER_FIRST_NAME)
						.param(CommonAttribute.OWNER_LAST_NAME, OWNER_LAST_NAME)
						.param(CommonAttribute.OWNER_CITY, OWNER_CITY).param(CommonAttribute.OWNER_PHONE, OWNER_PHONE))
				.andExpect(status().isOk()).andExpect(model().attributeHasErrors(CommonAttribute.OWNER))
				.andExpect(model().attributeHasFieldErrors(CommonAttribute.OWNER, CommonAttribute.OWNER_ADDRESS))
				.andExpect(view().name(CommonView.OWNER_CREATE_OR_UPDATE));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("processUpdateOwnerForm")
	@DisplayName("Verify that we return to update view if the Owner phone is wrong")
	void givenUpdatedOwnerWithoutPhone_whenPostOwner_thenRedirectToUpdateOwnerView() throws Exception {
		mockMvc.perform(post(CommonEndPoint.OWNERS_ID_EDIT, OWNER_ID)
				.param(CommonAttribute.OWNER_FIRST_NAME, OWNER_FIRST_NAME)
				.param(CommonAttribute.OWNER_LAST_NAME, OWNER_LAST_NAME)
				.param(CommonAttribute.OWNER_ADDRESS, OWNER_ADDRESS).param(CommonAttribute.OWNER_CITY, OWNER_CITY))
				.andExpect(status().isOk()).andExpect(model().attributeHasErrors(CommonAttribute.OWNER))
				.andExpect(model().attributeHasFieldErrors(CommonAttribute.OWNER, CommonAttribute.OWNER_PHONE))
				.andExpect(view().name(CommonView.OWNER_CREATE_OR_UPDATE));
	}

	@Test
	@WithMockUser(value = WebSecurityConfig.TEST_USER)
	@Tag("processUpdateOwnerForm")
	@DisplayName("Verify that we display view with right Owner")
	void givenOwnerId_whenGetOwner_thenShowOwnerView() throws Exception {
		mockMvc.perform(get(CommonEndPoint.OWNERS_ID, OWNER_ID)).andExpect(status().isOk())
				.andExpect(model().attribute(CommonAttribute.OWNER,
						hasProperty(CommonAttribute.OWNER_LAST_NAME, is(OWNER_LAST_NAME))))
				.andExpect(model().attribute(CommonAttribute.OWNER,
						hasProperty(CommonAttribute.OWNER_FIRST_NAME, is(OWNER_FIRST_NAME))))
				.andExpect(model().attribute(CommonAttribute.OWNER,
						hasProperty(CommonAttribute.OWNER_ADDRESS, is(OWNER_ADDRESS))))
				.andExpect(model().attribute(CommonAttribute.OWNER,
						hasProperty(CommonAttribute.OWNER_CITY, is(OWNER_CITY))))
				.andExpect(model().attribute(CommonAttribute.OWNER,
						hasProperty(CommonAttribute.OWNER_PHONE, is(OWNER_PHONE))))
				.andExpect(
						model().attribute(CommonAttribute.OWNER, hasProperty(CommonAttribute.OWNER_PETS, not(empty()))))
				.andExpect(model().attribute(CommonAttribute.OWNER,
						hasProperty(CommonAttribute.OWNER_PETS, new BaseMatcher<List<PetDTO>>() {

							@Override
							public boolean matches(Object item) {
								@SuppressWarnings("unchecked")
								List<PetDTO> pets = (List<PetDTO>) item;
								PetDTO pet = pets.get(0);
								if (pet.getVisits().isEmpty()) {
									return false;
								}

								return true;
							}

							@Override
							public void describeTo(Description description) {
								description.appendText("Max did not have any visits");
							}
						})))
				.andExpect(view().name(CommonView.OWNER_DETAILS));
	}

}
