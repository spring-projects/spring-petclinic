package org.springframework.samples.petclinic.owner;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

class PetValidatorUnitTest {

	private PetValidator validator;

	private Pet validPet;

	@BeforeEach
	void setUp() {
		validator = new PetValidator();

		PetType dog = new PetType();
		dog.setId(1);
		dog.setName("dog");

		validPet = new Pet();
		validPet.setName("Rex");
		validPet.setType(dog);
		validPet.setBirthDate(LocalDate.of(2022, 5, 10));
	}

	@Test
	void supports_PetAndOtherClasses_ReturnsExpectedBoolean() {
		assertTrue(validator.supports(Pet.class));
		assertFalse(validator.supports(Owner.class));
	}

	@Test
	void validate_WhenAllFieldsAreValid_ProducesNoErrors() {
		Errors errors = new BeanPropertyBindingResult(validPet, "pet");

		validator.validate(validPet, errors);

		assertFalse(errors.hasErrors());
	}

	@Test
	void validate_WhenNameIsNullOrBlank_RejectsNameField() {
		validPet.setName(null);
		Errors nullErrors = new BeanPropertyBindingResult(validPet, "pet");
		validator.validate(validPet, nullErrors);
		assertTrue(nullErrors.hasFieldErrors("name"));

		validPet.setName("   ");
		Errors blankErrors = new BeanPropertyBindingResult(validPet, "pet");
		validator.validate(validPet, blankErrors);
		assertTrue(blankErrors.hasFieldErrors("name"));
	}

	@Test
	void validate_WhenNewPetHasNullType_RejectsTypeField() {
		validPet.setId(null);
		validPet.setType(null);
		Errors errors = new BeanPropertyBindingResult(validPet, "pet");

		validator.validate(validPet, errors);

		assertTrue(errors.hasFieldErrors("type"));
	}

	@Test
	void validate_WhenExistingPetHasNullType_DoesNotRejectTypeField() {
		validPet.setId(7);
		validPet.setType(null);
		Errors errors = new BeanPropertyBindingResult(validPet, "pet");

		validator.validate(validPet, errors);

		assertFalse(errors.hasFieldErrors("type"));
	}

	@Test
	void validate_WhenBirthDateIsNull_RejectsBirthDateField() {
		validPet.setBirthDate(null);
		Errors errors = new BeanPropertyBindingResult(validPet, "pet");

		validator.validate(validPet, errors);

		assertTrue(errors.hasFieldErrors("birthDate"));
	}

	@Test
	void validate_WhenNameHasExactly30Characters_DoesNotRejectNameField() {
		// Arrange
		validPet.setName("A".repeat(30));
		Errors errors = new BeanPropertyBindingResult(validPet, "pet");

		// Act
		validator.validate(validPet, errors);

		// Assert
		assertFalse(errors.hasErrors());
	}

}