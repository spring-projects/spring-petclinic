package org.springframework.samples.petclinic.pet;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetRepository;
import org.springframework.samples.petclinic.owner.PetValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Thibault Helsmoortel
 */
@Controller
public class PetController {

	private final PetRepository pets;

	private final OwnerRepository owners;

	public PetController(PetRepository pets, OwnerRepository owners) {
		this.pets = pets;
		this.owners = owners;
	}

	@InitBinder("pet")
	public void initPetBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new PetValidator());
	}

	@ModelAttribute("pet")
	public Pet findPet(@PathVariable(name = "petId", required = false) Integer petId) {
		if (petId == null) {
			return new Pet();
		}

		Pet pet = pets.findById(petId);
		if (pet == null) {
			throw new IllegalArgumentException("Pet ID not found: " + petId);
		}

		return pet;
	}

	@GetMapping("/pets/find")
	public String initFindForm() {
		return "pets/findPets";
	}

	@GetMapping("/pets")
	public String processFindForm(@RequestParam(defaultValue = "1") int page, Pet pet, BindingResult result,
			Model model) {
		// allow parameterless GET request for /pets to return all records
		String petName = "";
		if (pet.getName() != null) {
			petName = pet.getName().toLowerCase();
		}

		// find pets by name
		Page<Pet> petsResults = findPaginatedForPetName(page, petName);
		if (petsResults.isEmpty()) {
			// no pets found
			result.rejectValue("name", "notFound", "not found");

			return "pets/findPets";
		}

		if (petsResults.getTotalElements() == 1) {
			// 1 pet found
			pet = petsResults.iterator().next();

			return "redirect:/pets/" + pet.getId();
		}

		// multiple pets found
		return addPaginationModel(page, model, petsResults);
	}

	private Page<Pet> findPaginatedForPetName(int page, String name) {
		int pageSize = 5;
		Pageable pageable = PageRequest.of(page - 1, pageSize);

		return pets.findByName(name, pageable);
	}

	private String addPaginationModel(int page, Model model, Page<Pet> paginated) {
		List<Pet> listPets = paginated.getContent();
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("listPets", listPets);
		model.addAttribute("listPetAndOwners",
				listPets.stream().map(pet -> new PetAndOwner(pet, owners.findByPets_Id(pet.getId()))).toList());

		return "pets/petsList";
	}

	record PetAndOwner(Pet pet, Owner owner) {
	}

	@GetMapping("/pets/{petId}")
	public ModelAndView showPet(@PathVariable("petId") int petId) {
		ModelAndView mav = new ModelAndView("pets/petDetails");
		Pet pet = this.pets.findById(petId);
		Owner owner = owners.findByPets_Id(petId);
		mav.addObject("pet", pet);
		mav.addObject("owner", owner);

		return mav;
	}

}
