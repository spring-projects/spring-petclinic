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

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.assertj.core.util.Lists;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.common.CommonAttribute;
import org.springframework.samples.petclinic.common.CommonEndPoint;
import org.springframework.samples.petclinic.common.CommonView;
import org.springframework.samples.petclinic.dto.OwnerDTO;
import org.springframework.samples.petclinic.dto.PetDTO;
import org.springframework.samples.petclinic.dto.PetTypeDTO;
import org.springframework.samples.petclinic.dto.VisitDTO;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.VisitService;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
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
@WebMvcTest(OwnerController.class)
class OwnerControllerTests {

	private static final int TEST_OWNER_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OwnerService owners;

	@MockBean
	private VisitService visits;

	private OwnerDTO george;

	@BeforeEach
	void setup() {
		george = new OwnerDTO();
		george.setId(TEST_OWNER_ID);
		george.setFirstName("George");
		george.setLastName("Franklin");
		george.setAddress("110 W. Liberty St.");
		george.setCity("Madison");
		george.setTelephone("6085551023");
		PetDTO max = new PetDTO();
		PetTypeDTO dog = new PetTypeDTO();
		dog.setName("dog");
		max.setId(1);
		max.setType(dog);
		max.setName("Max");
		max.setBirthDate(LocalDate.now());
		george.setPetsInternal(Collections.singleton(max));
		given(this.owners.findById(TEST_OWNER_ID)).willReturn(george);
		VisitDTO visit = new VisitDTO();
		visit.setDate(LocalDate.now());
		given(this.visits.findByPetId(max.getId())).willReturn(Collections.singletonList(visit));
	}

	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get(CommonEndPoint.OWNERS_NEW))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists(CommonAttribute.OWNER))
			.andExpect(view().name(CommonView.OWNER_CREATE_OR_UPDATE));
	}

	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/new")
			.param(CommonAttribute.OWNER_FIRST_NAME, "Joe")
			.param(CommonAttribute.OWNER_LAST_NAME, "Bloggs")
			.param(CommonAttribute.OWNER_ADDRESS, "123 Caramel Street")
			.param(CommonAttribute.OWNER_CITY, "London")
			.param(CommonAttribute.OWNER_PHONE, "01316761638"))
			.andExpect(status().is3xxRedirection());
	}

	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post(CommonEndPoint.OWNERS_NEW)
			.param(CommonAttribute.OWNER_FIRST_NAME, "Joe")
			.param(CommonAttribute.OWNER_LAST_NAME, "Bloggs")
			.param(CommonAttribute.OWNER_CITY, "London"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors(CommonAttribute.OWNER))
			.andExpect(model().attributeHasFieldErrors(CommonAttribute.OWNER, CommonAttribute.OWNER_ADDRESS))
			.andExpect(model().attributeHasFieldErrors(CommonAttribute.OWNER, CommonAttribute.OWNER_PHONE))
			.andExpect(view().name(CommonView.OWNER_CREATE_OR_UPDATE));
	}

	@Test
	void testInitFindForm() throws Exception {
		mockMvc.perform(get(CommonEndPoint.OWNERS_FIND))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists(CommonAttribute.OWNER))
			.andExpect(view().name(CommonView.OWNER_FIND_OWNERS));
	}

	@Test
	void testProcessFindFormSuccess() throws Exception {
		given(this.owners.findByLastName("")).willReturn(Lists.newArrayList(george, new OwnerDTO()));

		mockMvc.perform(get(CommonEndPoint.OWNERS))
			.andExpect(status().isOk())
			.andExpect(view().name(CommonView.OWNER_OWNERS_LIST));
	}

	@Test
	void testProcessFindFormByLastName() throws Exception {
		given(this.owners.findByLastName(george.getLastName())).willReturn(Lists.newArrayList(george));

		mockMvc.perform(get(CommonEndPoint.OWNERS)
			.param(CommonAttribute.OWNER_LAST_NAME, "Franklin"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name(CommonView.OWNER_OWNERS_R + TEST_OWNER_ID));
	}

	@Test
	void testProcessFindFormNoOwnersFound() throws Exception {
		mockMvc.perform(get(CommonEndPoint.OWNERS)
			.param(CommonAttribute.OWNER_LAST_NAME,"Unknown Surname"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasFieldErrors(CommonAttribute.OWNER, CommonAttribute.OWNER_LAST_NAME))
			.andExpect(model().attributeHasFieldErrorCode(CommonAttribute.OWNER, CommonAttribute.OWNER_LAST_NAME, "notFound"))
			.andExpect(view().name(CommonView.OWNER_FIND_OWNERS));
	}

	@Test
	void testInitUpdateOwnerForm() throws Exception {
		mockMvc.perform(get(CommonEndPoint.OWNERS_ID_EDIT, TEST_OWNER_ID))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists(CommonAttribute.OWNER))
			.andExpect(model().attribute(CommonAttribute.OWNER, hasProperty(CommonAttribute.OWNER_LAST_NAME, is("Franklin"))))
			.andExpect(model().attribute(CommonAttribute.OWNER, hasProperty(CommonAttribute.OWNER_FIRST_NAME, is("George"))))
			.andExpect(model().attribute(CommonAttribute.OWNER, hasProperty(CommonAttribute.OWNER_ADDRESS, is("110 W. Liberty St."))))
			.andExpect(model().attribute(CommonAttribute.OWNER, hasProperty(CommonAttribute.OWNER_CITY, is("Madison"))))
			.andExpect(model().attribute(CommonAttribute.OWNER, hasProperty(CommonAttribute.OWNER_PHONE, is("6085551023"))))
			.andExpect(view().name(CommonView.OWNER_CREATE_OR_UPDATE));
	}

	@Test
	void testProcessUpdateOwnerFormSuccess() throws Exception {
		mockMvc.perform(post(CommonEndPoint.OWNERS_ID_EDIT, TEST_OWNER_ID)
			.param(CommonAttribute.OWNER_FIRST_NAME, "Joe")
			.param(CommonAttribute.OWNER_LAST_NAME, "Bloggs")
			.param(CommonAttribute.OWNER_ADDRESS, "123 Caramel Street")
			.param(CommonAttribute.OWNER_CITY, "London")
			.param(CommonAttribute.OWNER_PHONE, "01616291589"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name(CommonView.OWNER_OWNERS_ID_R));
	}

	@Test
	void testProcessUpdateOwnerFormHasErrors() throws Exception {
		mockMvc.perform(post(CommonEndPoint.OWNERS_ID_EDIT, TEST_OWNER_ID)
			.param(CommonAttribute.OWNER_FIRST_NAME, "Joe")
			.param(CommonAttribute.OWNER_LAST_NAME, "Bloggs")
			.param(CommonAttribute.OWNER_CITY, "London"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors(CommonAttribute.OWNER))
			.andExpect(model().attributeHasFieldErrors(CommonAttribute.OWNER, CommonAttribute.OWNER_ADDRESS))
			.andExpect(model().attributeHasFieldErrors(CommonAttribute.OWNER, CommonAttribute.OWNER_PHONE))
			.andExpect(view().name(CommonView.OWNER_CREATE_OR_UPDATE));
	}

	@Test
	void testShowOwner() throws Exception {
		mockMvc.perform(get(CommonEndPoint.OWNERS_ID, TEST_OWNER_ID))
			.andExpect(status().isOk())
			.andExpect(model().attribute(CommonAttribute.OWNER, hasProperty(CommonAttribute.OWNER_LAST_NAME, is("Franklin"))))
			.andExpect(model().attribute(CommonAttribute.OWNER, hasProperty(CommonAttribute.OWNER_FIRST_NAME, is("George"))))
			.andExpect(model().attribute(CommonAttribute.OWNER, hasProperty(CommonAttribute.OWNER_ADDRESS, is("110 W. Liberty St."))))
			.andExpect(model().attribute(CommonAttribute.OWNER, hasProperty(CommonAttribute.OWNER_CITY, is("Madison"))))
			.andExpect(model().attribute(CommonAttribute.OWNER, hasProperty(CommonAttribute.OWNER_PHONE, is("6085551023"))))
			.andExpect(model().attribute(CommonAttribute.OWNER, hasProperty(CommonAttribute.OWNER_PETS, not(empty()))))
			.andExpect(model().attribute(CommonAttribute.OWNER, hasProperty(CommonAttribute.OWNER_PETS, new BaseMatcher<List<PetDTO>>() {

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
