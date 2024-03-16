package org.springframework.samples.petclinic.api;

import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OwnerApiController {

	private final OwnerRepository ownerRepository;

	public OwnerApiController(OwnerRepository ownerRepository) {
		this.ownerRepository = ownerRepository;
	}

	@GetMapping("/api/owners")
	List<Owner> searchOwnersBySurname(@RequestParam String lastname) {
		return ownerRepository.findAllByLastName(lastname);
	}

}
