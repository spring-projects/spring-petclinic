package org.springframework.samples.petclinic.vet;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SpecialtyTest {

	@Test
	public void testSpecialtyName() {
		Specialty specialty = new Specialty();
		specialty.setName("Surgery");
		assertEquals("Surgery", specialty.getName());
	}

	@Test
	public void testSpecialtyId() {
		Specialty specialty = new Specialty();
		specialty.setId(5);
		assertEquals(5, specialty.getId());
	}

	@Test
	public void testSpecialtyDifferentSpecialties() {
		Specialty specialty1 = new Specialty();
		Specialty specialty2 = new Specialty();
		specialty1.setName("Surgery");
		specialty2.setName("Dentistry");
		assertNotEquals(specialty1.getName(), specialty2.getName());
	}

	@Test
	public void testSpecialtyDetailsSet() {
		Specialty specialty = new Specialty();
		specialty.setId(10);
		specialty.setName("Radiology");
		assertEquals(10, specialty.getId());
		assertEquals("Radiology", specialty.getName());
	}

}
