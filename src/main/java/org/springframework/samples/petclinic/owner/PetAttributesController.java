package org.springframework.samples.petclinic.owner;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/petAttributes")
public class PetAttributesController {

	private final PetAttributesRepository petAttributesRepository;
	
	private final PetAttributesService petAttributesService;

	@Autowired
	public PetAttributesController(PetAttributesRepository petAttributesRepository,PetAttributesService petAttributesService) {
		this.petAttributesRepository = petAttributesRepository;
		this.petAttributesService=petAttributesService;
	}

	@GetMapping("/all")
	public String list(Model model) {
		model.addAttribute("listAttributes", petAttributesRepository.findAll());
		return "petAttributes/list";
	}

	@GetMapping("/new")
	public String showCreateForm(Model model) {
		model.addAttribute("petAttributes", new PetAttributes());
		return "petAttributes/form";
	}

	@PostMapping("/save")
	public String save(@ModelAttribute PetAttributes petAttributes, RedirectAttributes redirectAttributes) {
		petAttributesRepository.save(petAttributes);
		redirectAttributes.addFlashAttribute("successMessage", "Saved successfully!");
		return "redirect:/petAttributes/all";
	}

	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable("id") Integer id, Model model) {
		PetAttributes attr = petAttributesRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("Invalid ID: " + id));
		model.addAttribute("petAttributes", attr);
		return "petAttributes/form";
	}
	@GetMapping("/search")
    public String showSearchForm() {
        return "petAttributes/search";
    }
	
	@GetMapping(value = "/search", params = "petId")
	public String searchByPetId(@RequestParam("petId") Integer petId, Model model) {
		PetAttributes result = petAttributesService.findByPetId(petId);
		if (result != null) {
			model.addAttribute("listAttributes", List.of(result));
		} else {
			model.addAttribute("listAttributes", List.of());
			model.addAttribute("error", "No results for Pet ID: " + petId);
		}
		return "petAttributes/search";
	}
	 

	@PostMapping("/delete/{id}")
	public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		petAttributesRepository.deleteById(id);
		redirectAttributes.addFlashAttribute("successMessage", "Deleted successfully!");
		return "redirect:/petAttributes/all";
	}

}
