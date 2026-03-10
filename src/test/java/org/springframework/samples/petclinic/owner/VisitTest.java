package org.springframework.samples.petclinic.owner;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VisitTest {

	@Test
	public void testVisitDate() {
		Visit visit = new Visit();
		LocalDate visitDate = LocalDate.of(2024, 6, 15);
		visit.setDate(visitDate);
		assertEquals(visitDate, visit.getDate());
	}

	@Test
	public void testVisitDescription() {
		Visit visit = new Visit();
		visit.setDescription("Routine checkup");
		assertEquals("Routine checkup", visit.getDescription());
	}

	@Test
	public void testVisitId() {
		Visit visit = new Visit();
		visit.setId(10);
		assertEquals(10, visit.getId());
	}

	@Test
	public void testVisitMultipleDates() {
		Visit visit1 = new Visit();
		Visit visit2 = new Visit();
		visit1.setDate(LocalDate.of(2024, 1, 10));
		visit2.setDate(LocalDate.of(2024, 2, 15));
		assertNotEquals(visit1.getDate(), visit2.getDate());
	}

	@Test
	public void testVisitDifferentDates() {
		Visit visit1 = new Visit();
		Visit visit2 = new Visit();
		visit1.setDate(LocalDate.of(2024, 1, 10));
		visit2.setDate(LocalDate.of(2024, 2, 15));
		assertNotEquals(visit1.getDate(), visit2.getDate());
	}

}
