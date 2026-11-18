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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

/**
 * SWE 472 course project - graph coverage tests for
 * {@link PetController#processCreationForm}.
 * <p>
 * CFG nodes: 1 duplicate-name decision, 2 reject name, 3 future-date decision, 4 reject
 * birthDate, 5 hasErrors, 6 return form, 7 addPet + saveAndFlush (exception?), 8
 * unique-name constraint?, 9 rethrow, 10 reject name + return form, 11 flash + redirect,
 * 12 exit. Each test executes the test path in its name (Table XIV of the report).
 * <ul>
 * <li>Node and edge coverage: Q1, Q2, Q3, Q4</li>
 * <li>Additional tests: Q5 to Q10 (Q9 and Q10 make every clause of nodes 1 and 3 false
 * once, for full JaCoCo branch coverage)</li>
 * </ul>
 * The repository is a Mockito mock so that edges (7,8), (8,9) and (8,10) can be forced.
 *
 * @author Samuel Alene
 */
@WebMvcTest(value = PetController.class,
		includeFilters = @ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE))
@DisabledInNativeImage
@DisabledInAotMode
@DisplayName("SWE472 Graph - PetController.processCreationForm")
class Swe472PetControllerGraphTests {

	private static final int OWNER_ID = 1;

	private static final String FORM_VIEW = "pets/createOrUpdatePetForm";

	private static final String REDIRECT_VIEW = "redirect:/owners/{ownerId}";

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private OwnerRepository owners;

	@MockitoBean
	private PetTypeRepository types;

	private Owner owner;

	@BeforeEach
	void setUp() {
		PetType hamster = new PetType();
		hamster.setId(3);
		hamster.setName("hamster");
		given(this.types.findPetTypes()).willReturn(List.of(hamster));

		this.owner = new Owner();
		Pet petty = new Pet();
		petty.setId(1);
		petty.setName("petty");
		this.owner.addPet(petty);
		given(this.owners.findById(OWNER_ID)).willReturn(Optional.of(this.owner));
	}

	private static MockHttpServletRequestBuilder createPet(String name, LocalDate birthDate) {
		return post("/owners/{ownerId}/pets/new", OWNER_ID).param("name", name)
			.param("type", "hamster")
			.param("birthDate", birthDate.toString());
	}

	private static LocalDate pastDate() {
		return LocalDate.of(2015, 2, 12);
	}

	private static LocalDate tomorrow() {
		return LocalDate.now().plusDays(1);
	}

	@Test
	@DisplayName("CFG-C-Q1 [1,3,5,7,11,12] valid new pet -> saved, redirect with flash message")
	void cfgC_q1_validPetSaved() throws Exception {
		this.mockMvc.perform(createPet("Betty", pastDate()))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name(REDIRECT_VIEW))
			.andExpect(flash().attribute("message", "New Pet has been Added"));

		verify(this.owners, times(1)).saveAndFlush(this.owner);
		assertThat(this.owner.getPet("Betty")).isNotNull();
	}

	@Test
	@DisplayName("CFG-C-Q2 [1,2,3,4,5,6,12] duplicate name and future date -> form with both errors")
	void cfgC_q2_duplicateNameAndFutureDate() throws Exception {
		this.mockMvc.perform(createPet("petty", tomorrow()))
			.andExpect(status().isOk())
			.andExpect(view().name(FORM_VIEW))
			.andExpect(model().attributeErrorCount("pet", 2))
			.andExpect(model().attributeHasFieldErrorCode("pet", "name", "duplicate"))
			.andExpect(model().attributeHasFieldErrorCode("pet", "birthDate", "typeMismatch.birthDate"));

		verify(this.owners, never()).saveAndFlush(any(Owner.class));
	}

	@Test
	@DisplayName("CFG-C-Q3 [1,3,5,7,8,10,12] save violates unique_owner_pet_name -> form with name/duplicate")
	void cfgC_q3_uniqueConstraintViolation() throws Exception {
		given(this.owners.saveAndFlush(any(Owner.class))).willThrow(
				new DataIntegrityViolationException("could not execute statement; constraint [UNIQUE_OWNER_PET_NAME]"));

		this.mockMvc.perform(createPet("Betty", pastDate()))
			.andExpect(status().isOk())
			.andExpect(view().name(FORM_VIEW))
			.andExpect(model().attributeErrorCount("pet", 1))
			.andExpect(model().attributeHasFieldErrorCode("pet", "name", "duplicate"));
	}

	@Test
	@DisplayName("CFG-C-Q4 [1,3,5,7,8,9,12] save fails for another reason -> exception rethrown")
	void cfgC_q4_otherIntegrityViolationRethrown() {
		DataIntegrityViolationException failure = new DataIntegrityViolationException(
				"NULL not allowed for column TYPE_ID");
		given(this.owners.saveAndFlush(any(Owner.class))).willThrow(failure);

		Throwable thrown = catchThrowable(() -> this.mockMvc.perform(createPet("Betty", pastDate())));

		Throwable cause = (thrown instanceof DataIntegrityViolationException) ? thrown : thrown.getCause();
		assertThat(cause).isSameAs(failure);
	}

	@Test
	@DisplayName("CFG-C-Q5 [1,2,3,5,6,12] duplicate name only -> form with exactly one name/duplicate")
	void cfgC_q5_duplicateNameOnly() throws Exception {
		this.mockMvc.perform(createPet("petty", pastDate()))
			.andExpect(status().isOk())
			.andExpect(view().name(FORM_VIEW))
			.andExpect(model().attributeErrorCount("pet", 1))
			.andExpect(model().attributeHasFieldErrorCode("pet", "name", "duplicate"));

		verify(this.owners, never()).saveAndFlush(any(Owner.class));
	}

	@Test
	@DisplayName("CFG-C-Q6 [1,3,4,5,6,12] future birth date only -> form with exactly one birthDate error")
	void cfgC_q6_futureDateOnly() throws Exception {
		this.mockMvc.perform(createPet("Betty", tomorrow()))
			.andExpect(status().isOk())
			.andExpect(view().name(FORM_VIEW))
			.andExpect(model().attributeErrorCount("pet", 1))
			.andExpect(model().attributeHasFieldErrorCode("pet", "birthDate", "typeMismatch.birthDate"));

		verify(this.owners, never()).saveAndFlush(any(Owner.class));
	}

	@Test
	@DisplayName("CFG-C-Q7 [1,3,5,6,12] empty name -> form with name/required from PetValidator")
	void cfgC_q7_validatorErrorOnly() throws Exception {
		this.mockMvc.perform(createPet("", pastDate()))
			.andExpect(status().isOk())
			.andExpect(view().name(FORM_VIEW))
			.andExpect(model().attributeErrorCount("pet", 1))
			.andExpect(model().attributeHasFieldErrorCode("pet", "name", "required"));

		verify(this.owners, never()).saveAndFlush(any(Owner.class));
	}

	@Test
	@DisplayName("CFG-C-Q8 [1,3,5,7,11,12] birth date today (boundary of node 3) -> saved, redirect")
	void cfgC_q8_birthDateToday() throws Exception {
		this.mockMvc.perform(createPet("Betty", LocalDate.now()))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name(REDIRECT_VIEW));

		verify(this.owners, times(1)).saveAndFlush(this.owner);
	}

	@Test
	@DisplayName("CFG-C-Q9 [1,3,5,6,12] no birth date (clause 1 of node 3 false) -> form with birthDate/required")
	void cfgC_q9_noBirthDate() throws Exception {
		this.mockMvc
			.perform(post("/owners/{ownerId}/pets/new", OWNER_ID).param("name", "Betty").param("type", "hamster"))
			.andExpect(status().isOk())
			.andExpect(view().name(FORM_VIEW))
			.andExpect(model().attributeErrorCount("pet", 1))
			.andExpect(model().attributeHasFieldErrorCode("pet", "birthDate", "required"));

		verify(this.owners, never()).saveAndFlush(any(Owner.class));
	}

	@Test
	@DisplayName("CFG-C-Q10 [1,3,5,7,11,12] saved pet named \"petty\" (clause 2 of node 1 false), direct call -> saved")
	void cfgC_q10_savedPetSkipsDuplicateCheck() {
		// Not reachable over HTTP: the binder disallows "id", so the form always binds a
		// new pet. The method is called directly to make pet.isNew() false.
		PetController controller = new PetController(this.owners, this.types);
		Pet savedPetty = new Pet();
		savedPetty.setId(7);
		savedPetty.setName("petty");
		savedPetty.setBirthDate(pastDate());
		BindingResult result = new BeanPropertyBindingResult(savedPetty, "pet");
		RedirectAttributesModelMap redirect = new RedirectAttributesModelMap();

		String viewName = controller.processCreationForm(this.owner, savedPetty, result, redirect);

		assertThat(viewName).isEqualTo(REDIRECT_VIEW);
		assertThat(result.hasErrors()).isFalse();
		assertThat(redirect.getFlashAttributes().get("message")).isEqualTo("New Pet has been Added");
		verify(this.owners, times(1)).saveAndFlush(this.owner);
	}

}
