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

import java.time.LocalDate;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

/**
 * Service responsible for Pet validation logic. Extracts complex validation rules from
 * the controller layer. Follows Single Responsibility Principle (SRP) from SOLID.
 *
 * @author Nathan Dalbert
 * @author Paulo Henrique
 * @author Mickael de Albuquerque
 * @author Igor Rego
 */
@Service
public class PetValidationService {

	/**
	 * Validates pet name for duplicates during creation.
	 * @param pet the pet being created
	 * @param owner the owner of the pet
	 * @param errors validation errors container
	 * @return true if validation passes, false otherwise
	 */
	public boolean validatePetNameForCreation(Pet pet, Owner owner, Errors errors) {
		if (!StringUtils.hasText(pet.getName())) {
			return true; // Let @Valid handle blank names
		}

		if (pet.isNew() && owner.getPet(pet.getName(), true) != null) {
			errors.rejectValue("name", "duplicate", "already exists");
			return false;
		}

		return true;
	}

	/**
	 * Validates pet name for duplicates during update.
	 * @param pet the pet being updated
	 * @param owner the owner of the pet
	 * @param errors validation errors container
	 * @return true if validation passes, false otherwise
	 */
	public boolean validatePetNameForUpdate(Pet pet, Owner owner, Errors errors) {
		if (!StringUtils.hasText(pet.getName())) {
			return true; // Let @Valid handle blank names
		}

		Pet existingPet = owner.getPet(pet.getName(), false);
		if (existingPet != null && !Objects.equals(existingPet.getId(), pet.getId())) {
			errors.rejectValue("name", "duplicate", "already exists");
			return false;
		}

		return true;
	}

	/**
	 * Validates that birth date is not in the future.
	 * @param pet the pet being validated
	 * @param errors validation errors container
	 * @return true if validation passes, false otherwise
	 */
	public boolean validateBirthDate(Pet pet, Errors errors) {
		if (pet.getBirthDate() == null) {
			return true;
		}

		LocalDate currentDate = LocalDate.now();
		if (pet.getBirthDate().isAfter(currentDate)) {
			errors.rejectValue("birthDate", "typeMismatch.birthDate");
			return false;
		}

		return true;
	}

	/**
	 * Performs all validations for pet creation.
	 * @param pet the pet being created
	 * @param owner the owner of the pet
	 * @param errors validation errors container
	 * @return true if all validations pass, false otherwise
	 */
	public boolean validateForCreation(Pet pet, Owner owner, Errors errors) {
		boolean isNameValid = validatePetNameForCreation(pet, owner, errors);
		boolean isBirthDateValid = validateBirthDate(pet, errors);
		return isNameValid && isBirthDateValid;
	}

	/**
	 * Performs all validations for pet update.
	 * @param pet the pet being updated
	 * @param owner the owner of the pet
	 * @param errors validation errors container
	 * @return true if all validations pass, false otherwise
	 */
	public boolean validateForUpdate(Pet pet, Owner owner, Errors errors) {
		boolean isNameValid = validatePetNameForUpdate(pet, owner, errors);
		boolean isBirthDateValid = validateBirthDate(pet, errors);
		return isNameValid && isBirthDateValid;
	}

}
