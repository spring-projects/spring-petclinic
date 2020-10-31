package org.springframework.samples.petclinic.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.common.CommonAttribute;
import org.springframework.samples.petclinic.common.CommonError;
import org.springframework.samples.petclinic.dto.PetDTO;
import org.springframework.samples.petclinic.dto.PetTypeDTO;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link PetDTOValidator}
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
class PetDTOValidatorTest {

	private final static Integer PET_ID = 14;

	private final static String PET_NAME = "bowser";

	private final static String PET_BIRTH_DATE = "2020-07-11";

	private final static String PET_TYPE_NAME = "dinausaur";

	private final static Integer PET_TYPE_ID = 11;

	private final PetDTOValidator petDTOValidator = new PetDTOValidator();

	private PetDTO petDTO;

	@BeforeEach
	void beforeEach() {
		PetTypeDTO petTypeDTO = new PetTypeDTO();
		petTypeDTO.setId(PET_TYPE_ID);
		petTypeDTO.setName(PET_TYPE_NAME);
		petDTO = new PetDTO();
		petDTO.setId(PET_ID);
		petDTO.setName(PET_NAME);
		petDTO.setType(petTypeDTO);
		petDTO.setBirthDate(LocalDate.parse(PET_BIRTH_DATE));
	}

	@Test
	@Tag("validate")
	@DisplayName("Verify that a Pet with good arguments is validate")
	void givenRightPetDTO_whenValidate_thenValidate() {
		Errors errors = new BeanPropertyBindingResult(petDTO, CommonAttribute.PET);

		petDTOValidator.validate(petDTO, errors);
		assertThat(errors.getErrorCount()).isZero();
	}

	@Test
	@Tag("validate")
	@DisplayName("Verify that a Pet without name is not validate")
	void givenPetDTOWithoutName_whenValidate_thenRejectWithError() {
		Errors errors = new BeanPropertyBindingResult(petDTO, CommonAttribute.PET);

		petDTO.setName(null);
		petDTOValidator.validate(petDTO, errors);
		assertThat(errors.getErrorCount()).isEqualTo(1);
		FieldError fieldError = errors.getFieldError(CommonAttribute.PET_NAME);

		assertThat(fieldError.getCode()).isEqualTo(CommonError.REQUIRED_ARGS);
		assertThat(fieldError.getObjectName()).isEqualTo(CommonAttribute.PET);
		assertThat(fieldError.getDefaultMessage()).isEqualTo(CommonError.REQUIRED_MESSAGE);
	}

	@Test
	@Tag("validate")
	@DisplayName("Verify that a Pet without name is not validate")
	void givenNewPetDTOWithoutType_whenValidate_thenRejectWithError() {
		Errors errors = new BeanPropertyBindingResult(petDTO, CommonAttribute.PET);

		petDTO.setType(null);
		petDTO.setId(null);
		petDTOValidator.validate(petDTO, errors);
		assertThat(errors.getErrorCount()).isEqualTo(1);

		FieldError fieldError = errors.getFieldError(CommonAttribute.PET_TYPE);
		assertThat(fieldError.getCode()).isEqualTo(CommonError.REQUIRED_ARGS);
		assertThat(fieldError.getObjectName()).isEqualTo(CommonAttribute.PET);
		assertThat(fieldError.getDefaultMessage()).isEqualTo(CommonError.REQUIRED_MESSAGE);
	}

	@Test
	@Tag("validate")
	@DisplayName("Verify that a Pet without birthDate is not validate")
	void givenNewPetDTOWithoutBirthDateType_whenValidate_thenRejectWithError() {
		Errors errors = new BeanPropertyBindingResult(petDTO, CommonAttribute.PET);

		petDTO.setBirthDate(null);
		petDTOValidator.validate(petDTO, errors);
		assertThat(errors.getErrorCount()).isEqualTo(1);

		FieldError fieldError = errors.getFieldError(CommonAttribute.PET_BIRTH_DATE);
		assertThat(fieldError.getCode()).isEqualTo(CommonError.REQUIRED_ARGS);
		assertThat(fieldError.getObjectName()).isEqualTo(CommonAttribute.PET);
		assertThat(fieldError.getDefaultMessage()).isEqualTo(CommonError.REQUIRED_MESSAGE);
	}

}
