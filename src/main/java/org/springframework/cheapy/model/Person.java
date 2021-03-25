package org.springframework.cheapy.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotEmpty;

@MappedSuperclass
public class Person extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "first_name")
	@NotEmpty
	private String firstName;

	@Column(name = "last_name")
	@NotEmpty
	private String lastName;

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
