package org.springframework.samples.petclinic.owner.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.repository.PetRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PetIdExistsValidator implements Validator {

	private final PetRepository petRepository;

	@Autowired
	public PetIdExistsValidator(PetRepository petRepository) {
		this.petRepository = petRepository;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		// This validator supports Integer class, for validating petId field
		return Integer.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if (target == null) {
			errors.reject("petId.null", "Pet ID is required");
			return;
		}

		if (!(target instanceof Integer)) {
			errors.reject("petId.type", "Pet ID must be an integer");
			return;
		}

		Integer petId = (Integer) target;

		if (petId <= 0) {
			errors.rejectValue("", "petId.positive", "Pet ID must be a positive number");
			return;
		}

		if (!petRepository.existsById(petId)) {
			errors.rejectValue("", "petId.notFound", "Pet with ID " + petId + " does not exist");
		}
	}

}
