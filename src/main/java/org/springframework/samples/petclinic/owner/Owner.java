/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner;

import java.util.*;

import Utils.CollectionUtils;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.style.ToStringCreator;
import org.springframework.samples.petclinic.Pet.Pet;
import org.springframework.samples.petclinic.model.Person;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;

import static java.util.function.Predicate.not;

/**
 * Simple JavaBean domain object representing an owner.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Oliver Drotbohm
 */
@Entity
@Table(name = "owners")
@Getter
@Setter
public class Owner extends Person {

	@Column(name = "address")
	@NotBlank
	private String address;

	@Column(name = "city")
	@NotBlank
	private String city;

	@Column(name = "telephone")
	@NotBlank
	@Digits(fraction = 0, integer = 10)
	private String telephone;

	@Setter(AccessLevel.NONE)
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "owner_id")
	@OrderBy("name")
	private List<Pet> pets = new ArrayList<>();

	public void addPet(Pet pet) {
		boolean petIsNewInList = false;
		if (!CollectionUtils.isEmpty(this.pets)) {
			petIsNewInList = this.pets.stream().anyMatch(value -> value.equals(pet));
		}
		if (pet.isNewEntry() || !petIsNewInList) {
			this.pets.add(pet);
		}
	}

	/**
	 * Return the Pet with the given name, or null if none found for this Owner.
	 * @param name to test
	 * @return a pet if pet name is already in use
	 */
	public Pet getPet(String name) {
		return getPet(name, false);
	}

	/**
	 * Return the Pet with the given id, or null if none found for this Owner.
	 * @param petId to test
	 * @return a pet if pet id is already in use
	 */
	public Pet getPet(Integer petId) {
		return this.pets.stream()
			.filter(not(Pet::isNewEntry))
			.filter(pet -> pet.getId().equals(petId))
			.findFirst()
			.orElse(null);
	}

	/**
	 * Return the Pet with the given name, or null if none found for this Owner.
	 * @param petName to test
	 * @return a pet if pet name is already in use
	 */
	public Pet getPet(String petName, boolean ignoreNew) {
		return this.pets.stream()
			.filter(pet -> StringUtils.containsIgnoreCase(pet.getName(), petName))
			.filter(pet -> !ignoreNew || !pet.isNewEntry())
			.findFirst()
			.orElse(null);
	}

	/**
	 * Adds the given {@link Visit} to the {@link Pet} with the given identifier.
	 * @param petId the identifier of the {@link Pet}, must not be {@literal null}.
	 * @param visit the visit to add, must not be {@literal null}.
	 */
	public void addVisit(Integer petId, Visit visit) {
		Pet pet = Objects.requireNonNull(getPet(petId), "no pet found with given Id");
		pet.addVisit(visit);
	}

	@Override
	public String toString() {
		return new ToStringCreator(this).append("id", this.getId())
			.append("new", this.isNewEntry())
			.append("lastName", this.getLastName())
			.append("firstName", this.getFirstName())
			.append("address", this.address)
			.append("city", this.city)
			.append("telephone", this.telephone)
			.toString();
	}

}
