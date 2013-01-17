package org.springframework.samples.petclinic.repository.jdbc;

import org.springframework.samples.petclinic.Pet;

/**
 * Subclass of Pet that carries temporary id properties which
 * are only relevant for a JDBC implmentation of the ClinicService.
 *
 * @author Juergen Hoeller
 * @see JdbcClinicImpl
 */
class JdbcPet extends Pet {

	private int typeId;

	private int ownerId;


	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public int getTypeId() {
		return this.typeId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public int getOwnerId() {
		return this.ownerId;
	}

}
