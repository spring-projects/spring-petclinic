package org.springframework.samples.petclinic.pet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.util.ResponseContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/owners/{owner}/pets/{pet}")
public class PetController {

	private final PetRepository repository;

	@Autowired
	public PetController(PetRepository repository) {
		this.repository = repository;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public Pet get(Long owner, String pet) {
		return repository.getPet(owner, pet);
	}
	
	@RequestMapping(value="/edit", method=RequestMethod.GET)
	public Pet getEditForm(Long owner, String pet) {
		return repository.getPet(owner, pet);
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	public void put(Pet pet, ResponseContext response) {
		repository.savePet(pet);
		response.redirect(pet.getName());
	}
	
	@RequestMapping(method = RequestMethod.DELETE)
	public void delete(Long owner, String pet, ResponseContext context) {
		context.forResource("owners").redirect(owner);
	}
	
}