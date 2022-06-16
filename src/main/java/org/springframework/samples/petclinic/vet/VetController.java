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
package org.springframework.samples.petclinic.vet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
class VetController {

	private static final String VIEWS_VET_CREATE_OR_UPDATE_FORM = "vets/createOrUpdateVetForma.html";

	private final VetRepository vets;

	public VetController(VetRepository clinicService) {
		this.vets = clinicService;
	}

	@ModelAttribute("vet")
	public Vet findVet(@PathVariable(name = "vetId", required = false) Integer vetId) {
		return vetId == null ? new Vet() : this.vets.findById(vetId);
	}

	@GetMapping("/vets/new")
	public String initCreationForm(Map<String, Object> model) {
		Vet vet = new Vet();
		model.put("vet", vet);
		return VIEWS_VET_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/vets/new")
	public String processCreationForm(@Valid Vet vet, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_VET_CREATE_OR_UPDATE_FORM;
		}
		else {
			this.vets.save(vet);
			return "redirect:/vets/" + vet.getId();
		}
	}

	@GetMapping("/vets/find")
	public String initFindForm(Map<String, Object> model) {
		model.put("vet", new Vet());
		return "vets/findVets";
	}

	@PostMapping("/findVets")
	public String processFindForm(@RequestParam(defaultValue = "1") int page, Vet vet, BindingResult result,
			Model model) {

		// allow parameterless GET request for /owners to return all records
		if (vet.getLastName() == null) {
			vet.setLastName(""); // empty string signifies broadest possible search
		}
		// find vets by last name
		String lastName = vet.getLastName();
		Page<Vet> vetsResults = findPaginatedForOwnersLastName(page, lastName);
		if (vetsResults.isEmpty()) {
			// no vets found
			result.rejectValue("lastName", "notFound", "not found");
			return "vets/findVets";
		}
		else if (vetsResults.getTotalElements() == 1) {
			// 1 vet found
			vet = vetsResults.iterator().next();
			return "redirect:/vets/" + vet.getId();
		}
		else {
			// multiple vets found
			return addPaginationModel(page, vetsResults, model);
		}
	}

	private Page<Vet> findPaginatedForOwnersLastName(int page, String lastname) {

		int pageSize = 5;
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		return vets.findByLastName(lastname, pageable);

	}

	@GetMapping("/vets.html")
	public String showVetList(@RequestParam(defaultValue = "1") int page, Model model) {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects so it is simpler for Object-Xml mapping
		Vets vets = new Vets();
		Page<Vet> paginated = findPaginated(page);
		vets.getVetList().addAll(paginated.toList());
		return addPaginationModel(page, paginated, model);

	}

	private String addPaginationModel(int page, Page<Vet> paginated, Model model) {
		List<Vet> listVets = paginated.getContent();
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("listVets", listVets);
		return "vets/vetList";
	}

	private Page<Vet> findPaginatedForVetsLastName(int page, String lastname) {

		int pageSize = 5;
		Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("lastName"));
		return vets.findByLastName(lastname, pageable);

	}

	@GetMapping("/vets/{vetId}/edit")
	public String initUpdateVetForm(@PathVariable("vetId") int ownerId, Model model) {
		Vet vet = this.vets.findById(ownerId);
		model.addAttribute(vet);
		return VIEWS_VET_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/vets/{vetId}/edit")
	public String processUpdateOwnerForm(@Valid Vet vet, BindingResult result, @PathVariable("vetId") int vetId) {
		if (result.hasErrors()) {
			return VIEWS_VET_CREATE_OR_UPDATE_FORM;
		}
		else {
			vet.setId(vetId);
			this.vets.save(vet);
			return "redirect:/vets/{vetId}";
		}
	}

	private Page<Vet> findPaginated(int page) {
		int pageSize = 5;
		Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by("lastName"));
		return vets.findAll(pageable);
	}

	@GetMapping({ "/vets" })
	public @ResponseBody Vets showResourcesVetList() {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects so it is simpler for JSon/Object mapping
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vets.findAll());
		return vets;
	}

	@GetMapping("/vets/{vetId}")
	public ModelAndView showVet(@PathVariable("vetId") int vetId) {
		ModelAndView mav = new ModelAndView("vets/vetDetails.html");
		Vet vet = this.vets.findById(vetId);
		mav.addObject(vet);
		return mav;
	}

}
