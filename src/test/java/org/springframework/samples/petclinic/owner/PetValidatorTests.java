/*
 * Copyright 2012-2024 the original author or authors.
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

import java.time.LocalDate;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for {@link PetValidator}
 *
 * @author Wick Dynex
 */
@ExtendWith(MockitoExtension.class)
@DisabledInNativeImage
public class PetValidatorTests {

	private PetValidator petValidator;

	private Pet pet;

	private PetType petType;

	private Errors errors;

	private static final String PET_NAME = "Buddy";

	private static final String PET_TYPE_NAME = "Dog";

	private static final LocalDate PET_BIRTH_DATE = LocalDate.of(1990, 1, 1);

	@BeforeEach
	void setUp() {
		petValidator = new PetValidator();
		pet = new Pet();
		petType = new PetType();
		errors = new MapBindingResult(new HashMap<>(), "pet");
	}

	@Test
	void testValidate() {
		petType.setName(PET_TYPE_NAME);
		pet.setName(PET_NAME);
		pet.setType(petType);
		pet.setBirthDate(PET_BIRTH_DATE);

		petValidator.validate(pet, errors);

		assertFalse(errors.hasErrors());
	}

	@Nested
	class ValidateHasErrors {

		@Test
		void testValidateWithInvalidPetName() {
			petType.setName(PET_TYPE_NAME);
			pet.setName("");
			pet.setType(petType);
			pet.setBirthDate(PET_BIRTH_DATE);

			petValidator.validate(pet, errors);

			assertTrue(errors.hasFieldErrors("name"));
		}

		@Test
		void testValidateWithInvalidPetType() {
			pet.setName(PET_NAME);
			pet.setType(null);
			pet.setBirthDate(PET_BIRTH_DATE);

			petValidator.validate(pet, errors);

			assertTrue(errors.hasFieldErrors("type"));
		}

		@Test
		void testValidateWithInvalidBirthDate() {
			petType.setName(PET_TYPE_NAME);
			pet.setName(PET_NAME);
			pet.setType(petType);
			pet.setBirthDate(null);

			petValidator.validate(pet, errors);

			assertTrue(errors.hasFieldErrors("birthDate"));
		}

	}

}