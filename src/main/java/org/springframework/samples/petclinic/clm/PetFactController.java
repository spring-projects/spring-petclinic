package org.springframework.samples.petclinic.clm;

import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.api.PetFactResponse;
import org.springframework.samples.petclinic.api.PetFactService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PetFactController {
	private final PetFactService petFactService;

	public PetFactController(PetFactService petFactService) {
		this.petFactService = petFactService;
	}

	@GetMapping(value = "/clm/facts", produces = MediaType.APPLICATION_JSON_VALUE)
	public PetFactResponse getPetFacts() {
		return petFactService.getPetFacts();
	}
}
