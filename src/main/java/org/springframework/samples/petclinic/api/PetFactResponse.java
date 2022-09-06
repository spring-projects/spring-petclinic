package org.springframework.samples.petclinic.api;

public class PetFactResponse {
	private String catFact;
	private String dogFact;

	public PetFactResponse(String catFact, String dogFact) {
		this.catFact = catFact;
		this.dogFact = dogFact;
	}

	public String getCatFact() {
		return catFact;
	}

	public void setCatFact(String catFact) {
		this.catFact = catFact;
	}

	public String getDogFact() {
		return dogFact;
	}

	public void setDogFact(String dogFact) {
		this.dogFact = dogFact;
	}
}
