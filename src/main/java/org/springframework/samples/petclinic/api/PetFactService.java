package org.springframework.samples.petclinic.api;

import org.springframework.samples.petclinic.api.client.CatFactClient;
import org.springframework.samples.petclinic.api.client.DogFactClient;
import org.springframework.stereotype.Service;

@Service
public class PetFactService {
	private final CatFactClient catFactClient;
	private final DogFactClient dogFactClient;

	public PetFactService(CatFactClient catFactClient, DogFactClient dogFactClient) {
		this.catFactClient = catFactClient;
		this.dogFactClient = dogFactClient;
	}

	public PetFactResponse getPetFacts() {
		final var catFact = catFactClient.fetchCatFact();
		final var dogFact = dogFactClient.fetchDogFact();
		return new PetFactResponse(catFact.getFact(), dogFact.getFacts().get(0));
	}

}
