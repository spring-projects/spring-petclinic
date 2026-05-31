package org.springframework.samples.petclinic.core.domain.owner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Owner {

	private Integer id;

	private String firstName;

	private String lastName;

	private String address;

	private String city;

	private String telephone;

	private List<Pet> pets = new ArrayList<>();

	public boolean isNew() {
		return this.id == null;
	}

	public void addPet(Pet pet) {
		if (pet.isNew()) {
			getPets().add(pet);
		}
	}

	public Pet getPet(String name) {
		return getPet(name, false);
	}

	public Pet getPet(Integer id) {
		for (Pet pet : getPets()) {
			if (!pet.isNew()) {
				if (Objects.equals(pet.getId(), id)) {
					return pet;
				}
			}
		}
		return null;
	}

	public Pet getPet(String name, boolean ignoreNew) {
		for (Pet pet : getPets()) {
			String compName = pet.getName();
			if (compName != null && compName.equalsIgnoreCase(name)) {
				if (!ignoreNew || !pet.isNew()) {
					return pet;
				}
			}
		}
		return null;
	}

	public void addVisit(Integer petId, Visit visit) {
		if (petId == null) {
			throw new IllegalArgumentException("Pet identifier must not be null!");
		}
		if (visit == null) {
			throw new IllegalArgumentException("Visit must not be null!");
		}
		Pet pet = getPet(petId);
		if (pet == null) {
			throw new IllegalArgumentException("Invalid Pet identifier!");
		}
		pet.addVisit(visit);
	}

	@Override
	public String toString() {
		return "Owner{id=" + id + ", firstName='" + firstName + "', lastName='" + lastName + "', address='" + address
				+ "', city='" + city + "', telephone='" + telephone + "'}";
	}

}
