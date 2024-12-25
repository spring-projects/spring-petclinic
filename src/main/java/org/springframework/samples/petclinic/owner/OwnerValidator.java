package org.springframework.samples.petclinic.owner;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class OwnerValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Owner.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Owner owner = (Owner) target;

		// Ensure lastName is not empty
		if (owner.getTelephone() == null || owner.getTelephone().length() != 10) {
			errors.rejectValue("telephone", "required", "Valid telephone number is required.");
		}
	}

}
