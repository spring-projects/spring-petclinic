package org.springframework.samples.petclinic.owner.rest;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;

import org.springframework.samples.petclinic.owner.Owner;
/**
 * 
 * @author Awadhesh Kumar
 *
 */
public class NewOwnerForm {
	@NotEmpty
	private String firstName;
	@NotEmpty
	private String lastName;
	@NotEmpty
	private String address;
	@NotEmpty
	private String city;
	@NotEmpty
	@Digits(fraction = 0, integer = 10)
	private String telephone;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Owner NewOwner() {
		Owner owner=new Owner();
		owner.setFirstName(this.firstName);
		owner.setLastName(this.lastName);
		owner.setAddress(this.address);
		owner.setCity(this.city);
		owner.setTelephone(this.telephone);
		return owner;
	}
}
