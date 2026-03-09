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
package org.springframework.samples.petclinic.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Test class for {@link NamedEntity}
 *
 * @author Andrew Huebner
 */
class NamedEntityTest {

	@Test
	void shouldReturnName() {
		NamedEntity namedEntity = new NamedEntity();
		namedEntity.setName("Test Name");
		assertThat(namedEntity.getName()).isEqualTo("Test Name");
	}

	@Test
	void shouldReturnNameInToString() {
		NamedEntity namedEntity = new NamedEntity();
		namedEntity.setName("Test Name");
		assertThat(namedEntity.toString()).isEqualTo("Test Name");
	}

	@Test
	void shouldReturnNullInToStringWhenNameIsNull() {
		NamedEntity namedEntity = new NamedEntity();
		assertThat(namedEntity.toString()).isEqualTo("<null>");
	}

}