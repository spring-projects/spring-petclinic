package org.springframework.samples.petclinic.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.web.bind.annotation.*;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.api.dto.OwnerDto;
import org.springframework.samples.petclinic.api.mapper.OwnerMapper;


import java.util.Optional;

@RestController
@RequestMapping("/api/owners")

public class OwnerRestController {
	private final OwnerRepository ownerRepository;
	private final OwnerMapper ownerMapper;

	@Autowired
	public OwnerRestController(OwnerRepository ownerRepository, OwnerMapper ownerMapper) {
		this.ownerRepository = ownerRepository;
		this.ownerMapper = ownerMapper;
	}

	@GetMapping("/{id}")
	public ResponseEntity<OwnerDto> getOwner(@PathVariable("id") int id) {
		Optional<Owner> ownerOpt = ownerRepository.findById(id);
		if (ownerOpt.isPresent()) {
			Owner own = ownerOpt.get();
			OwnerDto dto = ownerMapper.toDto(own);
			return ResponseEntity.ok(dto);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	public ResponseEntity<OwnerDto> createOwner(@RequestBody OwnerDto ownerDto) {
		Owner owner = ownerMapper.toEntity(ownerDto);
		Owner savedOwner = ownerRepository.save(owner);
		return ResponseEntity.status(HttpStatus.CREATED).body(ownerMapper.toDto(savedOwner));
	}
}
