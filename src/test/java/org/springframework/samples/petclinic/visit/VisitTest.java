package org.springframework.samples.petclinic.visit;

import java.util.Date;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class VisitTest {
	private Visit visit;

	@Before
	public void setUp() {
		visit = new Visit();
	}

	@Test
	public void testGetDate() {
		Date today = new Date();
		visit.setDate(today);
		assertThat(visit.getDate()).isEqualTo(today);
	}
	
	@Test
	public void testGetDescription() {
		String description = "The greatest visit of all time";
		visit.setDescription(description);
		assertThat(visit.getDescription()).isEqualTo(description);
	}
	
	@Test
	public void testGetPetId() {
		int petId = 1;
		visit.setPetId(petId);
		assertThat((int) visit.getPetId()).isEqualTo(petId);
	}

}
