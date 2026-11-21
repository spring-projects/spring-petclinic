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

package org.springframework.samples.petclinic;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.predicate.RuntimeHintsPredicates;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.model.Person;
import org.springframework.samples.petclinic.vet.Vet;

/**
 * Unit test: no Spring context needed, so it runs in milliseconds.
 */
class PetClinicRuntimeHintsTests {

	private final RuntimeHints hints = new RuntimeHints();

	PetClinicRuntimeHintsTests() {
		new PetClinicRuntimeHints().registerHints(this.hints, getClass().getClassLoader());
	}

	@Test
	void registersDatabaseAndMessageResources() {
		assertThat(RuntimeHintsPredicates.resource().forResource("db/h2/schema.sql")).accepts(this.hints);
		assertThat(RuntimeHintsPredicates.resource().forResource("messages/messages.properties")).accepts(this.hints);
	}

	@Test
	void registersReflectionForEntities() {
		assertThat(RuntimeHintsPredicates.reflection().onType(BaseEntity.class)).accepts(this.hints);
		assertThat(RuntimeHintsPredicates.reflection().onType(Person.class)).accepts(this.hints);
		assertThat(RuntimeHintsPredicates.reflection().onType(Vet.class)).accepts(this.hints);
	}

}
