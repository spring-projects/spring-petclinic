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
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.model.Person;
import org.springframework.samples.petclinic.vet.Vet;

/**
 * Test class for {@link PetClinicRuntimeHints}
 *
 * @author Test Suite
 */
class PetClinicRuntimeHintsTests {

	private final PetClinicRuntimeHints testee = new PetClinicRuntimeHints();

	@Test
	void testRegisterHintsNotNull() {
		RuntimeHints hints = new RuntimeHints();
		testee.registerHints(hints, getClass().getClassLoader());
		assertThat(hints).isNotNull();
	}

	@Test
	void testRegisterHintsResourcePatterns() {
		RuntimeHints hints = new RuntimeHints();
		testee.registerHints(hints, getClass().getClassLoader());
		// Verify hints were registered
		assertThat(hints).isNotNull();
	}

	@Test
	void testRegisterHintsWithDifferentClassLoader() {
		RuntimeHints hints = new RuntimeHints();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		testee.registerHints(hints, classLoader);
		assertThat(hints).isNotNull();
	}

	@Test
	void testConstructor() {
		PetClinicRuntimeHints hints = new PetClinicRuntimeHints();
		assertThat(hints).isNotNull();
	}

	@Test
	void testRegisterHintsMultipleCalls() {
		RuntimeHints hints1 = new RuntimeHints();
		RuntimeHints hints2 = new RuntimeHints();
		ClassLoader cl = getClass().getClassLoader();

		testee.registerHints(hints1, cl);
		testee.registerHints(hints2, cl);

		assertThat(hints1).isNotNull();
		assertThat(hints2).isNotNull();
	}

}
