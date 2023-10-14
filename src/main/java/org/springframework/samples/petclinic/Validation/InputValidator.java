package org.springframework.samples.petclinic.Validation;

import jakarta.validation.constraints.NotNull;
import org.springframework.samples.petclinic.Pet.Pet;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Visit;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

@Component
public class InputValidator {

	public void validatePetDuplication(BindingResult result, Pet pet, Owner owner) {

		requireNonNull(pet, "pet is null");
		requireNonNull(pet, "owner is null");

		boolean ownerHasExistingPet = owner.getPet(pet.getName(), true) != null;
		if (StringUtils.hasText(pet.getName()) && pet.isNewEntry() && ownerHasExistingPet) {
			result.rejectValue("name", "duplicate");
		}
	}

	public void validatePetUpdateDuplication(BindingResult result, Pet pet, Owner owner) {

		requireNonNull(pet, "pet is null");
		requireNonNull(pet, "owner is null");

		var ownerHasExistingPet = owner.getPet(pet.getName(), false);
		var notSameId = !Objects.equals(ownerHasExistingPet.getId(), pet.getId());
		if (StringUtils.hasText(pet.getName()) && ownerHasExistingPet != null && notSameId) {
			result.rejectValue("name", "duplicate");
		}
	}

	public <T> void validateDate(BindingResult result, @NotNull T obj, String fieldName) {
		requireNonNull(obj, "pet is null");

		// Refactor in case of changing to higher Java versions > 17 to use switch pattern
		// matching
		LocalDate date = null;

		if (obj instanceof Pet pet) {
			date = pet.getBirthDate();
		}
		else if (obj instanceof Visit visit) {
			date = visit.getDate();
		}

		if (date == null) {
			result.rejectValue(fieldName, "typeMismatch.birthDate");
		}
		else if (date.isAfter(LocalDate.now())) {
			result.rejectValue(fieldName, "typeMismatch.birthDate.future");
		}
	}

	public void validateString(BindingResult result, @NotNull Visit visit, String fieldName) {
		if (!StringUtils.hasText(visit.getDescription())) {
			result.rejectValue(fieldName, "required");
		}
	}

}
