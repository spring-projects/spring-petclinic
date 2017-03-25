package org.springframework.samples.petclinic.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author Oğuz Çağıran
 */
public class PetValidatorTest {

	private Validator validator;
	
	@Before
	public void setup() {
		validator = new PetValidator();
	}
	
	@Test
	public void shouldNotValidateWhenPetNameEmpty() {
		Pet pet = new Pet();
		pet.setName("");
		Errors errors = new BeanPropertyBindingResult(pet, "name");
		validator.validate(pet, errors);
	    assertTrue(errors.hasErrors()); 
	}
	
	@Test
	public void shouldNotValideWhenBirthDateEmpty() {
		Pet pet = new Pet();
		pet.setName("pet");
		pet.setBirthDate(null);
		Errors errors = new BeanPropertyBindingResult(pet, "birthDate");
		validator.validate(pet, errors);
	    assertTrue(errors.hasErrors()); 
	}
}
