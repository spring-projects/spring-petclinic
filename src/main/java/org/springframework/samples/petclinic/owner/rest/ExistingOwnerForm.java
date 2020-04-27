package org.springframework.samples.petclinic.owner.rest;

import org.springframework.samples.petclinic.owner.Owner;

/**
 * 
 * @author Awadhesh Kumar
 *
 */
public class ExistingOwnerForm extends NewOwnerForm{
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public Owner NewOwner() {
		Owner owner=super.NewOwner();
		owner.setId(this.id);
		return owner;
	}
}
