/*
 * Copyright 2024 the original author or authors.
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("PetValidator Tests")
class PetValidatorTest {

	@InjectMocks
	private PetValidator petValidator;

	private Pet pet;

	private Errors errors;

	@BeforeEach
	void setUp() {
		pet = new Pet();
		errors = new BeanPropertyBindingResult(pet, "pet");
	}

	@Test
	@DisplayName("Validate Pet with missing fields")
	void testValidatePetWithMissingFields() {
		petValidator.validate(pet, errors);

		assertThat(errors.getFieldError("name").getCode()).isEqualTo("required");
		assertThat(errors.getFieldError("type").getCode()).isEqualTo("required");
		assertThat(errors.getFieldError("birthDate").getCode()).isEqualTo("required");
	}

}
