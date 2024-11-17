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

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.model.NamedEntity;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
class VetController {

	private final VetRepository vetRepository;
	private final SpecialtyRepository specialtyRepository;

	@Autowired
	public VetController(VetRepository clinicService, SpecialtyRepository specialtyRepository) {
		this.vetRepository = clinicService;
		this.specialtyRepository = specialtyRepository;
	}
	@ModelAttribute("specialties")
	public Collection<Specialty> populateSpecialties() {
		return this.specialtyRepository.findSpecialties();
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
	@GetMapping("/vets/find")
	public String initFindForm() {
		return "vets/findVets";
	}
	@ModelAttribute("vet")
	public Vet findVet(@PathVariable(name = "vetId", required = false) Integer vetId) {
		return vetId == null ? new Vet()
			: this.vetRepository.findById(vetId)
			.orElseThrow(() -> new IllegalArgumentException("Vet not found with id: " + vetId
				+ ". Please ensure the ID is correct " + "and the owner exists in the database."));
	}

	static final String VIEWS_VETS_CREATE_OR_UPDATE_FORM = "vets/createOrUpdateVetForm";
	@GetMapping("/vets/new")
	public String initCreationForm() {
		System.out.println("Creating a new vet");
		return VIEWS_VETS_CREATE_OR_UPDATE_FORM;
	}
	@PostMapping("/vets/new")
	public String processCreationForm(@Valid Vet vet, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return VIEWS_VETS_CREATE_OR_UPDATE_FORM;
		}
		this.vetRepository.save(vet);
		return "redirect:/vets.html";
	}
	private String addPaginationModel(int page, Page<Vet> paginated, Model model) {
		List<Vet> listVets = paginated.getContent();
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("listVets", listVets);
		return "vets/vetList";
	}
	@GetMapping("/vets")
	public String processFindForm(@RequestParam(defaultValue = "1") int page, Vet vet, BindingResult result,
								  Model model) {
		// allow parameterless GET request for /owners to return all records
		System.out.println("Find vets:"+vet);
		if (vet.getLastName() == null) {
			vet.setLastName(""); // empty string signifies broadest possible search
		}

		// find owners by last name
		Page<Vet> vetsResults = findPaginatedForVetsLastName(page, vet.getLastName());
		if (vetsResults.isEmpty()) {
			// no owners found
			result.rejectValue("lastName", "notFound", "not found");
			return "vets/findVets";
		}

		/*if (vetsResults.getTotalElements() == 1) {
			// 1 owner found
			vet = vetsResults.iterator().next();
			return "redirect:/vets/" + vet.getId();
		}*/

		// multiple owners found
		return addPaginationModel(page, vetsResults, model);
	}
	private Page<Vet> findPaginatedForVetsLastName(int page, String lastname) {
		int pageSize = 5;
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		return vetRepository.findByLastNameStartingWith(lastname, pageable);
	}

	private Page<Vet> findPaginated(int page) {
		int pageSize = 5;
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		return vetRepository.findAll(pageable);
	}

//	@GetMapping({ "/vets" })
//	public @ResponseBody Vets showResourcesVetList() {
//		// Here we are returning an object of type 'Vets' rather than a collection of Vet
//		// objects so it is simpler for JSon/Object mapping
//		Vets vets = new Vets();
//		vets.getVetList().addAll(this.vetRepository.findAll());
//		return vets;
//	}

}
