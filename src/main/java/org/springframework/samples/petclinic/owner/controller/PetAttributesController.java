package org.springframework.samples.petclinic.owner.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.owner.dto.PetAttributesDTO;
import org.springframework.samples.petclinic.owner.expection.PetNotFoundException;
import org.springframework.samples.petclinic.owner.model.PetAttributes;
import org.springframework.samples.petclinic.owner.repository.PetRepository;
import org.springframework.samples.petclinic.owner.service.PetAttributesService;
import org.springframework.samples.petclinic.owner.validation.PetIdExistsValidator;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pets/{petId}/attributes")
public class PetAttributesController {

	private final PetAttributesService petAttributesService;

	private PetIdExistsValidator petIdExistsValidator;

	private PetRepository petRepository;

	public PetAttributesController(PetIdExistsValidator petIdExistsValidator, PetAttributesService petAttributesService,
								   PetRepository petRepository) {
		this.petIdExistsValidator = petIdExistsValidator;
		this.petAttributesService = petAttributesService;
		this.petRepository = petRepository;
	}

	@GetMapping
	public ResponseEntity<?> getPetAttributes(@PathVariable("petId") int petId) {
		if (!petRepository.existsById(petId)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pet not found");
		}

		Optional<PetAttributes> optionalAttributes = petAttributesService.findByPetId(petId);

		return optionalAttributes.<ResponseEntity<?>>map(ResponseEntity::ok)
			.orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Attributes not found"));
	}

	@PostMapping
	public ResponseEntity savePetAttributes(@PathVariable int petId, @Valid @RequestBody PetAttributesDTO dto,
											BindingResult bindingResult) {
		// Validate petId exists
		petIdExistsValidator.validate(petId, bindingResult);

		if (bindingResult.hasErrors()) {
			return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
		}

		dto.setPetId(petId);

		try {
			petAttributesService.savePetAttributes(dto);
			return ResponseEntity.status(HttpStatus.CREATED).body("Pet attributes saved");
		} catch (PetNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

}
