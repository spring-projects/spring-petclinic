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

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link Pet}.
 */
class PetTest {

	@Test
	void newPetHasNoVisits() {
		assertThat(new Pet().getVisits()).isEmpty();
	}

	@Test
	void keepsBirthDateAndType() {
		PetType cat = new PetType();
		cat.setName("cat");
		Pet pet = new Pet();
		pet.setName("Leo");
		pet.setBirthDate(LocalDate.of(2020, 9, 7));
		pet.setType(cat);

		assertThat(pet.getName()).isEqualTo("Leo");
		assertThat(pet.getBirthDate()).isEqualTo(LocalDate.of(2020, 9, 7));
		assertThat(pet.getType()).isSameAs(cat);
		assertThat(pet.getType()).hasToString("cat");
	}

	@Test
	void addVisitKeepsInsertionOrder() {
		Pet pet = new Pet();
		Visit first = new Visit();
		Visit second = new Visit();

		pet.addVisit(first);
		pet.addVisit(second);

		assertThat(pet.getVisits()).containsExactly(first, second);
	}

	@Test
	void addVisitDoesNotDuplicateSameVisit() {
		Pet pet = new Pet();
		Visit visit = new Visit();

		pet.addVisit(visit);
		pet.addVisit(visit);

		assertThat(pet.getVisits()).hasSize(1);
	}

}
