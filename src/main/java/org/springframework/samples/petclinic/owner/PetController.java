package org.springframework.samples.petclinic.owner;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.samples.petclinic.feature.FeatureToggle;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Wick Dynex
 */
@Controller
@RequestMapping("/owners/{ownerId}")
class PetController {

	private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";

	private final OwnerRepository owners;

	private final PetTypeRepository types;

	public PetController(OwnerRepository owners, PetTypeRepository types) {
		this.owners = owners;
		this.types = types;
	}

	/*
	 * ========================= MODEL ATTRIBUTES =========================
	 */

	@ModelAttribute("types")
	public Collection<PetType> populatePetTypes() {
		return this.types.findPetTypes();
	}

	@ModelAttribute("owner")
	public Owner findOwner(@PathVariable("ownerId") int ownerId) {
		return this.owners.findById(ownerId)
			.orElseThrow(() -> new IllegalArgumentException("Owner not found with id: " + ownerId));
	}

	@ModelAttribute("pet")
	public Pet findPet(@PathVariable("ownerId") int ownerId,
			@PathVariable(name = "petId", required = false) Integer petId) {

		if (petId == null) {
			return new Pet();
		}

		Owner owner = findOwner(ownerId);
		Pet pet = owner.getPet(petId);

		if (pet == null) {
			throw new IllegalArgumentException("Pet not found with id: " + petId);
		}

		return pet;
	}

	/*
	 * ========================= BINDERS =========================
	 */

	@InitBinder("owner")
	public void initOwnerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("pet")
	public void initPetBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new PetValidator());
	}

	/*
	 * ========================= CREATE PET =========================
	 */

	@GetMapping("/pets/new")
	public String initCreationForm(Owner owner, Model model) {
		Pet pet = new Pet();
		model.addAttribute("pet", pet);
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	@FeatureToggle("ADD_PET")
	@PostMapping("/pets/new")
	public String processCreationForm(@Valid Pet pet, BindingResult result, Owner owner, Model model) {

		if (result.hasErrors()) {
			model.addAttribute("pet", pet);
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}

		owner.addPet(pet);
		this.owners.save(owner);

		return "redirect:/owners/{ownerId}";
	}

	/*
	 * ========================= UPDATE PET =========================
	 */

	@GetMapping("/pets/{petId}/edit")
	public String initUpdateForm() {
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/pets/{petId}/edit")
	public String processUpdateForm(Owner owner, @Valid Pet pet, BindingResult result,
			RedirectAttributes redirectAttributes) {

		String petName = pet.getName();

		if (StringUtils.hasText(petName)) {
			Pet existingPet = owner.getPet(petName, false);
			if (existingPet != null && !Objects.equals(existingPet.getId(), pet.getId())) {
				result.rejectValue("name", "duplicate", "already exists");
			}
		}

		if (pet.getBirthDate() != null && pet.getBirthDate().isAfter(LocalDate.now())) {
			result.rejectValue("birthDate", "typeMismatch.birthDate");
		}

		if (result.hasErrors()) {
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}

		updatePetDetails(owner, pet);
		redirectAttributes.addFlashAttribute("message", "Pet details updated");

		return "redirect:/owners/{ownerId}";
	}

	/*
	 * ========================= HELPER =========================
	 */

	private void updatePetDetails(Owner owner, Pet pet) {
		Integer id = pet.getId();
		Assert.state(id != null, "'pet.getId()' must not be null");

		Pet existingPet = owner.getPet(id);
		if (existingPet != null) {
			existingPet.setName(pet.getName());
			existingPet.setBirthDate(pet.getBirthDate());
			existingPet.setType(pet.getType());
		}
		else {
			owner.addPet(pet);
		}

		this.owners.save(owner);
	}

}
