package org.springframework.samples.petclinic.owner.rest;

public class ExistingOwnerForm extends NewOwnerForm{
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
