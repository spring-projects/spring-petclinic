package org.springframework.samples.petclinic.adapters.controllers.owner;

import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.samples.petclinic.adapters.dtos.owner.VisitDto;
import org.springframework.samples.petclinic.adapters.mappers.owner.OwnerMapper;
import org.springframework.samples.petclinic.core.domain.owner.Owner;
import org.springframework.samples.petclinic.core.domain.owner.Visit;
import org.springframework.samples.petclinic.core.usecases.owner.ports.FindOwnerPort;
import org.springframework.samples.petclinic.core.usecases.owner.ports.SaveOwnerPort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
class VisitController {

	private final FindOwnerPort findOwnerPort;

	private final SaveOwnerPort saveOwnerPort;

	VisitController(FindOwnerPort findOwnerPort, SaveOwnerPort saveOwnerPort) {
		this.findOwnerPort = findOwnerPort;
		this.saveOwnerPort = saveOwnerPort;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id", "*.id");
	}

	@ModelAttribute("visit")
	public VisitDto loadPetWithVisit(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId,
			Map<String, Object> model) {
		Owner owner = findOwnerPort.findById(ownerId)
			.orElseThrow(() -> new IllegalArgumentException(
					"Owner not found with id: " + ownerId + ". Please ensure the ID is correct."));

		org.springframework.samples.petclinic.core.domain.owner.Pet pet = owner.getPet(petId);
		if (pet == null) {
			throw new IllegalArgumentException(
					"Pet with id " + petId + " not found for owner with id " + ownerId + ".");
		}

		model.put("pet", OwnerMapper.petToDto(pet));
		model.put("owner", OwnerMapper.toDto(owner));

		VisitDto visit = new VisitDto();
		pet.addVisit(new Visit());
		return visit;
	}

	@GetMapping("/owners/{ownerId}/pets/{petId}/visits/new")
	public String initNewVisitForm() {
		return "pets/createOrUpdateVisitForm";
	}

	@PostMapping("/owners/{ownerId}/pets/{petId}/visits/new")
	public String processNewVisitForm(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId,
			@Valid VisitDto visitDto, BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "pets/createOrUpdateVisitForm";
		}

		Owner owner = findOwnerPort.findById(ownerId)
			.orElseThrow(() -> new IllegalArgumentException("Owner not found with id: " + ownerId));

		Visit visit = OwnerMapper.visitToDomain(visitDto);
		owner.addVisit(petId, visit);
		saveOwnerPort.save(owner);
		redirectAttributes.addFlashAttribute("message", "Your visit has been booked");
		return "redirect:/owners/" + ownerId;
	}

}
