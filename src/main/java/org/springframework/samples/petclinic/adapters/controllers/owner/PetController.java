package org.springframework.samples.petclinic.adapters.controllers.owner;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

import jakarta.validation.Valid;

import org.springframework.samples.petclinic.adapters.dtos.owner.OwnerDto;
import org.springframework.samples.petclinic.adapters.validators.owner.PetValidator;
import org.springframework.samples.petclinic.adapters.dtos.owner.PetDto;
import org.springframework.samples.petclinic.adapters.dtos.owner.PetTypeDto;
import org.springframework.samples.petclinic.adapters.mappers.owner.OwnerMapper;
import org.springframework.samples.petclinic.core.domain.owner.Owner;
import org.springframework.samples.petclinic.core.usecases.owner.ports.FindOwnerPort;
import org.springframework.samples.petclinic.core.usecases.owner.ports.FindPetTypePort;
import org.springframework.samples.petclinic.core.usecases.owner.ports.SaveOwnerPort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
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
class PetController {

	private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";

	private final FindOwnerPort findOwnerPort;

	private final SaveOwnerPort saveOwnerPort;

	private final FindPetTypePort findPetTypePort;

	PetController(FindOwnerPort findOwnerPort, SaveOwnerPort saveOwnerPort, FindPetTypePort findPetTypePort) {
		this.findOwnerPort = findOwnerPort;
		this.saveOwnerPort = saveOwnerPort;
		this.findPetTypePort = findPetTypePort;
	}

	@ModelAttribute("types")
	public Collection<PetTypeDto> populatePetTypes() {
		return findPetTypePort.findAll().stream().map(OwnerMapper::petTypeToDto).toList();
	}

	@ModelAttribute("owner")
	public OwnerDto findOwner(@PathVariable("ownerId") int ownerId) {
		Owner owner = findOwnerPort.findById(ownerId)
			.orElseThrow(() -> new IllegalArgumentException(
					"Owner not found with id: " + ownerId + ". Please ensure the ID is correct."));
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
	public String processCreationForm(OwnerDto ownerDto, @Valid PetDto pet, BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (StringUtils.hasText(pet.getName()) && pet.isNew() && ownerDto.getPet(pet.getName(), true) != null) {
			result.rejectValue("name", "duplicate", "already exists");
		}

		LocalDate currentDate = LocalDate.now();
		if (pet.getBirthDate() != null && pet.getBirthDate().isAfter(currentDate)) {
			result.rejectValue("birthDate", "typeMismatch.birthDate");
		}

		if (result.hasErrors()) {
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}

		Owner owner = findOwnerPort.findById(ownerDto.getId())
			.orElseThrow(() -> new IllegalArgumentException("Owner not found"));
		owner.addPet(OwnerMapper.petToDomain(pet));
		saveOwnerPort.save(owner);
		redirectAttributes.addFlashAttribute("message", "New Pet has been Added");
		return "redirect:/owners/{ownerId}";
	}

	@GetMapping("/pets/{petId}/edit")
	public String initUpdateForm() {
		return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/pets/{petId}/edit")
	public String processUpdateForm(OwnerDto ownerDto, @Valid PetDto pet, BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (StringUtils.hasText(pet.getName())) {
			PetDto existingPet = ownerDto.getPet(pet.getName(), false);
			if (existingPet != null && !Objects.equals(existingPet.getId(), pet.getId())) {
				result.rejectValue("name", "duplicate", "already exists");
			}
		}

		LocalDate currentDate = LocalDate.now();
		if (pet.getBirthDate() != null && pet.getBirthDate().isAfter(currentDate)) {
			result.rejectValue("birthDate", "typeMismatch.birthDate");
		}

		if (result.hasErrors()) {
			return VIEWS_PETS_CREATE_OR_UPDATE_FORM;
		}

		Owner owner = findOwnerPort.findById(ownerDto.getId())
			.orElseThrow(() -> new IllegalArgumentException("Owner not found"));
		org.springframework.samples.petclinic.core.domain.owner.Pet existingPet = owner.getPet(pet.getId());
		if (existingPet != null) {
			existingPet.setName(pet.getName());
			existingPet.setBirthDate(pet.getBirthDate());
			if (pet.getType() != null) {
				existingPet.setType(OwnerMapper.petTypeToDomain(pet.getType()));
			}
		}
		else {
			owner.addPet(OwnerMapper.petToDomain(pet));
		}
		saveOwnerPort.save(owner);
		redirectAttributes.addFlashAttribute("message", "Pet details has been edited");
		return "redirect:/owners/{ownerId}";
	}

}
