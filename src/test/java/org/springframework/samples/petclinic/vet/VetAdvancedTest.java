package org.springframework.samples.petclinic.vet;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VetAdvancedTest {

	@Test
	public void testVetMultipleSpecialties() {
		Vet vet = new Vet();
		vet.setId(1);
		vet.setFirstName("Dr");
		vet.setLastName("Smith");

		Specialty s1 = new Specialty();
		s1.setId(1);
		s1.setName("Surgery");

		Specialty s2 = new Specialty();
		s2.setId(2);
		s2.setName("Dentistry");

		Specialty s3 = new Specialty();
		s3.setId(3);
		s3.setName("Cardiology");

		vet.addSpecialty(s1);
		vet.addSpecialty(s2);
		vet.addSpecialty(s3);

		assertEquals(3, vet.getNrOfSpecialties());
		List<Specialty> specialties = vet.getSpecialties();
		assertEquals(3, specialties.size());
	}

	@Test
	public void testVetNoSpecialties() {
		Vet vet = new Vet();
		assertEquals(0, vet.getNrOfSpecialties());
		List<Specialty> specialties = vet.getSpecialties();
		assertEquals(0, specialties.size());
	}

	@Test
	public void testVetSpecialtiesSorted() {
		Vet vet = new Vet();

		Specialty s1 = new Specialty();
		s1.setName("Zebra");

		Specialty s2 = new Specialty();
		s2.setName("Apple");

		Specialty s3 = new Specialty();
		s3.setName("Monkey");

		vet.addSpecialty(s1);
		vet.addSpecialty(s2);
		vet.addSpecialty(s3);

		List<Specialty> specialties = vet.getSpecialties();
		assertEquals("Apple", specialties.get(0).getName());
		assertEquals("Monkey", specialties.get(1).getName());
		assertEquals("Zebra", specialties.get(2).getName());
	}

	@Test
	public void testVetInheritanceFromPerson() {
		Vet vet = new Vet();
		vet.setId(100);
		vet.setFirstName("James");
		vet.setLastName("Carter");
		assertEquals(100, vet.getId());
		assertEquals("James", vet.getFirstName());
		assertEquals("Carter", vet.getLastName());
	}

}
