package org.springframework.samples.petclinic.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.dto.PetAttributeDto;
import org.springframework.samples.petclinic.model.PetTypeAttribute;
import org.springframework.samples.petclinic.service.PetAttributeService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/* REST Controller for managing PetTypeAttributes.
 * Purpose: Provides CRUD endpoints under /v1/api/pet.
 * @Author: Sujeet Sharma
 * @Date: 08--9-2025
 */
@RestController
@RequestMapping("/v1/api/pet")
public class PetAttributeController {

	private final PetAttributeService service;
	private static final Logger logger = LoggerFactory.getLogger(PetAttributeController.class);

	public PetAttributeController(PetAttributeService service) {
		this.service = service;
	}

	/** Get attribute by ID */
	@GetMapping("/{id}")
	public ResponseEntity<PetAttributeDto> getById(@PathVariable Integer id) {
		logger.info("PetAttributeController ::getById :: Fetching PetAttributeDto with id={}", id);
		PetAttributeDto dto = service.getById(id);
		logger.debug("PetAttributeController ::getById :: Fetched PetAttributeDto: {}", dto);
		return ResponseEntity.ok(dto);
	}

	/** Get all attributes for a given PetType */
	@GetMapping("/type/{typeId}")
	public ResponseEntity<List<PetAttributeDto>> getByTypeId(@PathVariable Integer typeId) {
		logger.info("PetAttributeController ::getByTypeId :: Fetching all PetAttributeDto for PetType id={}", typeId);
		List<PetAttributeDto> attributes = service.getByTypeId(typeId);
		logger.debug("PetAttributeController ::getByTypeId :: Fetched {} PetAttributeDto for PetType id={}", attributes.size(), typeId);
		return ResponseEntity.ok(attributes);
	}


	/** Create a new attribute for a PetType */
	@PostMapping("/{typeId}")
	public ResponseEntity<PetAttributeDto> create(@PathVariable Integer typeId,
			@Valid @RequestBody PetAttributeDto attrDto) {
		logger.info("PetAttributeController ::create :: Creating PetAttributeDto for PetType id={}", typeId);
		PetAttributeDto created = service.create(typeId, attrDto);
		logger.debug("PetAttributeController ::create :: Created PetAttributeDto: {}", created);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	/** Update an existing attribute */
	@PutMapping("/{id}")
	public ResponseEntity<PetAttributeDto> update(@PathVariable Integer id,
			@Valid @RequestBody PetAttributeDto payload) {
		logger.info("PetAttributeController ::update ::Updating PetAttributeDto with id={}", id);
		PetAttributeDto updated = service.update(id, payload);
		logger.debug("PetAttributeController ::update :: Updated PetAttributeDto: {}", updated);
		return ResponseEntity.ok(updated);
	}

	/** Delete an attribute */
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		logger.warn("PetAttributeController ::delete :: Deleting PetAttributeDto with id={}", id);
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}