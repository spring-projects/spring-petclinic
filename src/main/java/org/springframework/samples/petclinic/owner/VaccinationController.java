package org.springframework.samples.petclinic.owner;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/owners/{ownerId}/pets/{petId}/vaccinations")
public class VaccinationController {

	private final VaccinationRepository vaccinationRepository;

	private final OwnerRepository ownerRepository;

	public VaccinationController(VaccinationRepository vaccinationRepository, OwnerRepository ownerRepository) {
		this.vaccinationRepository = vaccinationRepository;
		this.ownerRepository = ownerRepository;
	}

	@GetMapping("/new")
	public String initCreationForm(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId,
			Model model) {
		Owner owner = ownerRepository.findById(ownerId)
			.orElseThrow(() -> new IllegalArgumentException("Invalid owner Id: " + ownerId));
		Pet pet = owner.getPet(petId);
		if (pet == null) {
			throw new IllegalArgumentException("Invalid pet Id: " + petId);
		}

		Vaccination vaccination = new Vaccination();
		vaccination.setPet(pet);

		model.addAttribute("vaccination", vaccination);
		model.addAttribute("ownerId", ownerId); // pass ownerId explicitly
		return "pets/createOrUpdateVaccinationForm";
	}

	@PostMapping("/new")
	public String processCreationForm(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId,
			@Valid Vaccination vaccination, BindingResult result, Model model) {
		if (result.hasErrors()) {
			model.addAttribute("ownerId", ownerId); // needed if form has errors
			return "pets/createOrUpdateVaccinationForm";
		}

		Owner owner = ownerRepository.findById(ownerId)
			.orElseThrow(() -> new IllegalArgumentException("Invalid owner Id: " + ownerId));
		Pet pet = owner.getPet(petId);
		if (pet == null) {
			throw new IllegalArgumentException("Invalid pet Id: " + petId);
		}

		vaccination.setPet(pet);
		vaccinationRepository.save(vaccination);
		return "redirect:/owners/" + ownerId;
	}

}
