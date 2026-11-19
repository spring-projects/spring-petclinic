package org.springframework.samples.petclinic.vet;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VetUnitTest {

	private Vet vet;

	@BeforeEach
	void setUp() {
		vet = new Vet();
		vet.setId(1);
		vet.setFirstName("Marco");
		vet.setLastName("Neri");
	}

	@Test
	void getSpecialties_WhenEmpty_ReturnsZeroCountAndEmptyList() {
		assertEquals(0, vet.getNrOfSpecialties());
		assertTrue(vet.getSpecialties().isEmpty());
	}

	@Test
	void getSpecialties_WhenMultipleAdded_ReturnsSortedAlphabeticallyByName() {
		Specialty surgery = new Specialty();
		surgery.setId(1);
		surgery.setName("surgery");

		Specialty dentistry = new Specialty();
		dentistry.setId(2);
		dentistry.setName("dentistry");

		Specialty radiology = new Specialty();
		radiology.setId(3);
		radiology.setName("radiology");

		vet.addSpecialty(surgery);
		vet.addSpecialty(dentistry);
		vet.addSpecialty(radiology);

		List<Specialty> specialties = vet.getSpecialties();
		assertEquals(3, vet.getNrOfSpecialties());
		assertEquals("dentistry", specialties.get(0).getName());
		assertEquals("radiology", specialties.get(1).getName());
		assertEquals("surgery", specialties.get(2).getName());
	}

	@Test
	void vetsWrapper_GetVetList_InitializesEmptyList() {
		Vets vets = new Vets();

		assertNotNull(vets.getVetList());
		assertTrue(vets.getVetList().isEmpty());
	}

}