package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class PetTests {
	@Test
	public void testSetAndGetBirthDate() {
		Pet pet = new Pet();
		LocalDate birthDate = LocalDate.of(2020, 1, 1);

		pet.setBirthDate(birthDate);

		assertThat(pet.getBirthDate()).isEqualTo(birthDate);
	}

	@Test
	public void testSetAndGetType() {
		Pet pet = new Pet();
		PetType type = new PetType();
		type.setName("Dog");

		pet.setType(type);

		assertThat(pet.getType().getName()).isEqualTo("Dog");
	}

	@Test
	public void testAddVisit() {
		Pet pet = new Pet();
		Visit visit = new Visit();
		visit.setDescription("Regular check-up");

		pet.addVisit(visit);

		Collection<Visit> visits = pet.getVisits();
		assertThat(visits).contains(visit);
	}
}
