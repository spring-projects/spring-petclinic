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

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/**
 * Test class for {@link Visit}
 *
 * @author Andrew Huebner
 */
class VisitTest {

	@Test
	void shouldCreateVisit() {
		Visit visit = new Visit();
		assertThat(visit).isNotNull();
		assertThat(visit.getDate()).isEqualTo(LocalDate.now());
	}

	@Test
	void shouldSetDate() {
		Visit visit = new Visit();
		LocalDate date = LocalDate.of(2023, 5, 15);
		visit.setDate(date);
		assertThat(visit.getDate()).isEqualTo(date);
	}

	@Test
	void shouldSetDescription() {
		Visit visit = new Visit();
		visit.setDescription("Annual checkup");
		assertThat(visit.getDescription()).isEqualTo("Annual checkup");
	}

}