package org.springframework.samples.petclinic.adapters.web.owner;

import java.util.Collection;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.samples.petclinic.adapters.web.owner.dtos.OwnerDto;
import org.springframework.samples.petclinic.adapters.web.owner.dtos.PetDto;
import org.springframework.samples.petclinic.adapters.web.owner.dtos.PetTypeDto;
import org.springframework.samples.petclinic.adapters.web.owner.mappers.OwnerMapper;
import org.springframework.samples.petclinic.adapters.web.owner.validators.PetValidator;
import org.springframework.samples.petclinic.core.domain.owner.Owner;
import org.springframework.samples.petclinic.core.usecases.owner.ports.AddPetPort;
import org.springframework.samples.petclinic.core.usecases.owner.ports.FindOwnerPort;
import org.springframework.samples.petclinic.core.usecases.owner.ports.FindPetTypePort;
import org.springframework.samples.petclinic.core.usecases.owner.ports.UpdatePetPort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/owners/{ownerId}")
@RequiredArgsConstructor
class PetController {

	private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";

	private final FindOwnerPort findOwnerPort;

	private final FindPetTypePort findPetTypePort;

	private final AddPetPort addPetPort;

	private final UpdatePetPort updatePetPort;

	@ModelAttribute("types")
	public Collection<PetTypeDto> populatePetTypes() {
		return findPetTypePort.findAll().stream().map(OwnerMapper::petTypeToDto).toList();
	}

	@ModelAttribute("owner")
	public OwnerDto findOwner(@PathVariable("ownerId") int ownerId) {
		Owner owner = findOwnerPort.findById(ownerId)
			.orElseThrow(() -> new IllegalArgumentException("Owner not found with id: " + ownerId));
		return OwnerMapper.toDto(owner);
	}

	@ModelAttribute("pet")
	public PetDto findPet(@PathVariable("ownerId") int ownerId,
			@PathVariable(name = "petId", required = false) Integer petId) {
		if (petId == null) {
			return new PetDto();
		}
		return findOwner(ownerId).getPet(petId);
	}

	@InitBinder("owner")
	public void initOwnerBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id", "*.id");
	}

	@InitBinder("pet")
	public void initPetBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new PetValidator());
		dataBinder.setDisallowedFields("id", "*.id");
	}

	@GetMapping("/pets/new")
	public String initCreationForm(OwnerDto owner, ModelMap model) {
		owner.addPet(new PetDto());
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/pets/new")
	public String processCreationForm(@PathVariable("ownerId") int ownerId, @Valid PetDto pet, BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}
		try {
			addPetPort.addPet(ownerId, OwnerMapper.petToDomain(pet));
		}
		catch (IllegalArgumentException e) {
			result.rejectValue("name", "duplicate", e.getMessage());
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}
		redirectAttributes.addFlashAttribute("message", "New Pet has been Added");
		return "redirect:/owners/{ownerId}";
	}

	@GetMapping("/pets/{petId}/edit")
	public String initUpdateForm() {
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/pets/{petId}/edit")
	public String processUpdateForm(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId,
			@Valid PetDto pet, BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}
		try {
			updatePetPort.updatePet(ownerId, petId, OwnerMapper.petToDomain(pet));
		}
		catch (IllegalArgumentException e) {
			result.rejectValue("name", "duplicate", e.getMessage());
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}
		redirectAttributes.addFlashAttribute("message", "Pet details has been edited");
		return "redirect:/owners/{ownerId}";
	}

}
