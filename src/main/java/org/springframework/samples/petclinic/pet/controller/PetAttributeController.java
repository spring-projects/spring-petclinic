package org.springframework.samples.petclinic.pet.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.pet.model.PetAttribute;
import org.springframework.samples.petclinic.pet.service.PetAttributeService;
import org.springframework.samples.petclinic.pet.service.PetTypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Rohit Lalwani
 */
@RestController
@RequestMapping("/api/petTypes/{typeId}/attributes")
public class PetAttributeController {

	private final PetTypeService petTypeService;

	private final PetAttributeService petAttributeService;

	private final PetAttributeModelAssembler assembler;

	public PetAttributeController(PetTypeService petTypeService, PetAttributeService attrService,
			PetAttributeModelAssembler assembler) {
		this.petTypeService = petTypeService;
		this.petAttributeService = attrService;
		this.assembler = assembler;
	}

	@PostMapping
	public ResponseEntity<EntityModel<PetAttribute>> createAttribute(@PathVariable Integer typeId,
			@RequestBody PetAttribute attr) {
		PetType type = petTypeService.findPetTypeById(typeId);
		if (type == null)
			return ResponseEntity.notFound().build();

		attr.setPetType(type);
		PetAttribute saved = petAttributeService.save(attr);

		return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(saved));
	}

	@GetMapping
	public ResponseEntity<CollectionModel<EntityModel<PetAttribute>>> getAttributes(@PathVariable Integer typeId) {
		List<PetAttribute> attrs = petAttributeService.findByPetTypeId(typeId);

		List<EntityModel<PetAttribute>> attrModels = attrs.stream().map(assembler::toModel).toList();

		return ResponseEntity.ok(CollectionModel.of(attrModels,
				linkTo(methodOn(PetAttributeController.class).getAttributes(typeId)).withSelfRel()));
	}

}
