/*
 * Copyright 2012-2019 the original author or authors.
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
package org.springframework.samples.petclinic.visit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @author Dave Syer
 */
@Controller
public
class VisitController {

	private final OwnerRepository owners;

	private final VisitRepository visit;

	public VisitController(OwnerRepository owners, VisitRepository visit) {
		this.owners = owners;
		this.visit = visit;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	/**
	 * Called before each and every @RequestMapping annotated method. 2 goals: - Make sure
	 * we always have fresh data - Since we do not use the session scope, make sure that
	 * Pet object always has an id (Even though id is not part of the form fields)
	 *
	 * @param petId
	 * @return Pet
	 */

	public Owner loadPetWithVisit(int ownerId, int petId, Map<String, Object> model) {
		Owner owner = this.owners.findById(ownerId);
		Pet pet = owner.getPet(petId);
		model.put("pet", pet);
		model.put("owner", owner);
		Visit visit = new Visit();
		pet.addVisit(visit);
		return owner;
	}

	// Spring MVC calls method loadPetWithVisit(...) before initNewVisitForm is
	// called

	@GetMapping("/owners/{ownerId}/pets/{petId}/visits/new")
	public String initNewVisitForm(@PathVariable("petId") int petId,
								   @PathVariable("ownerId") int ownerId, Map<String, Object> model) {
		loadPetWithVisit(ownerId, petId, model);
		return "pets/createOrUpdateVisitForm";
	}

	// Spring MVC calls method loadPetWithVisit(...) before processNewVisitForm is
	// called
	@PostMapping("/owners/{ownerId}/pets/{petId}/visits/new")
	public String processNewVisitForm(@PathVariable("ownerId") int ownerId, @PathVariable int petId, @Valid Visit visit,
									  BindingResult result, Map<String, Object> model) {
		Owner owner = loadPetWithVisit(ownerId, petId, model);

		if (result.hasErrors()) {
			return "pets/createOrUpdateVisitForm";
		} else {
			owner.addVisit(petId, visit);
			this.owners.save(owner);
			return "redirect:/owners/{ownerId}";
		}
	}


	@GetMapping("/visits")
	public String showVetList(@RequestParam(defaultValue = "1") int page, Model model) {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects so it is simpler for Object-Xml mapping
		Page<Visit> paginated = findPaginated(page);
		return addPaginationModel(page, paginated, model);

	}

	private Page<Visit> findPaginated(int page) {
		int pageSize = 5;
		Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("date"));
		return visit.findAll(pageable);
	}

	private String addPaginationModel(int page, Page<Visit> paginated, Model model) {
		List<Visit> visitList = paginated.getContent();
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("visitList", visitList);
		return "visits/visits";
	}
}
