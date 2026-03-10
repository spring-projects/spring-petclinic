package org.springframework.samples.petclinic.owner;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PetTest {

	@Test
	public void testPetName() {
		Pet pet = new Pet();
		pet.setName("Fido");
		assertEquals("Fido", pet.getName());
	}

	@Test
	public void testPetBirthDate() {
		Pet pet = new Pet();
		LocalDate birthDate = LocalDate.of(2020, 5, 15);
		pet.setBirthDate(birthDate);
		assertEquals(birthDate, pet.getBirthDate());
	}

	@Test
	public void testPetType() {
		Pet pet = new Pet();
		PetType type = new PetType();
		type.setName("Dog");
		pet.setType(type);
		assertEquals(type, pet.getType());
	}

	@Test
	public void testPetAddVisit() {
		Pet pet = new Pet();
		Visit visit = new Visit();
		visit.setDate(LocalDate.now());
		pet.addVisit(visit);
		assertEquals(1, pet.getVisits().size());
		assertTrue(pet.getVisits().contains(visit));
	}

	@Test
	public void testPetMultipleVisits() {
		Pet pet = new Pet();
		Visit visit1 = new Visit();
		Visit visit2 = new Visit();
		visit1.setDate(LocalDate.of(2024, 1, 15));
		visit2.setDate(LocalDate.of(2024, 2, 20));
		pet.addVisit(visit1);
		pet.addVisit(visit2);
		assertEquals(2, pet.getVisits().size());
	}

	@Test
	public void testPetGetVisits() {
		Pet pet = new Pet();
		Visit visit = new Visit();
		visit.setDescription("Checkup");
		pet.addVisit(visit);
		int size = pet.getVisits().size();
		assertEquals(1, size);
	}

	@Test
	public void testPetComplete() {
		Pet pet = new Pet();
		PetType type = new PetType();
		type.setName("Cat");

		pet.setName("Whiskers");
		pet.setType(type);
		pet.setBirthDate(LocalDate.of(2021, 3, 10));

		assertEquals("Whiskers", pet.getName());
		assertEquals(type, pet.getType());
		assertEquals(LocalDate.of(2021, 3, 10), pet.getBirthDate());
	}

}
