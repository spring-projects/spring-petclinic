package org.springframework.samples.petclinic.adapters.web.owner.validators;

import org.springframework.samples.petclinic.adapters.web.owner.dtos.PetDto;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PetValidator implements Validator {

	private static final String REQUIRED = "required";

	@Override
	public void validate(Object obj, Errors errors) {
		PetDto pet = (PetDto) obj;
		if (!StringUtils.hasText(pet.getName())) {
			errors.rejectValue("name", REQUIRED, REQUIRED);
		}
		if (pet.isNew() && pet.getType() == null) {
			errors.rejectValue("type", REQUIRED, REQUIRED);
		}
		if (pet.getBirthDate() == null) {
			errors.rejectValue("birthDate", REQUIRED, REQUIRED);
		}
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return PetDto.class.isAssignableFrom(clazz);
	}

}
