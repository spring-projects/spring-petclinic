package org.springframework.samples.petclinic.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.dto.PetDetailDto;
import org.springframework.samples.petclinic.exception.ResourceNotFoundException;
import org.springframework.samples.petclinic.model.PetDetail;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.repository.PetDetailRepository;
import org.springframework.samples.petclinic.service.PetDetailService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pet-details")
@Tag(name = "PetDetails" , description = "Pet details apis")
public class PetDetailController {

	private PetDetailService service;
	private PetDetailRepository petRepo;

	@Autowired
	public PetDetailController(PetDetailRepository petRepo, PetDetailService service) {
		this.petRepo = petRepo;
		this.service = service;
	}

	@PostMapping("/{petId}")
	@Operation(summary = "create pet details")
	public ResponseEntity<PetDetail> createDetail(@PathVariable int petId,
												  @RequestBody PetDetailDto dto) {
		Pet pet = petRepo.findByPetId(petId).get().getPet();
		PetDetail detail = PetDetail.builder()
			.pet(pet)
			.temperament(dto.temperament())
			.weight(dto.weight())
			.length(dto.length())
			.build();
		return ResponseEntity.ok(service.savePetDetail(detail));
	}

	@GetMapping("/{petId}")
	@Operation(summary = "Get pet details by per id")
	public ResponseEntity<PetDetail> getDetail(@PathVariable int petId) {
		var detail = service.getPetDetailByPetId(petId);
		return detail != null ? ResponseEntity.ok(detail) : ResponseEntity.notFound().build();
	}

	@PutMapping("/{petId}")
	@Operation(summary = "Update pet detail")
	public ResponseEntity<PetDetail> updateDetail(@PathVariable int petId,
												  @RequestBody PetDetailDto dto) {
		Pet pet = petRepo.findById(petId).orElseThrow(() ->
			new ResourceNotFoundException("Pet not found with id: " + petId)).getPet();

		PetDetail detail = PetDetail.builder()
			.pet(pet)
			.temperament(dto.temperament())
			.weight(dto.weight())
			.length(dto.length())
			.build();

		return ResponseEntity.ok(service.updatePetDetail(petId, detail));
	}

	@DeleteMapping("/{petId}")
	@Operation(summary = "Delete pet detail by pet id")
	public ResponseEntity<Void> deleteDetail(@PathVariable int petId) {
		service.deletePetDetail(petId);
		return ResponseEntity.noContent().build();
	}
}
