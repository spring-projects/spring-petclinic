package org.springframework.samples.petclinic.featureflag.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.featureflag.entity.FeatureFlag;
import org.springframework.samples.petclinic.featureflag.service.FeatureFlagService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flags")
public class FeatureFlagController {

	@Autowired
	private FeatureFlagService service;

	@PostMapping
	public ResponseEntity<FeatureFlag> createFeatureFlag(@RequestBody FeatureFlag flag) {
		FeatureFlag createdFlag = service.createFeatureFlag(flag);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdFlag);
	}

	@GetMapping
	public List<FeatureFlag> fetchAllFeatureFlags() {
		return service.fetchAllFeatureFlags();
	}

	@GetMapping("/{name}")
	public FeatureFlag getFeatureFlagByName(@PathVariable String name) {
		return service.getFeatureFlagByName(name);
	}

	@PutMapping("/{id}")
	public ResponseEntity<FeatureFlag> updateFeatureFlag(@PathVariable Long id, @RequestBody FeatureFlag flag) {
		return service.updateFeatureFlag(id, flag).map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteFeatureFlag(@PathVariable Long id) {
		service.deleteFeatureFlag(id);
		return ResponseEntity.noContent().build();
	}

}
