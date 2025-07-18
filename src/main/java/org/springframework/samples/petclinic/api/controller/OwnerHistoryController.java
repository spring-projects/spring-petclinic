package org.springframework.samples.petclinic.api.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.api.service.OwnerService;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController("owner-controller")
@RestControllerAdvice
@Validated
@RequestMapping("/api/owner")
public class OwnerHistoryController {

	@Autowired
	private OwnerService ownerService;

	@PostMapping
	public void addOwner(@Valid @RequestBody Owner owner) {
		ownerService.addOwner(owner);
	}

	@GetMapping
	public String getOwner() {
		return "API working";
	}

}
