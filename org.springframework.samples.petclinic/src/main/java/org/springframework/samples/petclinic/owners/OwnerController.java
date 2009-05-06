package org.springframework.samples.petclinic.owners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.util.ExternalContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/owners/{owner}")
public class OwnerController {

	private final OwnerRepository repository;

	@Autowired
	public OwnerController(OwnerRepository repository) {
		this.repository = repository;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public String get(@PathVariable Long owner, Model model) {
		model.addAttribute(repository.getOwner(owner));
		return "owner";
	}
	
	@RequestMapping(value="/edit", method=RequestMethod.GET)
	public Owner getEditForm(@PathVariable Long owner) {
		return repository.getOwner(owner);
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	public void put(Owner owner, ExternalContext response) {
		repository.saveOwner(owner);
		response.redirect(owner.getId());
	}	
	
}