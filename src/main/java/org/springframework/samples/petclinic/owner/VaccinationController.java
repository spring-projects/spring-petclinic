package org.springframework.samples.petclinic.owner;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

/**
 * Controller for managing pet vaccinations.
 */
@Controller
class VaccinationController {

	private final OwnerRepository owners;

	public VaccinationController(OwnerRepository owners) {
		this.owners = owners;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id", "*.id");
	}

	/**
	 * Loads the owner and pet and creates a new vaccination for the form.
	 * @param ownerId the identifier of the owner
	 * @param petId the identifier of the pet
	 * @param model the model used by the view
	 * @return a new Vaccination instance
	 */
	@ModelAttribute("vaccination")
	public Vaccination loadPetWithVaccination(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId,
			Map<String, Object> model) {

		Optional<Owner> optionalOwner = owners.findById(ownerId);
		Owner owner = optionalOwner.orElseThrow(() -> new IllegalArgumentException(
				"Owner not found with id: " + ownerId + ". Please ensure the ID is correct "));

		Pet pet = owner.getPet(petId);
		if (pet == null) {
			throw new IllegalArgumentException(
					"Pet with id " + petId + " not found for owner with id " + ownerId + ".");
		}

		model.put("pet", pet);
		model.put("owner", owner);

		return new Vaccination();
	}

	@ModelAttribute("minVaccinationDate")
	public LocalDate minVaccinationDate() {
		return LocalDate.now();
	}

	@GetMapping("/owners/{ownerId}/pets/{petId}/vaccinations/new")
	public String initNewVaccinationForm() {
		return "pets/createOrUpdateVaccinationForm";
	}

	@PostMapping("/owners/{ownerId}/pets/{petId}/vaccinations/new")
	public String processNewVaccinationForm(@ModelAttribute Owner owner, @PathVariable int petId,
			@Valid Vaccination vaccination, BindingResult result, RedirectAttributes redirectAttributes) {

		if (vaccination.getVaccinationDate() != null && vaccination.getNextDueDate() != null
				&& vaccination.getNextDueDate().isBefore(vaccination.getVaccinationDate())) {
			result.rejectValue("nextDueDate", "typeMismatch.nextDueDate");
		}

		if (result.hasErrors()) {
			return "pets/createOrUpdateVaccinationForm";
		}

		owner.addVaccination(petId, vaccination);
		this.owners.save(owner);

		redirectAttributes.addFlashAttribute("message", "Vaccination has been added");
		return "redirect:/owners/{ownerId}";
	}

}
