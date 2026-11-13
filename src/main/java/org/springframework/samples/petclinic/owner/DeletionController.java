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

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Server-rendered confirm + execute endpoints for deleting owners, pets and visits. The
 * delete rules live in {@link DeletionService}.
 */
@Controller
class DeletionController {

	private static final String VIEWS_CONFIRM_DELETE = "confirmDelete";

	private static final String OWNERS_LIST = "redirect:/owners";

	private final DeletionService deletion;

	DeletionController(DeletionService deletion) {
		this.deletion = deletion;
	}

	@GetMapping("/owners/{ownerId}/delete")
	public String confirmDeleteOwner(@PathVariable("ownerId") int ownerId, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Owner owner = this.deletion.getOwner(ownerId);
			addConfirmModel(model, "Owner", ownerId, "/owners/" + ownerId + "/delete", "/owners/" + ownerId,
					"Delete owner " + owner.getFirstName() + " " + owner.getLastName()
							+ "? This cannot be undone. An owner can only be deleted when they have no pets.");
			return VIEWS_CONFIRM_DELETE;
		}
		catch (ResourceNotFoundException ex) {
			return notFound(ex, ownerId, redirectAttributes);
		}
	}

	@PostMapping("/owners/{ownerId}/delete")
	public String deleteOwner(@PathVariable("ownerId") int ownerId, RedirectAttributes redirectAttributes) {
		try {
			this.deletion.deleteOwner(ownerId);
		}
		catch (ResourceNotFoundException ex) {
			return notFound(ex, ownerId, redirectAttributes);
		}
		catch (DeleteBlockedException ex) {
			return blocked(ex, ownerId, redirectAttributes);
		}
		redirectAttributes.addFlashAttribute("successMessage", "Owner has been deleted");
		// MLG-13 known edge case: when exactly one owner remains, /owners redirects again
		// to that owner's page (OwnerController#processFindForm), which drops this flash
		// message.
		return OWNERS_LIST;
	}

	@GetMapping("/owners/{ownerId}/pets/{petId}/delete")
	public String confirmDeletePet(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Pet pet = this.deletion.getPet(ownerId, petId);
			addConfirmModel(model, "Pet", petId, "/owners/" + ownerId + "/pets/" + petId + "/delete",
					"/owners/" + ownerId, "Delete pet " + pet.getName()
							+ "? This cannot be undone. A pet can only be deleted when it has no visits.");
			return VIEWS_CONFIRM_DELETE;
		}
		catch (ResourceNotFoundException ex) {
			return notFound(ex, ownerId, redirectAttributes);
		}
	}

	@PostMapping("/owners/{ownerId}/pets/{petId}/delete")
	public String deletePet(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId,
			RedirectAttributes redirectAttributes) {
		try {
			this.deletion.deletePet(ownerId, petId);
		}
		catch (ResourceNotFoundException ex) {
			return notFound(ex, ownerId, redirectAttributes);
		}
		catch (DeleteBlockedException ex) {
			return blocked(ex, ownerId, redirectAttributes);
		}
		redirectAttributes.addFlashAttribute("successMessage", "Pet has been deleted");
		return "redirect:/owners/" + ownerId;
	}

	@GetMapping("/owners/{ownerId}/pets/{petId}/visits/{visitId}/delete")
	public String confirmDeleteVisit(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId,
			@PathVariable("visitId") int visitId, Model model, RedirectAttributes redirectAttributes) {
		try {
			Visit visit = this.deletion.getVisit(ownerId, petId, visitId);
			addConfirmModel(model, "Visit", visitId,
					"/owners/" + ownerId + "/pets/" + petId + "/visits/" + visitId + "/delete", "/owners/" + ownerId,
					"Delete the visit on " + visit.getDate() + "? This cannot be undone.");
			return VIEWS_CONFIRM_DELETE;
		}
		catch (ResourceNotFoundException ex) {
			return notFound(ex, ownerId, redirectAttributes);
		}
	}

	@PostMapping("/owners/{ownerId}/pets/{petId}/visits/{visitId}/delete")
	public String deleteVisit(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId,
			@PathVariable("visitId") int visitId, RedirectAttributes redirectAttributes) {
		try {
			this.deletion.deleteVisit(ownerId, petId, visitId);
		}
		catch (ResourceNotFoundException ex) {
			return notFound(ex, ownerId, redirectAttributes);
		}
		redirectAttributes.addFlashAttribute("successMessage", "Visit has been deleted");
		return "redirect:/owners/" + ownerId;
	}

	private void addConfirmModel(Model model, String entityName, int entityId, String postUrl, String cancelUrl,
			String warningMessage) {
		model.addAttribute("entityName", entityName);
		model.addAttribute("entityId", entityId);
		model.addAttribute("postUrl", postUrl);
		model.addAttribute("cancelUrl", cancelUrl);
		model.addAttribute("warningMessage", warningMessage);
	}

	private String blocked(DeleteBlockedException ex, int ownerId, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
		return "redirect:/owners/" + ownerId;
	}

	/**
	 * A missing owner leaves nothing to show, so go to the owners list; a missing pet or
	 * visit goes back to the (existing) owner.
	 */
	private String notFound(ResourceNotFoundException ex, int ownerId, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
		return DeletionService.OWNER.equals(ex.getEntity()) ? OWNERS_LIST : "redirect:/owners/" + ownerId;
	}

}
