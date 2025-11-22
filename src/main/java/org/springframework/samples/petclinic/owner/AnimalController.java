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
package org.springframework.samples.petclinic.animal;

import java.util.Optional;

import org.springframework.samples.petclinic.model.Animal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller class for managing {@link Animal} entities.
 *
 * Mirrors the structure of the PetController from Spring PetClinic.
 *
 * Author list follows PetClinic conventions.
 * 
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Wick Dynex
 */
@Controller
@RequestMapping("/animals")
class AnimalController {

	private static final String VIEWS_ANIMALS_CREATE_OR_UPDATE_FORM = "animals/createOrUpdateAnimalForm";

	private final AnimalRepository animals;

	public AnimalController(AnimalRepository animals) {
		this.animals = animals;
	}

	@ModelAttribute("animal")
	public @Nullable Animal findAnimal(@PathVariable(name = "animalId", required = false) @Nullable Integer id) {
		if (id == null) {
			return new Animal();
		}

		Optional<Animal> optional = this.animals.findById(id);
		return optional.orElseThrow(() -> new IllegalArgumentException(
				"Animal not found with id: " + id + ". Please ensure the ID is correct"));
	}

	@InitBinder("animal")
	public void initAnimalBinder(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping("/new")
	public String initCreationForm(ModelMap model) {
		model.put("animal", new Animal());
		return VIEWS_ANIMALS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/new")
	public String processCreationForm(@Valid Animal animal, BindingResult result,
			RedirectAttributes redirectAttributes) {

		// Example validation similar to name duplicate logic in PetController
		if (StringUtils.hasText(animal.getName())) {
			boolean exists = this.animals.findAll().stream()
					.anyMatch(a -> animal.getName().equalsIgnoreCase(a.getName()));

			if (exists) {
				result.rejectValue("name", "duplicate", "already exists");
			}
		}

		if (result.hasErrors()) {
			return VIEWS_ANIMALS_CREATE_OR_UPDATE_FORM;
		}

		this.animals.save(animal);
		redirectAttributes.addFlashAttribute("message", "New Animal has been added");
		return "redirect:/animals";
	}

	@GetMapping("/{animalId}/edit")
	public String initUpdateForm() {
		return VIEWS_ANIMALS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/{animalId}/edit")
	public String processUpdateForm(@Valid Animal animal, BindingResult result,
			RedirectAttributes redirectAttributes) {

		String name = animal.getName();

		if (StringUtils.hasText(name)) {
			boolean exists = this.animals.findAll().stream()
					.anyMatch(a -> a.getName().equalsIgnoreCase(name)
							&& !a.getId().equals(animal.getId()));

			if (exists) {
				result.rejectValue("name", "duplicate", "already exists");
			}
		}

		if (result.hasErrors()) {
			return VIEWS_ANIMALS_CREATE_OR_UPDATE_FORM;
		}

		this.animals.save(animal);
		redirectAttributes.addFlashAttribute("message", "Animal details have been updated");
		return "redirect:/animals";
	}

	@GetMapping
	public String listAnimals(ModelMap model) {
		model.put("animals", this.animals.findAll());
		return "animals/animalList";
	}

	@GetMapping("/{animalId}")
	public String showAnimal(@PathVariable("animalId") int animalId, ModelMap model) {
		Animal animal = this.animals.findById(animalId).orElseThrow(
				() -> new IllegalArgumentException("Animal not found with id " + animalId));
		model.put("animal", animal);
		return "animals/animalDetails";
	}
}