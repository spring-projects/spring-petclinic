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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * SWE 472 course project - graph coverage tests for {@link Owner#addPet(Pet)}.
 * <p>
 * CFG nodes: 1 pet == null, 2 return, 3 contains(pet), 4 return, 5 loop test, 6 saved-id
 * decision, 7 return, 8 add, 9 exit. Each test executes the test path in its name (Table
 * XI of the project report).
 * <ul>
 * <li>Node and edge coverage: P1, P2, P4, P5</li>
 * <li>Prime path coverage: P1, P2, P3, P5, P6</li>
 * </ul>
 *
 * @author Samuel Alene
 */
@DisplayName("SWE472 Graph - Owner.addPet")
class Swe472OwnerAddPetGraphTests {

	private Owner owner;

	private static Pet pet(String name, Integer id) {
		Pet pet = new Pet();
		pet.setName(name);
		pet.setId(id);
		return pet;
	}

	@BeforeEach
	void setUp() {
		this.owner = new Owner();
	}

	@Test
	@DisplayName("CFG-A-P1 [1,2,9] null pet -> ignored")
	void cfgA_p1_nullPet() {
		this.owner.addPet(null);
		assertThat(this.owner.getPets()).isEmpty();
	}

	@Test
	@DisplayName("CFG-A-P2 [1,3,4,9] same object added twice -> stored once")
	void cfgA_p2_sameObject() {
		Pet max = pet("Max", 1);
		this.owner.getPets().add(max);

		this.owner.addPet(max);

		assertThat(this.owner.getPets()).containsExactly(max);
	}

	@Test
	@DisplayName("CFG-A-P3 [1,3,5,8,9] empty owner, loop runs zero times -> added")
	void cfgA_p3_firstPet() {
		Pet max = pet("Max", 1);

		this.owner.addPet(max);

		assertThat(this.owner.getPets()).containsExactly(max);
	}

	@Test
	@DisplayName("CFG-A-P4 [1,3,5,6,5,8,9] saved pet, other saved id -> added")
	void cfgA_p4_differentId() {
		Pet max = pet("Max", 1);
		Pet bella = pet("Bella", 2);
		this.owner.addPet(max);

		this.owner.addPet(bella);

		assertThat(this.owner.getPets()).containsExactly(max, bella);
	}

	@Test
	@DisplayName("CFG-A-P4b [1,3,5,6,5,8,9] existing pet is new (clause 1 false) -> added")
	void cfgA_p4b_existingPetNew() {
		Pet max = pet("Max", null);
		Pet bella = pet("Bella", 2);
		this.owner.addPet(max);

		this.owner.addPet(bella);

		assertThat(this.owner.getPets()).containsExactly(max, bella);
	}

	@Test
	@DisplayName("CFG-A-P4c [1,3,5,6,5,8,9] added pet is new (clause 2 false) -> added")
	void cfgA_p4c_addedPetNew() {
		Pet max = pet("Max", 1);
		Pet bella = pet("Bella", null);
		this.owner.addPet(max);

		this.owner.addPet(bella);

		assertThat(this.owner.getPets()).containsExactly(max, bella);
	}

	@Test
	@DisplayName("CFG-A-P5 [1,3,5,6,7,9] different object with the same saved id -> rejected")
	void cfgA_p5_sameSavedId() {
		Pet max = pet("Max", 1);
		Pet copy = pet("Copy of Max", 1);
		this.owner.addPet(max);

		this.owner.addPet(copy);

		assertThat(this.owner.getPets()).hasSize(1);
		assertThat(this.owner.getPets().get(0)).isSameAs(max);
	}

	@Test
	@DisplayName("CFG-A-P6 [1,3,5,6,5,6,5,8,9] two saved pets, third id -> added (loop twice)")
	void cfgA_p6_loopTwice() {
		Pet max = pet("Max", 1);
		Pet bella = pet("Bella", 2);
		Pet rocky = pet("Rocky", 3);
		this.owner.addPet(max);
		this.owner.addPet(bella);

		this.owner.addPet(rocky);

		assertThat(this.owner.getPets()).containsExactly(max, bella, rocky);
	}

	@Test
	@DisplayName("CFG-A-P7 [1,3,5,6,5,6,7,9] id of the second saved pet -> rejected")
	void cfgA_p7_sameIdAsSecondPet() {
		Pet max = pet("Max", 1);
		Pet bella = pet("Bella", 2);
		this.owner.addPet(max);
		this.owner.addPet(bella);

		this.owner.addPet(pet("Copy of Bella", 2));

		assertThat(this.owner.getPets()).containsExactly(max, bella);
	}

}
