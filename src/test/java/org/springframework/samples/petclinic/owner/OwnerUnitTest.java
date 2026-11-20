package org.springframework.samples.petclinic.owner;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OwnerUnitTest {

	private Owner owner;

	private Pet existingPet;

	private Pet newPet;

	@BeforeEach
	void setUp() {
		owner = new Owner();
		owner.setId(1);
		owner.setFirstName("Giulia");
		owner.setLastName("Bianchi");
		owner.setAddress("Via Roma 12");
		owner.setCity("Salerno");
		owner.setTelephone("0891234567");

		existingPet = new Pet();
		existingPet.setId(10);
		existingPet.setName("Fido");

		newPet = new Pet();
		newPet.setName("Luna");
	}

	@Test
	void addPet_WhenPetIsNew_AddsPetToOwner() {
		owner.addPet(newPet);

		assertEquals(1, owner.getPets().size());
		assertTrue(owner.getPets().contains(newPet));
	}

	@Test
	void addPet_WhenPetHasExistingId_AddsPetToOwner() {
		owner.addPet(existingPet);

		assertEquals(1, owner.getPets().size());
		assertTrue(owner.getPets().contains(existingPet));
	}

	@Test
	void getPetByName_CaseInsensitiveAndIgnoreNewFlag_ReturnsExpectedPet() {
		owner.addPet(newPet);
		owner.getPets().add(existingPet);

		assertEquals(existingPet, owner.getPet("fido", true));
		assertNull(owner.getPet("luna", true), "Con ignoreNew=true un pet senza ID deve essere ignorato");
		assertEquals(newPet, owner.getPet("LUNA", false));
		assertEquals(newPet, owner.getPet("luna"));
		assertNull(owner.getPet("Inesistente"));
	}

	@Test
	void getPetById_WhenPetExistsAndIsNotNew_ReturnsPet() {
		owner.addPet(newPet);
		owner.getPets().add(existingPet);

		assertEquals(existingPet, owner.getPet(10));
		assertNull(owner.getPet(999));
	}

	@Test
	void addVisit_WhenValidPetId_AddsVisitToPet() {
		owner.getPets().add(existingPet);
		Visit visit = new Visit();
		visit.setDate(LocalDate.of(2026, 5, 10));
		visit.setDescription("Controllo annuale");

		owner.addVisit(10, visit);

		assertEquals(1, existingPet.getVisits().size());
		assertTrue(existingPet.getVisits().contains(visit));
	}

	@Test
	void addVisit_WhenInvalidArguments_ThrowsIllegalArgumentException() {
		owner.getPets().add(existingPet);
		Visit visit = new Visit();

		assertThrows(IllegalArgumentException.class, () -> owner.addVisit(null, visit));
		assertThrows(IllegalArgumentException.class, () -> owner.addVisit(10, null));
		assertThrows(IllegalArgumentException.class, () -> owner.addVisit(999, visit));
	}

}