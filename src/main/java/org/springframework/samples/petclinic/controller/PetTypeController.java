package org.springframework.samples.petclinic.controller;

import org.springframework.samples.petclinic.model.ExtendedPetType;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/extended-pet-types")
public class PetTypeController {

	private final PetService service;

	public PetTypeController(PetService service) {
		this.service = service;
	}

	@GetMapping
	public Iterable<ExtendedPetType> getAll() {
		return service.findAll();
	}

	@PostMapping
	public ExtendedPetType add(@RequestBody ExtendedPetType petType) {
		return service.save(petType);
	}

	@GetMapping("/{id}")
	public Optional<ExtendedPetType> getById(@PathVariable Integer id) {
		return service.findById(id);
	}

}
