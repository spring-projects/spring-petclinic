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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.HashMap;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

/**
 * SWE 472 course project - anomalies found by the test design (Table XX of the report).
 * <p>
 * Every test asserts the behaviour a user would expect and therefore FAILS on the current
 * code. The class is disabled so that the build stays green. To demonstrate the failures,
 * run it with the disabled condition switched off:
 *
 * <pre>
 * mvnw test -Dtest=Swe472AnomalyDemoTests \
 *   -Djunit.jupiter.conditions.deactivate=org.junit.*DisabledCondition
 * </pre>
 *
 * The integration tests run against the embedded H2 database with its seed data: owner 1
 * (George Franklin) owns the saved pet "Leo" (id 1, type cat). Each test runs in a
 * transaction that is rolled back.
 *
 * @author Yisak Demelash
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisabledInNativeImage
@DisabledInAotMode
@Disabled("SWE472 anomaly demonstration: these tests fail on purpose, see Table XX")
@DisplayName("SWE472 Anomalies - expected behaviour that the code does not have")
class Swe472AnomalyDemoTests {

	@Autowired
	private MockMvc mockMvc;

	private static Pet pet(String name, Integer id) {
		Pet pet = new Pet();
		pet.setName(name);
		pet.setId(id);
		return pet;
	}

	@Test
	@DisplayName("ANOM-1a getPet(\" Max \", false) should find the saved pet \"Max\"")
	void anom1a_getPetIgnoresSurroundingSpaces() {
		Owner owner = new Owner();
		Pet max = pet("Max", 1);
		owner.addPet(max);

		assertThat(owner.getPet(" Max ", false)).as("pet found for \" Max \"").isSameAs(max);
	}

	@Test
	@DisplayName("ANOM-1b POST new pet \" Leo \" for owner 1 (who has Leo) should be rejected as duplicate")
	void anom1b_spacedDuplicateNameIsRejected() throws Exception {
		this.mockMvc
			.perform(post("/owners/{ownerId}/pets/new", 1).param("name", " Leo ")
				.param("type", "cat")
				.param("birthDate", "2020-01-01"))
			.andExpect(status().isOk())
			.andExpect(view().name("pets/createOrUpdatePetForm"))
			.andExpect(model().attributeHasFieldErrorCode("pet", "name", "duplicate"));
	}

	@Test
	@DisplayName("ANOM-2a validating a saved pet without a type should report type/required")
	void anom2a_savedPetWithoutTypeIsRejected() {
		Pet saved = pet("Leo", 1);
		saved.setBirthDate(LocalDate.of(2010, 9, 7));
		Errors errors = new MapBindingResult(new HashMap<>(), "pet");

		new PetValidator().validate(saved, errors);

		assertThat(errors.getFieldError("type")).as("error on field type").isNotNull();
	}

	@Test
	@DisplayName("ANOM-2b POST edit of Leo with an empty type should return the form, not fail with an exception")
	void anom2b_updateWithEmptyTypeReturnsForm() throws Exception {
		this.mockMvc
			.perform(post("/owners/{ownerId}/pets/{petId}/edit", 1, 1).param("name", "Leo")
				.param("type", "")
				.param("birthDate", "2010-09-07"))
			.andExpect(status().isOk())
			.andExpect(view().name("pets/createOrUpdatePetForm"))
			.andExpect(model().attributeHasFieldErrorCode("pet", "type", "required"));
	}

}
