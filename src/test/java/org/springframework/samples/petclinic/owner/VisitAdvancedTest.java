package org.springframework.samples.petclinic.owner;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VisitAdvancedTest {

	@Test
	public void testVisitDefaultDate() {
		Visit visit = new Visit();
		LocalDate today = LocalDate.now();
		assertEquals(today, visit.getDate());
	}

	@Test
	public void testVisitCustomDate() {
		Visit visit = new Visit();
		LocalDate customDate = LocalDate.of(2025, 6, 15);
		visit.setDate(customDate);
		assertEquals(customDate, visit.getDate());
	}

	@Test
	public void testVisitDescriptionAdvanced() {
		Visit visit = new Visit();
		String longDescription = "This is a comprehensive description of the pet's visit including "
				+ "all observations, treatments, and recommendations for future care";
		visit.setDescription(longDescription);
		assertEquals(longDescription, visit.getDescription());
	}

	@Test
	public void testVisitDescriptionLong() {
		Visit visit = new Visit();
		String description = "This is a very long description for a visit with many details "
				+ "about the pet's condition and what was done during the visit";
		visit.setDescription(description);
		assertEquals(description, visit.getDescription());
	}

	@Test
	public void testVisitMultipleDifferentDates() {
		Visit v1 = new Visit();
		Visit v2 = new Visit();
		Visit v3 = new Visit();

		v1.setDate(LocalDate.of(2024, 1, 15));
		v2.setDate(LocalDate.of(2024, 6, 20));
		v3.setDate(LocalDate.of(2024, 12, 31));

		assertNotEquals(v1.getDate(), v2.getDate());
		assertNotEquals(v2.getDate(), v3.getDate());
		assertNotEquals(v1.getDate(), v3.getDate());
	}

	@Test
	public void testVisitIdAssignment() {
		Visit visit = new Visit();
		visit.setId(999);
		assertEquals(999, visit.getId());
	}

	@Test
	public void testVisitIsNew() {
		Visit visit = new Visit();
		assertTrue(visit.isNew());
		visit.setId(5);
		assertFalse(visit.isNew());
	}

}
