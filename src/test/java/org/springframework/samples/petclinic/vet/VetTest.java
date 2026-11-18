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
package org.springframework.samples.petclinic.vet;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link Vet} specialties. Complements the upstream {@link VetTests}
 * (serialization).
 */
class VetTest {

	private static Specialty specialty(String name) {
		Specialty specialty = new Specialty();
		specialty.setName(name);
		return specialty;
	}

	@Test
	void newVetHasNoSpecialties() {
		Vet vet = new Vet();

		assertThat(vet.getSpecialties()).isEmpty();
		assertThat(vet.getNrOfSpecialties()).isZero();
	}

	@Test
	void specialtiesAreSortedByName() {
		Vet vet = new Vet();
		vet.addSpecialty(specialty("surgery"));
		vet.addSpecialty(specialty("dentistry"));
		vet.addSpecialty(specialty("radiology"));

		assertThat(vet.getSpecialties()).extracting(Specialty::getName)
			.containsExactly("dentistry", "radiology", "surgery");
		assertThat(vet.getNrOfSpecialties()).isEqualTo(3);
	}

	@Test
	void addingSameSpecialtyTwiceCountsOnce() {
		Vet vet = new Vet();
		Specialty surgery = specialty("surgery");

		vet.addSpecialty(surgery);
		vet.addSpecialty(surgery);

		assertThat(vet.getNrOfSpecialties()).isEqualTo(1);
	}

	@Test
	void getSpecialtiesReturnsCopy() {
		Vet vet = new Vet();
		vet.addSpecialty(specialty("surgery"));

		vet.getSpecialties().clear();

		assertThat(vet.getNrOfSpecialties()).isEqualTo(1);
	}

}
