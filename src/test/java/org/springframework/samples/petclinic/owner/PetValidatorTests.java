package org.springframework.samples.petclinic.owner;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class PetValidatorTests {

	@Test
	public void testValidationWithValidName() {
		//given
		PetValidator petValidator = new PetValidator();
		Pet validPet = new Pet();
		validPet.setName("Peter");
		Errors errors = new BeanPropertyBindingResult(validPet, "");

		//when
		petValidator.validate(validPet, errors);

		//then
		assertNull(errors.getFieldError("name"));
	}

	@Test
	public void testValidationWithInvalidName() {
		//given
		PetValidator petValidator = new PetValidator();
		Pet invalidPet = new Pet();
		invalidPet.setName("");
		Errors errors = new BeanPropertyBindingResult(invalidPet, "");

		//when
		petValidator.validate(invalidPet, errors);

		//then
		assertTrue(errors.hasErrors());
	}

	@Test
	public void testValidationWithValidType() {
		//given
		PetValidator petValidator = new PetValidator();
		Pet validPet = new Pet();
		PetType tiger = new PetType();
		tiger.setId(24);
		validPet.setType(tiger);
		Errors errors = new BeanPropertyBindingResult(validPet, "");

		//when
		petValidator.validate(validPet, errors);

		//then
		assertNull(errors.getFieldError("type"));
	}

	@Test
	public void testValidationWithInvalidType() {
		//given
		PetValidator petValidator = new PetValidator();
		Pet invalidPet = new Pet();
		PetType emptyType = null;
		invalidPet.setType(emptyType);
		Errors errors = new BeanPropertyBindingResult(invalidPet, "");

		//when
		petValidator.validate(invalidPet, errors);

		//then
		assertTrue(errors.hasErrors());
	}

	@Test
	public void testValidationWithValidBirthDate() {
		//given
		PetValidator petValidator = new PetValidator();
		Pet validPet = new Pet();
		LocalDateTime timePoint = LocalDateTime.now();
		LocalDate localDate = timePoint.toLocalDate();
		Date birthDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		validPet.setBirthDate(birthDate);
		Errors errors = new BeanPropertyBindingResult(validPet, "");

		//when
		petValidator.validate(validPet, errors);

		//then
		assertNull(errors.getFieldError("birthDate"));
	}

	@Test
	public void testValidationWithInvalidBirthDate() {
		//given
		PetValidator petValidator = new PetValidator();
		Pet invalidPet = new Pet();
		Date birthDate = null;
		invalidPet.setBirthDate(birthDate);
		Errors errors = new BeanPropertyBindingResult(invalidPet, "");

		//when
		petValidator.validate(invalidPet, errors);

		//then
		assertTrue(errors.hasErrors());
	}
}

