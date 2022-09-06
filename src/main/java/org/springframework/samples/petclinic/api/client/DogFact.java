package org.springframework.samples.petclinic.api.client;

import java.util.List;

public class DogFact {
	private List<String> facts;

	public DogFact() {
	}

	public DogFact(List<String> facts) {
		this.facts = facts;
	}

	public List<String> getFacts() {
		return facts;
	}

	public void setFacts(List<String> facts) {
		this.facts = facts;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DogFact dogFact = (DogFact) o;

		return facts.equals(dogFact.facts);
	}

	@Override
	public int hashCode() {
		return facts.hashCode();
	}
}
