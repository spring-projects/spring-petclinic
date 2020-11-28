package org.springframework.samples.petclinic.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.common.CommonAttribute;
import org.springframework.samples.petclinic.common.CommonError;
import org.springframework.samples.petclinic.dto.business.VisitDTO;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link VisitDTOValidator}
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
class VisitDTOValidatorTest {

	private final static Integer VISIT_PET_ID = 14;

	private final static String VISIT_DESCRIPTION = "Annual visit for new pet";

	private final VisitDTOValidator visitDTOValidator = new VisitDTOValidator();

	private VisitDTO visitDTO;

	@BeforeEach
	void beforeEach() {
		visitDTO = new VisitDTO();
		visitDTO.setPetId(VISIT_PET_ID);
		visitDTO.setDate(LocalDate.now());
		visitDTO.setDescription(VISIT_DESCRIPTION);
	}

	@Test
	@Tag("validate")
	@DisplayName("Verify that a Visit with good arguments is validate")
	void givenRightVisitDTO_whenValidate_thenValidate() {
		Errors errors = new BeanPropertyBindingResult(visitDTO, CommonAttribute.VISIT);

		visitDTOValidator.validate(visitDTO, errors);
		assertThat(errors.getErrorCount()).isZero();
	}

	@Test
	@Tag("validate")
	@DisplayName("Verify that a Visit without Pet ID is not validate")
	void givenVisitDTOWithoutPetID_whenValidate_thenRejectWithError() {
		Errors errors = new BeanPropertyBindingResult(visitDTO, CommonAttribute.VISIT);

		visitDTO.setPetId(null);
		visitDTOValidator.validate(visitDTO, errors);
		assertThat(errors.getErrorCount()).isEqualTo(1);
		FieldError fieldError = errors.getFieldError(CommonAttribute.VISIT_PET_ID);

		assertThat(fieldError.getCode()).isEqualTo(CommonError.REQUIRED_ARGS);
		assertThat(fieldError.getObjectName()).isEqualTo(CommonAttribute.VISIT);
		assertThat(fieldError.getDefaultMessage()).isEqualTo(CommonError.REQUIRED_MESSAGE);
	}

	@Test
	@Tag("validate")
	@DisplayName("Verify that a Visit without date is not validate")
	void givenVisitDTOWithoutDate_whenValidate_thenRejectWithError() {
		Errors errors = new BeanPropertyBindingResult(visitDTO, CommonAttribute.VISIT);

		visitDTO.setDate(null);
		visitDTOValidator.validate(visitDTO, errors);
		assertThat(errors.getErrorCount()).isEqualTo(1);
		FieldError fieldError = errors.getFieldError(CommonAttribute.VISIT_DATE);

		assertThat(fieldError.getCode()).isEqualTo(CommonError.REQUIRED_ARGS);
		assertThat(fieldError.getObjectName()).isEqualTo(CommonAttribute.VISIT);
		assertThat(fieldError.getDefaultMessage()).isEqualTo(CommonError.REQUIRED_MESSAGE);
	}

	@Test
	@Tag("validate")
	@DisplayName("Verify that a Visit without description is not validate")
	void givenVisitDTOWithoutDescription_whenValidate_thenRejectWithError() {
		Errors errors = new BeanPropertyBindingResult(visitDTO, CommonAttribute.VISIT);

		visitDTO.setDescription(null);
		visitDTOValidator.validate(visitDTO, errors);
		assertThat(errors.getErrorCount()).isEqualTo(1);
		FieldError fieldError = errors.getFieldError(CommonAttribute.VISIT_DESCRIPTION);

		assertThat(fieldError.getCode()).isEqualTo(CommonError.REQUIRED_ARGS);
		assertThat(fieldError.getObjectName()).isEqualTo(CommonAttribute.VISIT);
		assertThat(fieldError.getDefaultMessage()).isEqualTo(CommonError.REQUIRED_MESSAGE);
	}

}
