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
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * SWE 472 course project - Input Space Partitioning tests for
 * {@link Owner#getPet(String, boolean)}.
 * <p>
 * Test IDs ISP-G-TC01 to ISP-G-TC13 trace to Table VIII of the project report. Base
 * choice: an owner with two saved pets, Max (id 1) and Bella (id 2), searched for "Max"
 * with ignoreNew = false.
 *
 * @author Amir Khalifa
 */
@DisplayName("SWE472 ISP - Owner.getPet(String, boolean)")
class Swe472OwnerGetPetIspTests {

	private Owner owner;

	private Pet max;

	private Pet bella;

	private static Pet pet(String name, Integer id) {
		Pet pet = new Pet();
		pet.setName(name);
		pet.setId(id);
		return pet;
	}

	@BeforeEach
	void setUp() {
		this.owner = new Owner();
		this.max = pet("Max", 1);
		this.bella = pet("Bella", 2);
		this.owner.addPet(this.max);
		this.owner.addPet(this.bella);
	}

	@Test
	@DisplayName("ISP-G-TC01 a3 b2 c1 d3 e2 (base) getPet(\"Max\", false) -> Max")
	void tc01_base() {
		assertThat(this.owner.getPet("Max", false)).isSameAs(this.max);
	}

	@Test
	@DisplayName("ISP-G-TC02 a1 b1 e0 getPet(null, false) -> null")
	void tc02_nullName() {
		assertThat(this.owner.getPet(null, false)).isNull();
	}

	@Test
	@DisplayName("ISP-G-TC03 a2 b1 e0 getPet(\"\", false) -> null")
	void tc03_emptyName() {
		assertThat(this.owner.getPet("", false)).isNull();
	}

	@Test
	@DisplayName("ISP-G-TC04 b1 e0 getPet(\"Rex\", false) -> null")
	void tc04_unknownName() {
		assertThat(this.owner.getPet("Rex", false)).isNull();
	}

	@Test
	@DisplayName("ISP-G-TC05 b3 getPet(\"mAX\", false) -> Max (case ignored)")
	void tc05_differentCase() {
		assertThat(this.owner.getPet("mAX", false)).isSameAs(this.max);
	}

	@Test
	@DisplayName("ISP-G-TC06 c2 getPet(\"Max\", true) -> Max (saved pet not skipped)")
	void tc06_ignoreNewTrue() {
		assertThat(this.owner.getPet("Max", true)).isSameAs(this.max);
	}

	@Test
	@DisplayName("ISP-G-TC07 d1 b1 e0 owner without pets -> null")
	void tc07_noPets() {
		assertThat(new Owner().getPet("Max", false)).isNull();
	}

	@Test
	@DisplayName("ISP-G-TC08 d2 owner with only Max -> Max")
	void tc08_onePet() {
		Owner single = new Owner();
		Pet onlyMax = pet("Max", 1);
		single.addPet(onlyMax);
		assertThat(single.getPet("Max", false)).isSameAs(onlyMax);
	}

	@Test
	@DisplayName("ISP-G-TC09 e1 new pet Rocky, getPet(\"Rocky\", false) -> Rocky")
	void tc09_newPetFound() {
		Pet rocky = pet("Rocky", null);
		this.owner.addPet(rocky);
		assertThat(this.owner.getPet("Rocky", false)).isSameAs(rocky);
	}

	@Test
	@DisplayName("ISP-G-TC10 e1 c2 new pet Rocky, getPet(\"Rocky\", true) -> null")
	void tc10_newPetIgnored() {
		this.owner.addPet(pet("Rocky", null));
		assertThat(this.owner.getPet("Rocky", true)).isNull();
	}

	@Test
	@DisplayName("ISP-G-TC11 robustness: pet with null name is skipped, no exception")
	void tc11_petWithNullName() {
		Owner withNullName = new Owner();
		withNullName.addPet(pet(null, 5));
		Pet onlyMax = pet("Max", 1);
		withNullName.addPet(onlyMax);

		assertThat(withNullName.getPet("Max", false)).isSameAs(onlyMax);
		assertThatNoException().isThrownBy(() -> withNullName.getPet(null, false));
		assertThat(withNullName.getPet(null, false)).isNull();
	}

	@Test
	@DisplayName("ISP-G-TC12 x2 flag excludes new pet Rocky, getPet(\"Max\", true) -> Max")
	void tc12_flagExcludesOtherPet() {
		this.owner.addPet(pet("Rocky", null));
		assertThat(this.owner.getPet("Max", true)).isSameAs(this.max);
	}

	@Test
	@DisplayName("ISP-G-TC13 x2 new \"Luna\" before saved \"LUNA\": true -> saved, false -> new")
	void tc13_flagSkipsNewMatchAndContinues() {
		Owner luna = new Owner();
		Pet newLuna = pet("Luna", null);
		Pet savedLuna = pet("LUNA", 9);
		luna.addPet(newLuna);
		luna.addPet(savedLuna);

		assertThat(luna.getPet("luna", true)).isSameAs(savedLuna);
		assertThat(luna.getPet("luna", false)).isSameAs(newLuna);
	}

}
