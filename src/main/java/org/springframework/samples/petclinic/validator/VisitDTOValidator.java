package org.springframework.samples.petclinic.validator;

import org.springframework.samples.petclinic.common.CommonAttribute;
import org.springframework.samples.petclinic.common.CommonError;
import org.springframework.samples.petclinic.dto.business.VisitDTO;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * <code>Validator</code> for <code>VisitDTO</code> forms.
 * <p>
 * We're not using Bean Validation annotations here because it is easier to define such
 * validation rule in Java.
 * </p>
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
public class VisitDTOValidator implements Validator {

	@Override
	public boolean supports(Class<?> aClass) {
		return VisitDTO.class.isAssignableFrom(aClass);
	}

	@Override
	public void validate(Object object, Errors errors) {
		VisitDTO visit = (VisitDTO) object;

		// Pet ID validation
		if (visit.getPetId() == null) {
			errors.rejectValue(CommonAttribute.VISIT_PET_ID, CommonError.REQUIRED_ARGS, CommonError.REQUIRED_MESSAGE);
		}

		// Visit date validation
		if (visit.getDate() == null) {
			errors.rejectValue(CommonAttribute.VISIT_DATE, CommonError.REQUIRED_ARGS, CommonError.REQUIRED_MESSAGE);
		}

		// Visit description validation
		if (visit.getDescription() == null || visit.getDescription().isEmpty()) {
			errors.rejectValue(CommonAttribute.VISIT_DESCRIPTION, CommonError.REQUIRED_ARGS,
					CommonError.REQUIRED_MESSAGE);
		}

	}

}
