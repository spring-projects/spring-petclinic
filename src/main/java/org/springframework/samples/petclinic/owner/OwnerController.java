package org.springframework.samples.petclinic.owner;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.samples.petclinic.util.DependencyLogger;

import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
class OwnerController {

	private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";

	private final OwnerRepository owners;

	private final OwnerService ownerService;

	public OwnerController(OwnerRepository owners, OwnerService ownerService) {
		this.owners = owners;
		this.ownerService = ownerService;
	}

	@InitBinder
	public void initOwnerBinder(WebDataBinder dataBinder) {
		DependencyLogger.log("Initializing data binder in OwnerController");
		setAllowedFields(dataBinder);
		addOwnerValidator(dataBinder);
	}

	private void setAllowedFields(WebDataBinder dataBinder) {
		DependencyLogger.log("Setting disallowed fields for WebDataBinder");
		dataBinder.setDisallowedFields("id");
	}

	private void addOwnerValidator(WebDataBinder dataBinder) {
		DependencyLogger.log("Adding OwnerValidator to WebDataBinder");
		dataBinder.addValidators((Validator) new OwnerValidator());
	}

	@ModelAttribute("owner")
	public Owner findOwner(@PathVariable(name = "ownerId", required = false) Integer ownerId) {
		DependencyLogger.log("findOwner called with ownerId: " + ownerId);
		return ownerId == null ? new Owner()
				: this.owners.findById(ownerId)
					.orElseThrow(() -> new IllegalArgumentException("Owner not found with id: " + ownerId
							+ ". Please ensure the ID is correct " + "and the owner exists in the database."));
	}

	@GetMapping("/owners/new")
	public String initCreationForm() {
		DependencyLogger.log("Initializing creation form for new owner");
		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/owners/new")
	public String processCreationForm(@Valid Owner owner, BindingResult result, RedirectAttributes redirectAttributes) {
		DependencyLogger.log("Processing creation form for new owner");
		if (result.hasErrors()) {
			DependencyLogger.log("Validation errors occurred while creating owner");
			redirectAttributes.addFlashAttribute("error", "There was an error in creating the owner.");
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		}

		this.owners.save(owner);
		DependencyLogger.log("New owner saved with ID: " + owner.getId());
		redirectAttributes.addFlashAttribute("message", "New Owner Created");
		return "redirect:/owners/" + owner.getId();
	}

	@GetMapping("/owners/find")
	public String initFindForm() {
		DependencyLogger.log("Initializing find form for owners");
		return "owners/findOwners";
	}

	@GetMapping("/owners")
	public String processFindForm(@RequestParam(defaultValue = "1") int page,
			@RequestParam(required = false) String city, Owner owner, BindingResult result, Model model) {
		DependencyLogger.log("Processing find form for owners, page: " + page + ", city: " + city);

		if (owner.getLastName() == null) {
			owner.setLastName(""); // Broadest possible search
		}

		// Use ownerService to fetch paginated results
		Page<Owner> ownersResults = ownerService.findOwners(owner.getLastName(), city, page);

		if (ownersResults.isEmpty()) {
			DependencyLogger.log("No owners found for last name: " + owner.getLastName());
			result.rejectValue("lastName", "notFound", "not found");
			return "owners/findOwners";
		}

		if (ownersResults.getTotalElements() == 1) {
			owner = ownersResults.iterator().next();
			return "redirect:/owners/" + owner.getId();
		}

		DependencyLogger.log("Multiple owners found, adding pagination model.");
		return addPaginationModel(page, model, ownersResults);
	}

	private String addPaginationModel(int page, Model model, Page<Owner> paginated) {
		DependencyLogger.log("Adding pagination model for page: " + page);
		List<Owner> listOwners = paginated.getContent();
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("listOwners", listOwners);
		return "owners/ownersList";
	}

	private Page<Owner> findPaginatedForOwnersLastName(int page, String lastname) {
		DependencyLogger.log("Finding paginated owners for last name: " + lastname + ", page: " + page);
		int pageSize = 5;
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		return owners.findByLastNameStartingWith(lastname, pageable);

	}

	@GetMapping("/owners/{ownerId}/edit")
	public String initUpdateOwnerForm() {
		DependencyLogger.log("Initializing update form for owner");
		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/owners/{ownerId}/edit")
	public String processUpdateOwnerForm(@Valid Owner owner, BindingResult result, @PathVariable("ownerId") int ownerId,
			RedirectAttributes redirectAttributes) {
		DependencyLogger.log("Processing update form for owner with ID: " + ownerId);
		if (result.hasErrors()) {
			DependencyLogger.log("Validation errors occurred while updating owner with ID: " + ownerId);
			redirectAttributes.addFlashAttribute("error", "There was an error in updating the owner.");
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		}

		if (owner.getId() != ownerId) {
			DependencyLogger.log("Owner ID mismatch during update. Form ID: " + owner.getId() + ", URL ID: " + ownerId);
			result.rejectValue("id", "mismatch", "The owner ID in the form does not match the URL.");
			redirectAttributes.addFlashAttribute("error", "Owner ID mismatch. Please try again.");
			return "redirect:/owners/{ownerId}/edit";
		}

		owner.setId(ownerId);
		this.owners.save(owner);
		DependencyLogger.log("Owner updated with ID: " + ownerId);
		redirectAttributes.addFlashAttribute("message", "Owner Values Updated");
		return "redirect:/owners/{ownerId}";
	}

	@GetMapping("/owners/{ownerId}")
	public ModelAndView showOwner(@PathVariable("ownerId") int ownerId) {
		DependencyLogger.log("Displaying owner details for ownerId: " + ownerId);
		ModelAndView mav = new ModelAndView("owners/ownerDetails");
		Optional<Owner> optionalOwner = this.owners.findById(ownerId);
		Owner owner = optionalOwner.orElseThrow(() -> new IllegalArgumentException(
				"Owner not found with id: " + ownerId + ". Please ensure the ID is correct "));
		mav.addObject(owner);
		return mav;
	}

}
