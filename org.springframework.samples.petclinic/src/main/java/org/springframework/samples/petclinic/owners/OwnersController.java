package org.springframework.samples.petclinic.owners;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/owners")
public class OwnersController {

	private final OwnerRepository repository;

	@Autowired
	public OwnersController(OwnerRepository repository) {
		this.repository = repository;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public OwnerSearchForm get() {
		return new OwnerSearchForm();
	}
	
	@RequestMapping(value="/search", method = RequestMethod.GET)
	public Collection<Owner> getSearchResults(@RequestParam String lastName) {
		return repository.findOwnersByLastName(lastName);
	}
	
	@RequestMapping(value="/new", method = RequestMethod.GET)
	public Owner getNewForm() {
		return new Owner();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String post(Owner owner) {
		Long ownerId = repository.saveOwner(owner);
		return "redirect:/owners/" + ownerId;
	}	

}
