/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.system;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.model.FeatureFlag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feature-flags")
public class FeatureFlagController {

	private final FeatureFlagRepository featureFlagRepository;

	public FeatureFlagController(FeatureFlagRepository featureFlagRepository) {
		this.featureFlagRepository = featureFlagRepository;
	}

	@GetMapping
	public ResponseEntity<List<FeatureFlag>> getAllFlags() {
		List<FeatureFlag> flags = (List<FeatureFlag>) featureFlagRepository.findAll();
		return ResponseEntity.ok(flags);
	}

	@PostMapping
	public ResponseEntity<FeatureFlag> createFlag(@RequestBody FeatureFlag featureFlag) {
		FeatureFlag saved = featureFlagRepository.save(featureFlag);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<FeatureFlag> updateFlag(@PathVariable Integer id, @RequestBody FeatureFlag featureFlag) {
		if (!featureFlagRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		featureFlag.setId(id);
		FeatureFlag updated = featureFlagRepository.save(featureFlag);
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteFlag(@PathVariable Integer id) {
		if (!featureFlagRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		featureFlagRepository.findById(id).ifPresent(flag -> {
			flag.getBlacklistedUsers().clear();
			flag.getWhitelistedUsers().clear();
			featureFlagRepository.delete(flag);
		});
		return ResponseEntity.noContent().build();
	}

}
