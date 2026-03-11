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

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VisitTest {

	private Visit visit;

	private Pet pet;

	@BeforeEach
	void setUp() {
		visit = new Visit();
		pet = new Pet();
	}

	@Test
	void testVisitCreation() {
		visit.setDate(LocalDate.of(2022, 1, 15));
		visit.setDescription("Routine checkup");

		assertEquals(LocalDate.of(2022, 1, 15), visit.getDate());
		assertEquals("Routine checkup", visit.getDescription());
	}

	@Test
	void testVisitDateSetterGetter() {
		LocalDate date = LocalDate.of(2023, 6, 20);
		visit.setDate(date);
		assertEquals(date, visit.getDate());
	}

	@Test
	void testVisitDescriptionSetterGetter() {
		visit.setDescription("Vaccination");
		assertEquals("Vaccination", visit.getDescription());
	}

	@Test
	void testVisitWithAllFields() {
		visit.setDate(LocalDate.now());
		visit.setDescription("Check-up");
		visit.setId(1);

		assertEquals(LocalDate.now(), visit.getDate());
		assertEquals("Check-up", visit.getDescription());
		assertEquals(1, visit.getId());
	}

	@Test
	void testVisitDateNull() {
		visit.setDate(null);
		assertNull(visit.getDate());
	}

	@Test
	void testVisitDescriptionEmpty() {
		visit.setDescription("");
		assertEquals("", visit.getDescription());
	}

	@Test
	void testVisitDescriptionWithSpecialCharacters() {
		visit.setDescription("Follow-up: Vaccination & Micro-chip");
		assertEquals("Follow-up: Vaccination & Micro-chip", visit.getDescription());
	}

	@Test
	void testVisitAfterTodayDate() {
		LocalDate futureDate = LocalDate.now().plusMonths(1);
		visit.setDate(futureDate);
		assertEquals(futureDate, visit.getDate());
	}

	@Test
	void testVisitPastDate() {
		LocalDate pastDate = LocalDate.of(2020, 1, 1);
		visit.setDate(pastDate);
		assertEquals(pastDate, visit.getDate());
	}

	@Test
	void testVisitMultipleDates() {
		LocalDate date1 = LocalDate.of(2022, 1, 15);
		LocalDate date2 = LocalDate.of(2023, 1, 15);

		visit.setDate(date1);
		assertEquals(date1, visit.getDate());

		visit.setDate(date2);
		assertEquals(date2, visit.getDate());
	}

	@Test
	void testVisitDescriptionLong() {
		String longDescription = "Routine checkup with full blood panel including CBC, "
				+ "metabolic panel, and thyroid function tests";
		visit.setDescription(longDescription);
		assertEquals(longDescription, visit.getDescription());
	}

	@Test
	void testVisitToString() {
		visit.setDate(LocalDate.now());
		visit.setDescription("Test");
		assertNotNull(visit.toString());
	}

	@Test
	void testVisitIsNew() {
		assertTrue(visit.isNew());
		visit.setId(5);
		assertFalse(visit.isNew());
	}

	@Test
	void testVisitWithOnlyDate() {
		visit.setDate(LocalDate.of(2025, 3, 10));
		assertEquals(LocalDate.of(2025, 3, 10), visit.getDate());
	}

	@Test
	void testVisitWithOnlyDescription() {
		visit.setDescription("Regular checkup");
		assertEquals("Regular checkup", visit.getDescription());
	}

}
