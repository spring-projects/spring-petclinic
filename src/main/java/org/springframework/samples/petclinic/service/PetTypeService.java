package org.springframework.samples.petclinic.service;

import org.springframework.samples.petclinic.model.PetTypes;

import java.util.List;

public interface PetTypeService {
	PetTypes save(PetTypes petType);
	List<PetTypes> findAll();
}
