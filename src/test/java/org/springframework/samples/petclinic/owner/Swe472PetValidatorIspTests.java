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

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;

/**
 * SWE 472 course project - Input Space Partitioning tests for
 * {@link PetValidator#validate(Object, Errors)}.
 * <p>
 * Test IDs ISP-V-TC01 to ISP-V-TC20 trace to Tables V of the project report. Base choice:
 * a new pet named "Buddy" of type dog, born 2020-01-01, validated into an empty error
 * collector.
 *
 * @author Amir Khalifa
 */
@DisplayName("SWE472 ISP - PetValidator.validate")
class Swe472PetValidatorIspTests {

	private static final LocalDate BASE_DATE = LocalDate.of(2020, 1, 1);

	private final PetValidator validator = new PetValidator();

	private static PetType dog() {
		PetType type = new PetType();
		type.setName("dog");
		return type;
	}

	private static Pet pet(String name, Integer id, PetType type, LocalDate birthDate) {
		Pet pet = new Pet();
		pet.setName(name);
		pet.setId(id);
		pet.setType(type);
		pet.setBirthDate(birthDate);
		return pet;
	}

	private static Errors emptyErrors() {
		return new MapBindingResult(new HashMap<>(), "pet");
	}

	/** Field and code of every error, e.g. "name/required". */
	private static List<String> fieldErrors(Errors errors) {
		return errors.getFieldErrors().stream().map(e -> e.getField() + "/" + e.getCode()).toList();
	}

	static Stream<Arguments> baseChoiceTests() {
		String x30 = "x".repeat(30);
		String x31 = "x".repeat(31);
		LocalDate today = LocalDate.now();
		return Stream.of(
				Arguments.of("ISP-V-TC01 a4 b2 c1 d2 e2 f1 (base) -> no errors", pet("Buddy", null, dog(), BASE_DATE),
						List.of()),
				Arguments.of("ISP-V-TC02 a1 b1 null name -> name/required", pet(null, null, dog(), BASE_DATE),
						List.of("name/required")),
				Arguments.of("ISP-V-TC03 a2 b1 empty name -> name/required", pet("", null, dog(), BASE_DATE),
						List.of("name/required")),
				Arguments.of("ISP-V-TC04 a3 whitespace-only name -> name/required", pet("   ", null, dog(), BASE_DATE),
						List.of("name/required")),
				Arguments.of("ISP-V-TC05 b3 name of 31 chars (boundary) -> name/size", pet(x31, null, dog(), BASE_DATE),
						List.of("name/size")),
				Arguments.of("ISP-V-TC06 c2 g3 saved pet (id 7) -> no errors", pet("Buddy", 7, dog(), BASE_DATE),
						List.of()),
				Arguments.of("ISP-V-TC07 d1 t1 type null -> type/required", pet("Buddy", null, null, BASE_DATE),
						List.of("type/required")),
				Arguments.of("ISP-V-TC08 e1 f0 birth date null -> birthDate/required", pet("Buddy", null, dog(), null),
						List.of("birthDate/required")),
				Arguments.of("ISP-V-TC09 f2 birth date today -> no errors", pet("Buddy", null, dog(), today),
						List.of()),
				Arguments.of("ISP-V-TC10 f3 birth date tomorrow -> no errors (rule is in controller)",
						pet("Buddy", null, dog(), today.plusDays(1)), List.of()),
				Arguments.of("ISP-V-TC11 b2 lower boundary (1 char) -> no errors", pet("x", null, dog(), BASE_DATE),
						List.of()),
				Arguments.of("ISP-V-TC12 b2 upper boundary (30 chars) -> no errors", pet(x30, null, dog(), BASE_DATE),
						List.of()),
				Arguments.of("ISP-V-TC13 c2 d1 saved pet without type -> no errors", pet("Buddy", 7, null, BASE_DATE),
						List.of()),
				Arguments.of("ISP-V-TC14 a1 d1 e1 all invalid -> three required errors", pet(null, null, null, null),
						List.of("name/required", "type/required", "birthDate/required")),
				Arguments.of("ISP-V-TC15 a3 b3 31 spaces -> name/required, not size",
						pet(" ".repeat(31), null, dog(), BASE_DATE), List.of("name/required")),
				Arguments.of("ISP-V-TC16 g2 id = 0 counts as saved -> no errors", pet("Buddy", 0, dog(), BASE_DATE),
						List.of()),
				Arguments.of("ISP-V-TC17 t3 type without a name -> no errors",
						pet("Buddy", null, new PetType(), BASE_DATE), List.of()));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("baseChoiceTests")
	@DisplayName("ISP-V-TC01..TC17 base choice, boundary and interaction tests")
	void validate(String id, Pet pet, List<String> expected) {
		Errors errors = emptyErrors();

		this.validator.validate(pet, errors);

		assertThat(errors.getErrorCount()).as("number of errors").isEqualTo(expected.size());
		assertThat(fieldErrors(errors)).as("field/code of errors").containsExactlyInAnyOrderElementsOf(expected);
	}

	@Test
	@DisplayName("ISP-V-TC18 k1 collector holds a global error, valid pet -> only the global error")
	void tc18_existingGlobalErrorIsKept() {
		Errors errors = emptyErrors();
		errors.reject("global");

		this.validator.validate(pet("Buddy", null, dog(), BASE_DATE), errors);

		assertThat(errors.getErrorCount()).isEqualTo(1);
		assertThat(errors.getGlobalError().getCode()).isEqualTo("global");
		assertThat(errors.getFieldErrors()).isEmpty();
	}

	@Test
	@DisplayName("ISP-V-TC19 k2 collector holds a name error, valid pet -> validator adds nothing")
	void tc19_existingNameErrorValidPet() {
		Errors errors = emptyErrors();
		errors.rejectValue("name", "earlier");

		this.validator.validate(pet("Buddy", null, dog(), BASE_DATE), errors);

		assertThat(fieldErrors(errors)).containsExactly("name/earlier");
	}

	@Test
	@DisplayName("ISP-V-TC20 k2 a1 collector holds a name error, null name -> both errors kept")
	void tc20_existingNameErrorInvalidName() {
		Errors errors = emptyErrors();
		errors.rejectValue("name", "earlier");

		this.validator.validate(pet(null, null, dog(), BASE_DATE), errors);

		assertThat(errors.getFieldErrorCount("name")).isEqualTo(2);
		assertThat(errors.getFieldErrors("name")).extracting(FieldError::getCode)
			.containsExactly("earlier", "required");
	}

}
