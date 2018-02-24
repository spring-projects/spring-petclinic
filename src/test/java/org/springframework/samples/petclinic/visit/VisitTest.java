package org.springframework.samples.petclinic.visit;

import java.util.Date;
import static org.junit.Assert.*;
import org.junit.Test;

public class VisitTest {
	private Visit visit;

	@Test
	public void testGetDate() {
		Date today = new Date();
		visit.setDate(today);
		assertEquals(visit.getDate(), today);
	}
	
	@Test
	public void testGetDescription() {
		String description = "The greatest visit of all time";
		visit.setDescription(description);
		assertEquals(visit.getDescription(), description);
	}
	
	@Test
	public void testGetPetId() {
		int petId = 1;
		visit.setPetId(petId);
		assertEquals((int) visit.getPetId(), petId);
	}

}
