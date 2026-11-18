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
 * Unit tests for {@link Visit}.
 */
class VisitTest {

	@Test
	void newVisitIsScheduledForTomorrow() {
		LocalDate before = LocalDate.now().plusDays(1);
		Visit visit = new Visit();
		LocalDate after = LocalDate.now().plusDays(1);

		// "before"/"after" avoid a flaky failure if the test runs at midnight
		assertThat(visit.getDate()).isBetween(before, after);
		assertThat(visit.isNew()).isTrue();
		assertThat(visit.getDescription()).isNull();
	}

	@Test
	void keepsDateAndDescription() {
		Visit visit = new Visit();
		visit.setDate(LocalDate.of(2026, 11, 17));
		visit.setDescription("rabies shot");

		assertThat(visit.getDate()).isEqualTo(LocalDate.of(2026, 11, 17));
		assertThat(visit.getDescription()).isEqualTo("rabies shot");
	}

}
