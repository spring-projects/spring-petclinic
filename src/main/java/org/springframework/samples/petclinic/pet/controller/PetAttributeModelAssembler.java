package org.springframework.samples.petclinic.pet.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.samples.petclinic.pet.model.PetAttribute;
import org.springframework.stereotype.Component;

/**
 * @author Rohit Lalwani
 */
@Component
public class PetAttributeModelAssembler
		implements RepresentationModelAssembler<PetAttribute, EntityModel<PetAttribute>> {

	@Override
	public EntityModel<PetAttribute> toModel(PetAttribute attr) {
		Integer typeId = attr.getPetType().getId();
		return EntityModel.of(attr,
				linkTo(methodOn(PetAttributeController.class).getAttributes(typeId)).withRel("allAttributes"),
				linkTo(methodOn(PetAttributeController.class).createAttribute(typeId, attr))
					.withRel("createAttribute"));
	}

}
