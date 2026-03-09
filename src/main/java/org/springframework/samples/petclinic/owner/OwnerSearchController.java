package org.springframework.samples.petclinic.owner;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OwnerSearchController {

	private final OwnerSearchService searchService;

	public OwnerSearchController(OwnerSearchService searchService) {
		this.searchService = searchService;
	}

	@GetMapping("/api/owners/search")
	public List<Owner> searchOwners(@RequestParam String query) {
		return searchService.searchOwners(query);
	}

}
