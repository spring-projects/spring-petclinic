/*
 * Copyright 2012-2026 the original author or authors.
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * Unit tests for {@link Owner} (pet lookup, visits and {@code toString}). Complements the
 * upstream {@link OwnerTests}.
 */
class OwnerTest {

	private Owner owner;

	private Pet savedPet;

	private Pet newPet;

	@BeforeEach
	void setUp() {
		owner = new Owner();
		owner.setFirstName("George");
		owner.setLastName("Franklin");
		owner.setAddress("110 W. Liberty St.");
		owner.setCity("Madison");
		owner.setTelephone("6085551023");

		savedPet = pet(7, "Leo");
		newPet = pet(null, "Max");
		owner.addPet(savedPet);
		owner.addPet(newPet);
	}

	private static Pet pet(Integer id, String name) {
		Pet pet = new Pet();
		pet.setId(id);
		pet.setName(name);
		return pet;
	}

	@Test
	void keepsContactData() {
		assertThat(owner.getAddress()).isEqualTo("110 W. Liberty St.");
		assertThat(owner.getCity()).isEqualTo("Madison");
		assertThat(owner.getTelephone()).isEqualTo("6085551023");
	}

	@Test
	void addPetIgnoresNull() {
		owner.addPet(null);

		assertThat(owner.getPets()).containsExactly(savedPet, newPet);
	}

	@Test
	void addPetIgnoresDifferentInstanceWithSameId() {
		owner.addPet(pet(7, "Leo copy"));

		assertThat(owner.getPets()).hasSize(2);
	}

	@Test
	void addPetAcceptsSeveralNewPets() {
		owner.addPet(pet(null, "Rex"));

		assertThat(owner.getPets()).hasSize(3);
	}

	@Test
	void getPetByNameIsCaseInsensitive() {
		assertThat(owner.getPet("leo")).isSameAs(savedPet);
		assertThat(owner.getPet("MAX")).isSameAs(newPet);
	}

	@Test
	void getPetByNameReturnsNullWhenNotFound() {
		assertThat(owner.getPet("Garfield")).isNull();
	}

	@Test
	void getPetByNameCanIgnoreNewPets() {
		assertThat(owner.getPet("Max", true)).isNull();
		assertThat(owner.getPet("Leo", true)).isSameAs(savedPet);
	}

	@Test
	void getPetByIdFindsOnlySavedPets() {
		assertThat(owner.getPet(7)).isSameAs(savedPet);
		assertThat(owner.getPet(99)).isNull();
		assertThat(owner.getPet((Integer) null)).isNull();
	}

	@Test
	void addVisitAddsVisitToThePet() {
		Visit visit = new Visit();
		visit.setDescription("rabies shot");

		owner.addVisit(7, visit);

		assertThat(savedPet.getVisits()).containsExactly(visit);
	}

	@Test
	void addVisitRejectsNullPetId() {
		assertThatIllegalArgumentException().isThrownBy(() -> owner.addVisit(null, new Visit()))
			.withMessage("Pet identifier must not be null!");
	}

	@Test
	void addVisitRejectsNullVisit() {
		assertThatIllegalArgumentException().isThrownBy(() -> owner.addVisit(7, null))
			.withMessage("Visit must not be null!");
	}

	@Test
	void addVisitRejectsUnknownPet() {
		assertThatIllegalArgumentException().isThrownBy(() -> owner.addVisit(99, new Visit()))
			.withMessage("Invalid Pet identifier!");
	}

	@Test
	void toStringContainsOwnerData() {
		owner.setId(1);

		assertThat(owner.toString()).contains("id = 1", "new = false", "lastName = 'Franklin'", "firstName = 'George'",
				"city = 'Madison'", "telephone = '6085551023'");
	}

}
