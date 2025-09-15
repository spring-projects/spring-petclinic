package org.springframework.samples.petclinic.controller;

import org.springframework.samples.petclinic.dto.PetAttributeDto;
import org.springframework.samples.petclinic.service.PetAttributeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/pet-attributes")
public class PetAttributeMvcController {

	private final PetAttributeService service;

    public PetAttributeMvcController(PetAttributeService service) {
        this.service = service;
    }

    /** Show form for a given PetType */
    @GetMapping("/{typeId}/new")
    public String initCreationForm(@PathVariable Integer typeId, Model model) {
        model.addAttribute("petAttribute", new PetAttributeDto());
        model.addAttribute("typeId", typeId); // keep it for form action
        return "petattributes/createOrUpdatePetAttributeForm";
    }

    /** Handle form submission */
    @PostMapping("/{typeId}/new")
    public String processCreationForm(@PathVariable Integer typeId,
                                      @Valid @ModelAttribute("petAttribute") PetAttributeDto dto,
                                      BindingResult result) {

        if (result.hasErrors()) {
            return "petattributes/createOrUpdatePetAttributeForm";
        }

        service.create(typeId, dto);   // ✅ dynamic typeId from URL
        return "redirect:/pet-attributes/list";
    }
    

    /** Show all attributes */
    @GetMapping("/list")
    public String listAll(Model model) {
        model.addAttribute("attributes", service.getByTypeId(1)); // sample typeId
        return "petattributes/petAttributeList";
    }
}
