package org.springframework.samples.petclinic.clm;

import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.api.CatFact;
import org.springframework.samples.petclinic.api.CatFactClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CatController {
	private final CatFactClient catFactClient;

	public CatController(CatFactClient catFactClient) {
		this.catFactClient = catFactClient;
	}

	@GetMapping(value = "/clm/cat", produces = MediaType.APPLICATION_JSON_VALUE)
	public CatFact http() {
		return catFactClient.fetchFact();
	}
}
