package org.springframework.samples.petclinic.owner;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PetViewController {

	private final PetViewRepository petViewRepository;
	
	public PetViewController(PetViewRepository petViewRepository) {
		this.petViewRepository = petViewRepository;
	}

	@ModelAttribute("pet")
	public Pet findPet(@PathVariable(name = "petId", required = false) Integer petId) {
		return petId == null ? new Pet()
				: this.petViewRepository.findById(petId)
					.orElseThrow(() -> new IllegalArgumentException("Pet not found with id: " + petId
							+ ". Please ensure the ID is correct " + "and the pet exists in the database."));
	}
	
	@GetMapping("/pets/find")
	public String initFindForm() {
		return "pets/findPets";
	}
	
	@GetMapping("/pets")
	public String processFindForm(@RequestParam(defaultValue = "1") int page, Pet pet, BindingResult result,
			Model model) {
		// allow parameterless GET request for /pets to return all records
		if (pet.getName() == null) {
			pet.setName(""); // empty string signifies broadest possible search
		}

		// find owners by last name
		Page<Pet> petsResults = findPaginatedForPetsName(page, pet.getName());
		if (petsResults.isEmpty()) {
			// no owners found
			result.rejectValue("Name", "notFound", "not found");
			return "pets/findPets";
		}

		if (petsResults.getTotalElements() == 1) {
			// 1 owner found
			pet = petsResults.iterator().next();
			return "redirect:/pets/" + pet.getId();
		}

		// multiple owners found
		return addPaginationModel(page, model, petsResults);
	}

	private String addPaginationModel(int page, Model model, Page<Pet> paginated) {
		List<Pet> listPets = paginated.getContent();
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("listPets", listPets);
		return "pets/petsList";
	}
	private Page<Pet> findPaginatedForPetsName(int page, String name) {
		int pageSize = 5;
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		return petViewRepository.findByNameStartingWith(name, pageable);
	}

	@GetMapping("/pets/{petId}")
	public ModelAndView showPet(@PathVariable("petId") int petId) {
		ModelAndView mav = new ModelAndView("pets/petDetails");
		Optional<Pet> optionalPet = this.petViewRepository.findPetByWithOwnerAndAttribute(petId);
		Pet pet = optionalPet.orElseThrow(() -> new IllegalArgumentException(
				"Pet not found with id: " + petId + ". Please ensure the ID is correct "));
		
		mav.addObject(pet);
		mav.addObject(pet.getOwner());
		//mav.addObject(pet.getPetAttribute());
		return mav;
	}
}
