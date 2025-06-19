/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for {@link OwnerController}
 *
 * @author Colin But
 * @author Wick Dynex
 */
@WebMvcTest(OwnerController.class)
@DisabledInNativeImage
@DisabledInAotMode
class OwnerControllerTests {

	private static final int TEST_OWNER_ID = 1;
	private static final String OWNER_ATTRIBUTE = "owner";
	private static final String FIRST_NAME = "firstName";
	private static final String LAST_NAME = "lastName";
	private static final String ADDRESS = "address";
	private static final String CITY = "London";
	private static final String TELEPHONE = "telephone";
	private static final String VIEW_CREATE_OR_UPDATE_OWNER_FORM = "owners/createOrUpdateOwnerForm";
	private static final String VIEW_FIND_OWNERS = "owners/findOwners";
	private static final String VIEW_OWNERS_LIST = "owners/ownersList";
	private static final String VIEW_OWNER_DETAILS = "owners/ownerDetails";
	private static final String OWNER_ID_EDIT_URL = "/owners/{ownerId}/edit";
	private static final String OWNER_NEW_URL = "/owners/new";
	private static final String OWNER_FIND_URL = "/owners/find";
	private static final String OWNER_REDIRECT_URL = "redirect:/owners/";
	private static final String TEST_LAST_NAME = "Franklin";
	private static final String TEST_FIRST_NAME = "George";
	private static final String TEST_ADDRESS = "110 W. Liberty St.";
	private static final String TEST_CITY = "Madison";
	private static final String TEST_TELEPHONE = "6085551023";
	private static final String TEST_LAST_NAME_BLOGGS = "Bloggs";
	private static final String TEST_CITY_LONDON = "London";
	private static final String TEST_ADDRESS_CARAMEL = "123 Caramel Street";
	private static final String TEST_TELEPHONE_JOE = "1316761638";

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private OwnerRepository owners;

	private Owner george() {
		Owner george = new Owner();
		george.setId(TEST_OWNER_ID);
		george.setFirstName(TEST_FIRST_NAME);
		george.setLastName(TEST_LAST_NAME);
		george.setAddress(TEST_ADDRESS);
		george.setCity(TEST_CITY);
		george.setTelephone(TEST_TELEPHONE);
		Pet max = new Pet();
		PetType dog = new PetType();
		dog.setName("dog");
		max.setType(dog);
		max.setName("Max");
		max.setBirthDate(LocalDate.now());
		george.addPet(max);
		max.setId(1);
		return george;
	}

	@BeforeEach
	void setup() {
		Owner george = george();
		given(this.owners.findByLastNameStartingWith(eq(TEST_LAST_NAME), any(Pageable.class)))
			.willReturn(new PageImpl<>(List.of(george)));

		given(this.owners.findById(TEST_OWNER_ID)).willReturn(Optional.of(george));
		Visit visit = new Visit();
		visit.setDate(LocalDate.now());
		george.getPet("Max").getVisits().add(visit);
	}

	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get(OWNER_NEW_URL))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists(OWNER_ATTRIBUTE))
			.andExpect(view().name(VIEW_CREATE_OR_UPDATE_OWNER_FORM));
	}

	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc
			.perform(post(OWNER_NEW_URL).param(FIRST_NAME, "Joe")
				.param(LAST_NAME, TEST_LAST_NAME_BLOGGS)
				.param(ADDRESS, TEST_ADDRESS_CARAMEL)
				.param("city", TEST_CITY_LONDON)
				.param(TELEPHONE, TEST_TELEPHONE_JOE))
			.andExpect(status().is3xxRedirection());
	}

	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc
			.perform(post(OWNER_NEW_URL).param(FIRST_NAME, "Joe").param(LAST_NAME, TEST_LAST_NAME_BLOGGS).param("city", TEST_CITY_LONDON))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors(OWNER_ATTRIBUTE))
			.andExpect(model().attributeHasFieldErrors(OWNER_ATTRIBUTE, ADDRESS))
			.andExpect(model().attributeHasFieldErrors(OWNER_ATTRIBUTE, TELEPHONE))
			.andExpect(view().name(VIEW_CREATE_OR_UPDATE_OWNER_FORM));
	}

	@Test
	void testInitFindForm() throws Exception {
		mockMvc.perform(get(OWNER_FIND_URL))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists(OWNER_ATTRIBUTE))
			.andExpect(view().name(VIEW_FIND_OWNERS));
	}

	@Test
	void testProcessFindFormSuccess() throws Exception {
		Page<Owner> tasks = new PageImpl<>(List.of(george(), new Owner()));
		when(this.owners.findByLastNameStartingWith(anyString(), any(Pageable.class))).thenReturn(tasks);
		mockMvc.perform(get("/owners?page=1")).andExpect(status().isOk()).andExpect(view().name(VIEW_OWNERS_LIST));
	}

	@Test
	void testProcessFindFormByLastName() throws Exception {
		Page<Owner> tasks = new PageImpl<>(List.of(george()));
		when(this.owners.findByLastNameStartingWith(eq(TEST_LAST_NAME), any(Pageable.class))).thenReturn(tasks);
		mockMvc.perform(get("/owners?page=1").param(LAST_NAME, TEST_LAST_NAME))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name(OWNER_REDIRECT_URL + TEST_OWNER_ID));
	}

	@Test
	void testProcessFindFormNoOwnersFound() throws Exception {
		Page<Owner> tasks = new PageImpl<>(List.of());
		when(this.owners.findByLastNameStartingWith(eq("Unknown Surname"), any(Pageable.class))).thenReturn(tasks);
		mockMvc.perform(get("/owners?page=1").param(LAST_NAME, "Unknown Surname"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasFieldErrors(OWNER_ATTRIBUTE, LAST_NAME))
			.andExpect(model().attributeHasFieldErrorCode(OWNER_ATTRIBUTE, LAST_NAME, "notFound"))
			.andExpect(view().name(VIEW_FIND_OWNERS));
	}

	@Test
	void testInitUpdateOwnerForm() throws Exception {
		mockMvc.perform(get(OWNER_ID_EDIT_URL, TEST_OWNER_ID))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists(OWNER_ATTRIBUTE))
			.andExpect(model().attribute(OWNER_ATTRIBUTE, hasProperty(LAST_NAME, is(TEST_LAST_NAME))))
			.andExpect(model().attribute(OWNER_ATTRIBUTE, hasProperty(FIRST_NAME, is(TEST_FIRST_NAME))))
			.andExpect(model().attribute(OWNER_ATTRIBUTE, hasProperty(ADDRESS, is(TEST_ADDRESS))))
			.andExpect(model().attribute(OWNER_ATTRIBUTE, hasProperty("city", is(TEST_CITY))))
			.andExpect(model().attribute(OWNER_ATTRIBUTE, hasProperty(TELEPHONE, is(TEST_TELEPHONE))))
			.andExpect(view().name(VIEW_CREATE_OR_UPDATE_OWNER_FORM));
	}

	@Test
	void testProcessUpdateOwnerFormSuccess() throws Exception {
		mockMvc
			.perform(post(OWNER_ID_EDIT_URL, TEST_OWNER_ID).param(FIRST_NAME, "Joe")
				.param(LAST_NAME, TEST_LAST_NAME_BLOGGS)
				.param(ADDRESS, TEST_ADDRESS_CARAMEL)
				.param("city", TEST_CITY_LONDON)
				.param(TELEPHONE, "1616291589"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name(OWNER_REDIRECT_URL + "{ownerId}"));
	}

	@Test
	void testProcessUpdateOwnerFormUnchangedSuccess() throws Exception {
		mockMvc.perform(post(OWNER_ID_EDIT_URL, TEST_OWNER_ID))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name(OWNER_REDIRECT_URL + "{ownerId}"));
	}

	@Test
	void testProcessUpdateOwnerFormHasErrors() throws Exception {
		mockMvc
			.perform(post(OWNER_ID_EDIT_URL, TEST_OWNER_ID).param(FIRST_NAME, "Joe")
				.param(LAST_NAME, TEST_LAST_NAME_BLOGGS)
				.param(ADDRESS, "")
				.param(TELEPHONE, ""))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors(OWNER_ATTRIBUTE))
			.andExpect(model().attributeHasFieldErrors(OWNER_ATTRIBUTE, ADDRESS))
			.andExpect(model().attributeHasFieldErrors(OWNER_ATTRIBUTE, TELEPHONE))
			.andExpect(view().name(VIEW_CREATE_OR_UPDATE_OWNER_FORM));
	}

	@Test
	void testShowOwner() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}", TEST_OWNER_ID))
			.andExpect(status().isOk())
			.andExpect(model().attribute(OWNER_ATTRIBUTE, hasProperty(LAST_NAME, is(TEST_LAST_NAME))))
			.andExpect(model().attribute(OWNER_ATTRIBUTE, hasProperty(FIRST_NAME, is(TEST_FIRST_NAME))))
			.andExpect(model().attribute(OWNER_ATTRIBUTE, hasProperty(ADDRESS, is(TEST_ADDRESS))))
			.andExpect(model().attribute(OWNER_ATTRIBUTE, hasProperty("city", is(TEST_CITY))))
			.andExpect(model().attribute(OWNER_ATTRIBUTE, hasProperty(TELEPHONE, is(TEST_TELEPHONE))))
			.andExpect(model().attribute(OWNER_ATTRIBUTE, hasProperty("pets", not(empty()))))
			.andExpect(model().attribute(OWNER_ATTRIBUTE,
					hasProperty("pets", hasItem(hasProperty("visits", hasSize(greaterThan(0)))))))
			.andExpect(view().name(VIEW_OWNER_DETAILS));
	}

	@Test
	public void testProcessUpdateOwnerFormWithIdMismatch() throws Exception {
		int pathOwnerId = 1;

		Owner owner = new Owner();
		owner.setId(2);
		owner.setFirstName("John");
		owner.setLastName("Doe");
		owner.setAddress("Center Street");
		owner.setCity("New York");
		owner.setTelephone("0123456789");

		when(owners.findById(pathOwnerId)).thenReturn(Optional.of(owner));

		mockMvc.perform(MockMvcRequestBuilders.post(OWNER_ID_EDIT_URL, pathOwnerId).flashAttr(OWNER_ATTRIBUTE, owner))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl(OWNER_REDIRECT_URL + pathOwnerId + "/edit"))
			.andExpect(flash().attributeExists("error"));
	}

}