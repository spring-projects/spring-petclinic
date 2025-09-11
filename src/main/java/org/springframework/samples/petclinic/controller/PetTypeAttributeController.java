package org.springframework.samples.petclinic.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.dto.PetAttributeDto;
import org.springframework.samples.petclinic.model.PetTypeAttribute;
import org.springframework.samples.petclinic.service.PetTypeAttributeService;
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
 * Purpose: Provides CRUD endpoints under /api/pet-types-attributes.
 * @Author: Sujeet Sharma
 * @Date: 08--9-2025
 */

@RestController
//@RequestMapping("/api/pet-type-attributes")

@RequestMapping("/v1/api/pet")
public class PetTypeAttributeController {

	private final PetTypeAttributeService service;
	private static final Logger logger = LoggerFactory.getLogger(PetTypeAttributeController.class);

	public PetTypeAttributeController(PetTypeAttributeService service) {
		this.service = service;
	}

	/** Get attribute by ID */
	@GetMapping("/{id}")
	public ResponseEntity<PetAttributeDto> getById(@PathVariable Integer id) {
		logger.info("Fetching PetAttributeDto with id={}", id);
		PetAttributeDto dto = service.getById(id);
		logger.debug("Fetched PetAttributeDto: {}", dto);
		return ResponseEntity.ok(dto);
	}

	/** Get all attributes for a given PetType */
	@GetMapping("/type/{typeId}")
	public ResponseEntity<List<PetAttributeDto>> getByTypeId(@PathVariable Integer typeId) {
		logger.info("Fetching all PetAttributeDto for PetType id={}", typeId);
		List<PetAttributeDto> attributes = service.getByTypeId(typeId);
		logger.debug("Fetched {} PetAttributeDto for PetType id={}", attributes.size(), typeId);
		return ResponseEntity.ok(attributes);
	}

	// write AOP for logger

	/** Create a new attribute for a PetType */
	@PostMapping("/{typeId}")
	public ResponseEntity<PetAttributeDto> create(@PathVariable Integer typeId,
			@Valid @RequestBody PetAttributeDto attrDto) {
		logger.info("Creating PetAttributeDto for PetType id={}", typeId);
		PetAttributeDto created = service.create(typeId, attrDto);
		logger.debug("Created PetAttributeDto: {}", created);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	/** Update an existing attribute */
	@PutMapping("/{id}")
	public ResponseEntity<PetAttributeDto> update(@PathVariable Integer id,
			@Valid @RequestBody PetAttributeDto payload) {
		logger.info("Updating PetAttributeDto with id={}", id);
		PetAttributeDto updated = service.update(id, payload);
		logger.debug("Updated PetAttributeDto: {}", updated);
		return ResponseEntity.ok(updated);
	}

	/** Delete an attribute */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		logger.warn("Deleting PetAttributeDto with id={}", id);
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}