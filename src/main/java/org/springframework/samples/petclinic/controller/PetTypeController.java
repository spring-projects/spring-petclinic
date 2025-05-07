package org.springframework.samples.petclinic.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.PetTypes;
import org.springframework.samples.petclinic.service.PetTypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pettypes")
public class PetTypeController {

	private final PetTypeService petTypeService;

	public PetTypeController(PetTypeService petTypeService) {
		this.petTypeService = petTypeService;
	}

	@PostMapping
	public ResponseEntity<PetTypes> createPetType(@RequestBody PetTypes petType) {
		PetTypes savedPetType = petTypeService.save(petType);
		return ResponseEntity.ok(savedPetType);
	}

	@GetMapping
	public ResponseEntity<List<PetTypes>> getAllPetTypes() {
		List<PetTypes> petTypes = petTypeService.findAll();
		return ResponseEntity.ok(petTypes);
	}
}
