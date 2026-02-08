package org.springframework.samples.petclinic.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.dto.FlagDTO;
import org.springframework.samples.petclinic.model.FeatureFlag;
import org.springframework.samples.petclinic.services.FeatureFlagService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/flags")
public class FeatureFlagController {
	@Autowired
	private FeatureFlagService service;

	@GetMapping
	public List<FeatureFlag> getAllFlags() {
		return service.getAllFlags();
	}

	@GetMapping("/{flagKey}")
	public ResponseEntity<FeatureFlag> getFlag(@PathVariable String flagKey) {
		return service.getFlag(flagKey)
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public FeatureFlag createFlag(@Valid @RequestBody FlagDTO dto) {
		return service.saveFlag(dto);
	}

	@PutMapping("/{flagKey}")
	public FeatureFlag updateFlag(@PathVariable String flagKey, @Valid @RequestBody FlagDTO dto) {
		dto.flagKey = flagKey;
		return service.saveFlag(dto);
	}

	@DeleteMapping("/{flagKey}")
	public ResponseEntity<Void> deleteFlag(@PathVariable String flagKey) {
		service.deleteFlag(flagKey);
		return ResponseEntity.noContent().build();
	}
}
