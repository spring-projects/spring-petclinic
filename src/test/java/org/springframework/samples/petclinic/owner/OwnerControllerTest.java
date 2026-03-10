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
package org.springframework.samples.petclinic.owner;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Tests for {@link OwnerController}.
 *
 * @author Wick Dynex
 */
@ExtendWith(MockitoExtension.class)
public class OwnerControllerTest {

	private static final int TEST_OWNER_ID = 1;

	@Mock
	private OwnerRepository ownerRepository;

	private MockMvc mockMvc;

	private Owner owner;

	@BeforeEach
	public void setup() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/jsp/");
		viewResolver.setSuffix(".jsp");

		OwnerController controller = new OwnerController(ownerRepository);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).setViewResolvers(viewResolver).build();

		owner = new Owner();
		owner.setId(TEST_OWNER_ID);
		owner.setFirstName("George");
		owner.setLastName("Franklin");
		owner.setAddress("110 W. Liberty St.");
		owner.setCity("Madison");
		owner.setTelephone("6085551023");
	}

	/**
	 * Test GET /owners/new - Display creation form
	 */
	@Test
	public void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/owners/new"))
			.andExpect(status().isOk())
			.andExpect(view().name("owners/createOrUpdateOwnerForm"))
			.andExpect(model().attributeExists("owner"));
	}

	/**
	 * Test POST /owners/new with valid data - Create new owner
	 */
	@Test
	public void testProcessCreationFormSuccess() throws Exception {
		Owner newOwner = new Owner();
		newOwner.setFirstName("John");
		newOwner.setLastName("Doe");
		newOwner.setAddress("123 Main St");
		newOwner.setCity("Chicago");
		newOwner.setTelephone("6085551234");
		newOwner.setId(2);

		when(ownerRepository.save(any(Owner.class))).thenReturn(newOwner);

		mockMvc
			.perform(post("/owners/new").param("firstName", "John")
				.param("lastName", "Doe")
				.param("address", "123 Main St")
				.param("city", "Chicago")
				.param("telephone", "6085551234"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/owners/2"));

		verify(ownerRepository, times(1)).save(any(Owner.class));
	}

	/**
	 * Test POST /owners/new with validation errors
	 */
	@Test
	public void testProcessCreationFormWithErrors() throws Exception {
		mockMvc.perform(post("/owners/new").param("firstName", "") // Missing first name
			.param("lastName", "Doe")
			.param("address", "123 Main St")
			.param("city", "Chicago")
			.param("telephone", "6085551234"))
			.andExpect(model().hasErrors())
			.andExpect(view().name("owners/createOrUpdateOwnerForm"));

		verify(ownerRepository, times(0)).save(any(Owner.class));
	}

	/**
	 * Test POST /owners/new with invalid telephone format
	 */
	@Test
	public void testProcessCreationFormWithInvalidTelephone() throws Exception {
		mockMvc
			.perform(post("/owners/new").param("firstName", "John")
				.param("lastName", "Doe")
				.param("address", "123 Main St")
				.param("city", "Chicago")
				.param("telephone", "123")) // Invalid telephone format
			.andExpect(model().hasErrors())
			.andExpect(view().name("owners/createOrUpdateOwnerForm"));

		verify(ownerRepository, times(0)).save(any(Owner.class));
	}

	/**
	 * Test GET /owners/find - Display find form
	 */
	@Test
	public void testInitFindForm() throws Exception {
		mockMvc.perform(get("/owners/find"))
			.andExpect(status().isOk())
			.andExpect(view().name("owners/findOwners"))
			.andExpect(model().attributeExists("owner"));
	}

	/**
	 * Test GET /owners with no search criteria - Returns all owners
	 */
	@Test
	public void testProcessFindFormDefaultAllOwners() throws Exception {
		List<Owner> ownersList = new ArrayList<>();
		ownersList.add(owner);
		Page<Owner> page = new PageImpl<>(ownersList, PageRequest.of(0, 5), 1);

		when(ownerRepository.findByLastNameStartingWith("", PageRequest.of(0, 5))).thenReturn(page);

		mockMvc.perform(get("/owners").param("page", "1"))
			.andExpect(status().isOk())
			.andExpect(view().name("owners/ownersList"))
			.andExpect(model().attribute("currentPage", 1))
			.andExpect(model().attribute("totalPages", 1))
			.andExpect(model().attribute("totalItems", 1L));

		verify(ownerRepository, times(1)).findByLastNameStartingWith("", PageRequest.of(0, 5));
	}

	/**
	 * Test GET /owners with single result - Redirect to owner details
	 */
	@Test
	public void testProcessFindFormSingleOwner() throws Exception {
		List<Owner> ownersList = new ArrayList<>();
		ownersList.add(owner);
		Page<Owner> page = new PageImpl<>(ownersList, PageRequest.of(0, 5), 1);

		when(ownerRepository.findByLastNameStartingWith("Franklin", PageRequest.of(0, 5))).thenReturn(page);

		mockMvc.perform(get("/owners").param("page", "1").param("lastName", "Franklin"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/owners/1"));

		verify(ownerRepository, times(1)).findByLastNameStartingWith("Franklin", PageRequest.of(0, 5));
	}

	/**
	 * Test GET /owners with multiple results
	 */
	@Test
	public void testProcessFindFormMultipleOwners() throws Exception {
		Owner owner2 = new Owner();
		owner2.setId(2);
		owner2.setFirstName("Peter");
		owner2.setLastName("Franklin");
		owner2.setAddress("485 Jackson St");
		owner2.setCity("San Francisco");
		owner2.setTelephone("6085552367");

		List<Owner> ownersList = new ArrayList<>();
		ownersList.add(owner);
		ownersList.add(owner2);
		Page<Owner> page = new PageImpl<>(ownersList, PageRequest.of(0, 5), 2);

		when(ownerRepository.findByLastNameStartingWith("Franklin", PageRequest.of(0, 5))).thenReturn(page);

		mockMvc.perform(get("/owners").param("page", "1").param("lastName", "Franklin"))
			.andExpect(status().isOk())
			.andExpect(view().name("owners/ownersList"))
			.andExpect(model().attribute("currentPage", 1))
			.andExpect(model().attribute("totalPages", 1))
			.andExpect(model().attribute("totalItems", 2L));

		verify(ownerRepository, times(1)).findByLastNameStartingWith("Franklin", PageRequest.of(0, 5));
	}

	/**
	 * Test GET /owners with no results
	 */
	@Test
	public void testProcessFindFormNothingFound() throws Exception {
		Page<Owner> emptyPage = new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 5), 0);

		when(ownerRepository.findByLastNameStartingWith("Xyz", PageRequest.of(0, 5))).thenReturn(emptyPage);

		mockMvc.perform(get("/owners").param("page", "1").param("lastName", "Xyz"))
			.andExpect(status().isOk())
			.andExpect(view().name("owners/findOwners"))
			.andExpect(model().hasErrors());

		verify(ownerRepository, times(1)).findByLastNameStartingWith("Xyz", PageRequest.of(0, 5));
	}

	/**
	 * Test GET /owners/{ownerId}/edit - Display edit form
	 */
	@Test
	public void testInitUpdateOwnerForm() throws Exception {
		when(ownerRepository.findById(TEST_OWNER_ID)).thenReturn(Optional.of(owner));

		mockMvc.perform(get("/owners/1/edit"))
			.andExpect(status().isOk())
			.andExpect(view().name("owners/createOrUpdateOwnerForm"))
			.andExpect(model().attributeExists("owner"));

		verify(ownerRepository, times(1)).findById(TEST_OWNER_ID);
	}

	/**
	 * Test POST /owners/{ownerId}/edit with valid data - Update owner
	 */
	@Test
	public void testProcessUpdateOwnerFormSuccess() throws Exception {
		when(ownerRepository.findById(TEST_OWNER_ID)).thenReturn(Optional.of(owner));
		when(ownerRepository.save(any(Owner.class))).thenReturn(owner);

		mockMvc
			.perform(post("/owners/1/edit").param("firstName", "George")
				.param("lastName", "Franklin")
				.param("address", "110 W. Liberty St.")
				.param("city", "Madison")
				.param("telephone", "6085551023"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/owners/{ownerId}"));

		verify(ownerRepository, times(1)).save(any(Owner.class));
	}

	/**
	 * Test POST /owners/{ownerId}/edit with validation errors
	 */
	@Test
	public void testProcessUpdateOwnerFormWithErrors() throws Exception {
		when(ownerRepository.findById(TEST_OWNER_ID)).thenReturn(Optional.of(owner));

		mockMvc.perform(post("/owners/1/edit").param("firstName", "") // Missing first
																		// name
			.param("lastName", "Franklin")
			.param("address", "110 W. Liberty St.")
			.param("city", "Madison")
			.param("telephone", "6085551023"))
			.andExpect(model().hasErrors())
			.andExpect(view().name("owners/createOrUpdateOwnerForm"));

		verify(ownerRepository, times(0)).save(any(Owner.class));
	}

	/**
	 * Test POST /owners/{ownerId}/edit with mismatched owner ID
	 */
	@Test
	public void testProcessUpdateOwnerFormWithIdMismatch() throws Exception {
		Owner mismatchedOwner = new Owner();
		mismatchedOwner.setId(999); // Different ID
		mismatchedOwner.setFirstName("George");
		mismatchedOwner.setLastName("Franklin");
		mismatchedOwner.setAddress("110 W. Liberty St.");
		mismatchedOwner.setCity("Madison");
		mismatchedOwner.setTelephone("6085551023");

		when(ownerRepository.findById(TEST_OWNER_ID)).thenReturn(Optional.of(owner));

		mockMvc.perform(post("/owners/1/edit").param("id", "999") // Mismatched ID
			.param("firstName", "George")
			.param("lastName", "Franklin")
			.param("address", "110 W. Liberty St.")
			.param("city", "Madison")
			.param("telephone", "6085551023")).andExpect(model().hasErrors());

		verify(ownerRepository, times(0)).save(any(Owner.class));
	}

	/**
	 * Test GET /owners/{ownerId} - Display owner details
	 */
	@Test
	public void testShowOwner() throws Exception {
		when(ownerRepository.findById(TEST_OWNER_ID)).thenReturn(Optional.of(owner));

		mockMvc.perform(get("/owners/1"))
			.andExpect(status().isOk())
			.andExpect(view().name("owners/ownerDetails"))
			.andExpect(model().attribute("owner", owner));

		verify(ownerRepository, times(1)).findById(TEST_OWNER_ID);
	}

	/**
	 * Test GET /owners/{ownerId} with non-existent owner
	 */
	@Test
	public void testShowOwnerNotFound() throws Exception {
		when(ownerRepository.findById(999)).thenReturn(Optional.empty());

		mockMvc.perform(get("/owners/999")).andExpect(status().is5xxServerError());

		verify(ownerRepository, times(1)).findById(999);
	}

	/**
	 * Test GET /owners/{ownerId}/edit with non-existent owner
	 */
	@Test
	public void testInitUpdateOwnerFormNotFound() throws Exception {
		when(ownerRepository.findById(999)).thenReturn(Optional.empty());

		mockMvc.perform(get("/owners/999/edit")).andExpect(status().is5xxServerError());

		verify(ownerRepository, times(1)).findById(999);
	}

	/**
	 * Test pagination with page 2
	 */
	@Test
	public void testProcessFindFormWithPagination() throws Exception {
		List<Owner> ownersList = new ArrayList<>();
		ownersList.add(owner);
		Page<Owner> page = new PageImpl<>(ownersList, PageRequest.of(1, 5), 10);

		when(ownerRepository.findByLastNameStartingWith("", PageRequest.of(1, 5))).thenReturn(page);

		mockMvc.perform(get("/owners").param("page", "2"))
			.andExpect(status().isOk())
			.andExpect(view().name("owners/ownersList"))
			.andExpect(model().attribute("currentPage", 2))
			.andExpect(model().attribute("totalPages", 2));

		verify(ownerRepository, times(1)).findByLastNameStartingWith("", PageRequest.of(1, 5));
	}

	/**
	 * Test POST /owners/new with all fields blank
	 */
	@Test
	public void testProcessCreationFormAllBlank() throws Exception {
		mockMvc.perform(post("/owners/new"))
			.andExpect(model().hasErrors())
			.andExpect(view().name("owners/createOrUpdateOwnerForm"));

		verify(ownerRepository, times(0)).save(any(Owner.class));
	}

	/**
	 * Test POST /owners/{ownerId}/edit with invalid telephone
	 */
	@Test
	public void testProcessUpdateOwnerFormWithInvalidTelephone() throws Exception {
		when(ownerRepository.findById(TEST_OWNER_ID)).thenReturn(Optional.of(owner));

		mockMvc
			.perform(post("/owners/1/edit").param("firstName", "George")
				.param("lastName", "Franklin")
				.param("address", "110 W. Liberty St.")
				.param("city", "Madison")
				.param("telephone", "invalid"))
			.andExpect(model().hasErrors());

		verify(ownerRepository, times(0)).save(any(Owner.class));
	}

	/**
	 * Test WebDataBinder disallows ID field
	 */
	@Test
	public void testCreateOwnerWithIdFromForm() throws Exception {
		Owner newOwner = new Owner();
		newOwner.setFirstName("John");
		newOwner.setLastName("Doe");
		newOwner.setAddress("123 Main St");
		newOwner.setCity("Chicago");
		newOwner.setTelephone("6085551234");
		newOwner.setId(2);

		when(ownerRepository.save(any(Owner.class))).thenReturn(newOwner);

		mockMvc.perform(post("/owners/new").param("id", "999") // ID should be disallowed
			.param("firstName", "John")
			.param("lastName", "Doe")
			.param("address", "123 Main St")
			.param("city", "Chicago")
			.param("telephone", "6085551234")).andExpect(status().is3xxRedirection());

		verify(ownerRepository, times(1)).save(any(Owner.class));
	}

}
