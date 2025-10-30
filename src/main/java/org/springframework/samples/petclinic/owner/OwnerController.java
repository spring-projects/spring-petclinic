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

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @author Wick Dynex
 */
@Controller
class OwnerController {

	private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";

	private static final String DEFAULT_PAGE_SIZE_VALUE = "10";

	private static final int DEFAULT_PAGE_SIZE = Integer.parseInt(DEFAULT_PAGE_SIZE_VALUE);

	private static final List<Integer> PAGE_SIZE_OPTIONS = List.of(DEFAULT_PAGE_SIZE, 20, 30, 40, 50);

	private final OwnerRepository owners;

	public OwnerController(OwnerRepository owners) {
		this.owners = owners;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@ModelAttribute("owner")
	public Owner findOwner(@PathVariable(name = "ownerId", required = false) @Nullable Integer ownerId) {
		return ownerId == null ? new Owner()
				: this.owners.findById(ownerId)
					.orElseThrow(() -> new IllegalArgumentException("Owner not found with id: " + ownerId
							+ ". Please ensure the ID is correct " + "and the owner exists in the database."));
	}

	@GetMapping("/owners/new")
	public String initCreationForm() {
		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/owners/new")
	public String processCreationForm(@Valid Owner owner, BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("error", "There was an error in creating the owner.");
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		}

		this.owners.save(owner);
		redirectAttributes.addFlashAttribute("message", "New Owner Created");
		return "redirect:/owners/" + owner.getId();
	}

	@GetMapping("/owners/find")
	public String initFindForm() {
		return "owners/findOwners";
	}

	@GetMapping("/owners")
	public String processFindForm(@RequestParam(defaultValue = "1") int page, Owner owner, BindingResult result,
			Model model, @RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE_VALUE) int size) {
		// allow parameterless GET request for /owners to return all records
		String lastName = owner.getLastName();
		if (lastName == null) {
			lastName = ""; // empty string signifies broadest possible search
		}

		owner.setLastName(lastName);

		int resolvedPage = Math.max(page, 1);
		int pageSize = resolvePageSize(size);

		// find owners by last name
		Page<Owner> ownersResults = findPaginatedForOwnersLastName(resolvedPage, pageSize, lastName);
		if (ownersResults.isEmpty()) {
			// no owners found
			result.rejectValue("lastName", "notFound", "not found");
			return "owners/findOwners";
		}

		if (ownersResults.getTotalElements() == 1) {
			// 1 owner found
			owner = ownersResults.iterator().next();
			return "redirect:/owners/" + owner.getId();
		}

		// multiple owners found
		return addPaginationModel(resolvedPage, pageSize, owner, model, ownersResults);
	}

	private String addPaginationModel(int page, int pageSize, Owner owner, Model model, Page<Owner> paginated) {
		List<Owner> listOwners = paginated.getContent();
		long totalItems = paginated.getTotalElements();
		int currentItemCount = paginated.getNumberOfElements();
		long startItem = totalItems == 0 ? 0L : (long) ((page - 1L) * pageSize) + 1L;
		long endItem = currentItemCount == 0 ? startItem : Math.min(startItem + currentItemCount - 1L, totalItems);
		int totalPages = paginated.getTotalPages();
		int windowSize = 10;
		int windowStart = 1;
		int windowEnd = 0;
		List<Integer> pageNumbers = Collections.emptyList();
		boolean showLeadingGap = false;
		boolean showTrailingGap = false;
		if (totalPages > 0) {
			int maxWindowStart = Math.max(1, totalPages - windowSize + 1);
			windowStart = Math.max(1, Math.min(page, maxWindowStart));
			windowEnd = Math.min(windowStart + windowSize - 1, totalPages);
			pageNumbers = IntStream.rangeClosed(windowStart, windowEnd).boxed().toList();
			int leadingHiddenCount = windowStart - 1;
			int trailingHiddenCount = totalPages - windowEnd;
			showLeadingGap = leadingHiddenCount > 0;
			showTrailingGap = trailingHiddenCount > 0;
		}
		model.addAttribute("currentPage", page);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("pageSizeOptions", PAGE_SIZE_OPTIONS);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("totalItems", totalItems);
		model.addAttribute("startItem", startItem);
		model.addAttribute("endItem", endItem);
		model.addAttribute("listOwners", listOwners);
		model.addAttribute("owner", owner);
		model.addAttribute("pageNumbers", pageNumbers);
		model.addAttribute("showLeadingGap", showLeadingGap);
		model.addAttribute("showTrailingGap", showTrailingGap);
		return "owners/ownersList";
	}

	private Page<Owner> findPaginatedForOwnersLastName(int page, int pageSize, String lastname) {
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		return owners.findByLastNameStartingWith(lastname, pageable);
	}

	private int resolvePageSize(int requestedSize) {
		return PAGE_SIZE_OPTIONS.contains(requestedSize) ? requestedSize : DEFAULT_PAGE_SIZE;
	}

	@GetMapping("/owners/{ownerId}/edit")
	public String initUpdateOwnerForm() {
		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/owners/{ownerId}/edit")
	public String processUpdateOwnerForm(@Valid Owner owner, BindingResult result, @PathVariable("ownerId") int ownerId,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			redirectAttributes.addFlashAttribute("error", "There was an error in updating the owner.");
			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		}

		if (!Objects.equals(owner.getId(), ownerId)) {
			result.rejectValue("id", "mismatch", "The owner ID in the form does not match the URL.");
			redirectAttributes.addFlashAttribute("error", "Owner ID mismatch. Please try again.");
			return "redirect:/owners/{ownerId}/edit";
		}

		owner.setId(ownerId);
		this.owners.save(owner);
		redirectAttributes.addFlashAttribute("message", "Owner Values Updated");
		return "redirect:/owners/{ownerId}";
	}

	/**
	 * Custom handler for displaying an owner.
	 * @param ownerId the ID of the owner to display
	 * @return a ModelMap with the model attributes for the view
	 */
	@GetMapping("/owners/{ownerId}")
	public ModelAndView showOwner(@PathVariable("ownerId") int ownerId) {
		ModelAndView mav = new ModelAndView("owners/ownerDetails");
		Optional<Owner> optionalOwner = this.owners.findById(ownerId);
		Owner owner = optionalOwner.orElseThrow(() -> new IllegalArgumentException(
				"Owner not found with id: " + ownerId + ". Please ensure the ID is correct "));
		mav.addObject(owner);
		return mav;
	}

}
