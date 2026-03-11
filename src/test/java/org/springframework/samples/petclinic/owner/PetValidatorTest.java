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

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

class PetValidatorTest {

	private PetValidator validator;

	private Pet pet;

	@BeforeEach
	void setUp() {
		validator = new PetValidator();
		pet = new Pet();
	}

	@Test
	void testValidPet() {
		pet.setName("Buddy");
		pet.setBirthDate(LocalDate.of(2020, 1, 15));
		PetType type = new PetType();
		type.setName("Dog");
		pet.setType(type);
		pet.setId(1); // Mark as not new

		Errors errors = new BeanPropertyBindingResult(pet, "pet");
		validator.validate(pet, errors);

		assertFalse(errors.hasErrors());
	}

	@Test
	void testPetWithoutName() {
		pet.setName(null);
		pet.setBirthDate(LocalDate.of(2020, 1, 15));
		PetType type = new PetType();
		type.setName("Dog");
		pet.setType(type);

		Errors errors = new BeanPropertyBindingResult(pet, "pet");
		validator.validate(pet, errors);

		assertTrue(errors.hasErrors());
		assertTrue(errors.hasFieldErrors("name"));
	}

	@Test
	void testPetWithEmptyName() {
		pet.setName("");
		pet.setBirthDate(LocalDate.of(2020, 1, 15));
		PetType type = new PetType();
		pet.setType(type);

		Errors errors = new BeanPropertyBindingResult(pet, "pet");
		validator.validate(pet, errors);

		assertTrue(errors.hasErrors());
		assertTrue(errors.hasFieldErrors("name"));
	}

	@Test
	void testNewPetWithoutType() {
		pet.setName("Buddy");
		pet.setBirthDate(LocalDate.of(2020, 1, 15));
		pet.setType(null);

		Errors errors = new BeanPropertyBindingResult(pet, "pet");
		validator.validate(pet, errors);

		assertTrue(errors.hasErrors());
		assertTrue(errors.hasFieldErrors("type"));
	}

	@Test
	void testExistingPetWithoutType() {
		pet.setName("Buddy");
		pet.setBirthDate(LocalDate.of(2020, 1, 15));
		pet.setType(null);
		pet.setId(1); // Mark as existing

		Errors errors = new BeanPropertyBindingResult(pet, "pet");
		validator.validate(pet, errors);

		// Existing pet doesn't need type validation
		assertFalse(errors.hasFieldErrors("type"));
	}

	@Test
	void testPetWithoutBirthDate() {
		pet.setName("Buddy");
		pet.setBirthDate(null);
		PetType type = new PetType();
		type.setName("Dog");
		pet.setType(type);

		Errors errors = new BeanPropertyBindingResult(pet, "pet");
		validator.validate(pet, errors);

		assertTrue(errors.hasErrors());
		assertTrue(errors.hasFieldErrors("birthDate"));
	}

	@Test
	void testValidatorSupportsClass() {
		assertTrue(validator.supports(Pet.class));
		assertFalse(validator.supports(Owner.class));
		assertFalse(validator.supports(String.class));
	}

	@Test
	void testPetWithNameAndTypeButNoBirthDate() {
		pet.setName("Max");
		pet.setBirthDate(null);
		PetType type = new PetType();
		type.setName("Cat");
		pet.setType(type);

		Errors errors = new BeanPropertyBindingResult(pet, "pet");
		validator.validate(pet, errors);

		assertTrue(errors.hasErrors());
		assertTrue(errors.hasFieldErrors("birthDate"));
		assertFalse(errors.hasFieldErrors("name"));
		assertFalse(errors.hasFieldErrors("type"));
	}

	@Test
	void testPetWithWhitespaceName() {
		pet.setName("   ");
		pet.setBirthDate(LocalDate.of(2020, 1, 15));
		PetType type = new PetType();
		pet.setType(type);

		Errors errors = new BeanPropertyBindingResult(pet, "pet");
		validator.validate(pet, errors);

		assertTrue(errors.hasErrors());
		assertTrue(errors.hasFieldErrors("name"));
	}

	@Test
	void testNewPetWithAllRequiredFields() {
		pet.setName("Fluffy");
		pet.setBirthDate(LocalDate.of(2021, 6, 10));
		PetType type = new PetType();
		type.setName("Rabbit");
		pet.setType(type);

		Errors errors = new BeanPropertyBindingResult(pet, "pet");
		validator.validate(pet, errors);

		assertFalse(errors.hasErrors());
	}

	@Test
	void testPetWithValidNameAndType() {
		pet.setName("Rover");
		pet.setBirthDate(LocalDate.of(2019, 3, 5));
		PetType type = new PetType();
		type.setName("Dog");
		pet.setType(type);
		pet.setId(10);

		Errors errors = new BeanPropertyBindingResult(pet, "pet");
		validator.validate(pet, errors);

		assertFalse(errors.hasErrors());
	}

	@Test
	void testPetErrorCount() {
		pet.setName(null);
		pet.setBirthDate(null);
		pet.setType(null);

		Errors errors = new BeanPropertyBindingResult(pet, "pet");
		validator.validate(pet, errors);

		assertTrue(errors.hasErrors());
		int errorCount = errors.getFieldErrorCount();
		assertTrue(errorCount >= 2); // At least name and birthDate
	}

}
