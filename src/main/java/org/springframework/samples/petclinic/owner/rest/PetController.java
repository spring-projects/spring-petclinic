package org.springframework.samples.petclinic.owner.rest;

import java.util.Collection;

import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.PetRepository;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 
 * @author Awadhesh Kumar
 *
 */
@RestController("PetController")
@RequestMapping("/api/owners/{ownerid}/pets")
public class PetController {
	private final PetRepository pets;

	private final OwnerRepository owners;

	public PetController(PetRepository pets, OwnerRepository owners) {
		this.pets = pets;
		this.owners = owners;
	}
	
	@GetMapping("/types")
	public Collection<PetType> populatePetTypes() {
		return this.pets.findPetTypes();
	}
	
	
}
