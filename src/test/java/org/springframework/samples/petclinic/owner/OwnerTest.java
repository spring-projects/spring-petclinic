/*
 * Copyright 2012-2025 the original author or authors.
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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Test class for {@link Owner}
 *
 * @author Andrew Huebner
 */
class OwnerTest {

	@Test
	void shouldCreateOwner() {
		Owner owner = new Owner();
		assertThat(owner).isNotNull();
	}

	@Test
	void shouldSetAddress() {
		Owner owner = new Owner();
		owner.setAddress("123 Main St");
		assertThat(owner.getAddress()).isEqualTo("123 Main St");
	}

	@Test
	void shouldSetCity() {
		Owner owner = new Owner();
		owner.setCity("Anytown");
		assertThat(owner.getCity()).isEqualTo("Anytown");
	}

	@Test
	void shouldSetTelephone() {
		Owner owner = new Owner();
		owner.setTelephone("1234567890");
		assertThat(owner.getTelephone()).isEqualTo("1234567890");
	}

	@Test
	void shouldHaveEmptyPetsInitially() {
		Owner owner = new Owner();
		assertThat(owner.getPets()).isEmpty();
	}

	@Test
	void shouldAddPet() {
		Owner owner = new Owner();
		Pet pet = new Pet();
		pet.setName("Fluffy");
		owner.addPet(pet);
		assertThat(owner.getPets()).contains(pet);
	}

	@Test
	void shouldNotAddPetIfNotNew() {
		Owner owner = new Owner();
		Pet pet = new Pet();
		pet.setId(1);
		owner.addPet(pet);
		assertThat(owner.getPets()).doesNotContain(pet);
	}

	@Test
	void shouldGetPetByName() {
		Owner owner = new Owner();
		Pet pet = new Pet();
		pet.setName("Fluffy");
		owner.addPet(pet);
		assertThat(owner.getPet("Fluffy")).isEqualTo(pet);
	}

	@Test
	void shouldGetPetById() {
		Owner owner = new Owner();
		Pet pet = new Pet();
		pet.setId(1);
		pet.setName("Fluffy");
		owner.addPet(pet);
		assertThat(owner.getPet(1)).isEqualTo(pet);
	}

	@Test
	void shouldReturnNullForNonExistentPet() {
		Owner owner = new Owner();
		assertThat(owner.getPet("NonExistent")).isNull();
		assertThat(owner.getPet(999)).isNull();
	}

	@Test
	void shouldAddVisitToPet() {
		Owner owner = new Owner();
		Pet pet = new Pet();
		pet.setId(1);
		pet.setName("Fluffy");
		owner.addPet(pet);
		Visit visit = new Visit();
		owner.addVisit(1, visit);
		assertThat(pet.getVisits()).contains(visit);
	}

}