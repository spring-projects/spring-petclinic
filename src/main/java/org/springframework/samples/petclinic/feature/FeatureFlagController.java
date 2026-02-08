package org.springframework.samples.petclinic.feature;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feature-flags")
public class FeatureFlagController {

	private final FeatureFlagService service;

	public FeatureFlagController(FeatureFlagService service) {
		this.service = service;
	}

	@GetMapping
	public List<FeatureFlag> getAll() {
		return service.findAll();
	}

	@GetMapping("/{id}")
	public FeatureFlag get(@PathVariable Integer id) {
		return service.findById(id).orElseThrow();
	}

	@PostMapping
	public FeatureFlag create(@RequestBody FeatureFlag flag) {
		return service.save(flag);
	}

	@PutMapping("/{id}")
	public FeatureFlag update(@PathVariable Integer id, @RequestBody FeatureFlag flag) {
		flag.setId(id);
		return service.save(flag);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Integer id) {
		service.deleteById(id);
	}

}
