package org.springframework.samples.petclinic.adapters.web.owner;

import java.util.List;
import java.util.Objects;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.samples.petclinic.adapters.web.owner.dtos.OwnerDto;
import org.springframework.samples.petclinic.adapters.web.owner.dtos.OwnerSearchDto;
import org.springframework.samples.petclinic.adapters.web.owner.mappers.OwnerMapper;
import org.springframework.samples.petclinic.core.domain.owner.Owner;
import org.springframework.samples.petclinic.core.usecases.owner.PagedResult;
import org.springframework.samples.petclinic.core.usecases.owner.ports.FindOwnerPort;
import org.springframework.samples.petclinic.core.usecases.owner.ports.SaveOwnerPort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
class OwnerController {

	private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";

	private final FindOwnerPort findOwnerPort;

	private final SaveOwnerPort saveOwnerPort;

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id", "*.id");
	}

	@ModelAttribute("owner")
	public OwnerDto findOwner(@PathVariable(name = "ownerId", required = false) Integer ownerId) {
		if (ownerId == null) {
			return new OwnerDto();
		}
		Owner owner = findOwnerPort.findById(ownerId)
			.orElseThrow(() -> new IllegalArgumentException(
					"Owner not found with id: " + ownerId + ". Please ensure the ID is correct."));
		return OwnerMapper.toDto(owner);
	}

	@GetMapping("/owners/new")
	public String initCreationForm() {
		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/owners/new")
	public String processCreationForm(@Valid OwnerDto ownerDto, BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("error", "There was an error in creating the owner.");
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		}
		Owner saved = saveOwnerPort.save(OwnerMapper.toDomain(ownerDto));
		redirectAttributes.addFlashAttribute("message", "New Owner Created");
		return "redirect:/owners/" + saved.getId();
	}

	@GetMapping("/owners/find")
	public String initFindForm() {
		return "owners/findOwners";
	}

	@GetMapping("/owners")
	public String processFindForm(@RequestParam(defaultValue = "1") int page, OwnerDto ownerDto, BindingResult result,
			Model model) {
		PagedResult<Owner> ownersResults = findOwnerPort.findByLastName(ownerDto.getLastName(), page);

		if (ownersResults.getContent().isEmpty()) {
			result.rejectValue("lastName", "notFound", "not found");
			return "owners/findOwners";
		}

		if (ownersResults.getTotalElements() == 1) {
			Owner found = ownersResults.getContent().get(0);
			return "redirect:/owners/" + found.getId();
		}

		List<OwnerDto> listOwners = ownersResults.getContent().stream().map(OwnerMapper::toDto).toList();
		model.addAttribute("currentPage", ownersResults.getCurrentPage());
		model.addAttribute("totalPages", ownersResults.getTotalPages());
		model.addAttribute("totalItems", ownersResults.getTotalElements());
		model.addAttribute("listOwners", listOwners);
		return "owners/ownersList";
	}

	@GetMapping("/owners/{ownerId}/edit")
	public String initUpdateOwnerForm() {
		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/owners/{ownerId}/edit")
	public String processUpdateOwnerForm(@Valid OwnerDto ownerDto, BindingResult result,
			@PathVariable("ownerId") int ownerId, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		}
		ownerDto.setId(ownerId);
		saveOwnerPort.save(OwnerMapper.toDomain(ownerDto));
		redirectAttributes.addFlashAttribute("message", "Owner Values Updated");
		return "redirect:/owners/{ownerId}";
	}

	@GetMapping("/owners/{ownerId}")
	public ModelAndView showOwner(@PathVariable("ownerId") int ownerId) {
		ModelAndView mav = new ModelAndView("owners/ownerDetails");
		Owner owner = findOwnerPort.findById(ownerId)
			.orElseThrow(() -> new IllegalArgumentException(
					"Owner not found with id: " + ownerId + ". Please ensure the ID is correct."));
		mav.addObject("owner", OwnerMapper.toDto(owner));
		return mav;
	}

}
