package org.springframework.samples.petclinic.owner.rest;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 
 * @author Awadhesh Kumar
 *
 */
@RestController("OwnerRestController")
@RequestMapping("/api/owner")
public class OwnerController {
	private final OwnerRepository owners;

	private VisitRepository visits;

	public OwnerController(OwnerRepository clinicService, VisitRepository visits) {
		this.owners = clinicService;
		this.visits = visits;
	}
	
	@PostMapping("/new")
	public ResponseEntity<Object> createOwner(@Valid @RequestBody NewOwnerForm owner, BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<Object>(result.getAllErrors(),HttpStatus.BAD_REQUEST);
		} else {
			createNewOwner(owner);
			return new ResponseEntity<Object>("Owner created",HttpStatus.OK);
		}
	}

	private void createNewOwner(final NewOwnerForm owner) {
		Owner newOwner=owner.NewOwner();
		this.owners.save(newOwner);
	}
}
