package org.springframework.samples.petclinic.vet;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VetTest {

	@Test
	public void testVetFirstAndLastName() {
		Vet vet = new Vet();
		vet.setFirstName("James");
		vet.setLastName("Carter");
		assertEquals("James", vet.getFirstName());
		assertEquals("Carter", vet.getLastName());
	}

	@Test
	public void testVetId() {
		Vet vet = new Vet();
		vet.setId(1);
		assertEquals(1, vet.getId());
	}

	@Test
	public void testVetAddSpecialty() {
		Vet vet = new Vet();
		Specialty specialty = new Specialty();
		specialty.setName("Surgery");
		vet.addSpecialty(specialty);
		assertEquals(1, vet.getSpecialties().size());
		assertTrue(vet.getSpecialties().contains(specialty));
	}

	@Test
	public void testVetMultipleSpecialties() {
		Vet vet = new Vet();
		Specialty specialty1 = new Specialty();
		Specialty specialty2 = new Specialty();
		specialty1.setName("Surgery");
		specialty2.setName("Dentistry");
		vet.addSpecialty(specialty1);
		vet.addSpecialty(specialty2);
		assertEquals(2, vet.getSpecialties().size());
	}

	@Test
	public void testVetGetSpecialties() {
		Vet vet = new Vet();
		Specialty specialty = new Specialty();
		specialty.setName("Cardiology");
		vet.addSpecialty(specialty);
		int size = vet.getSpecialties().size();
		assertEquals(1, size);
	}

	@Test
	public void testVetNoSpecialties() {
		Vet vet = new Vet();
		assertNotNull(vet.getSpecialties());
		assertEquals(0, vet.getSpecialties().size());
	}

	@Test
	public void testVetComplete() {
		Vet vet = new Vet();
		vet.setId(3);
		vet.setFirstName("Helen");
		vet.setLastName("Leary");
		Specialty specialty = new Specialty();
		specialty.setName("Orthopedics");
		vet.addSpecialty(specialty);
		assertEquals(3, vet.getId());
		assertEquals("Helen", vet.getFirstName());
		assertEquals("Leary", vet.getLastName());
		assertEquals(1, vet.getSpecialties().size());
	}

}
