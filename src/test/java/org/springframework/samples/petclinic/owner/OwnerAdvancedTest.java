package org.springframework.samples.petclinic.owner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OwnerAdvancedTest {

	@Test
	public void testOwnerAddMultiplePetsAndRetrieve() {
		Owner owner = new Owner();
		owner.setId(1);

		// Add pets without setting their IDs (they remain new)
		for (int i = 0; i < 3; i++) {
			Pet pet = new Pet();
			pet.setName("Pet" + i);
			owner.addPet(pet);
		}

		assertEquals(3, owner.getPets().size());
		assertNotNull(owner.getPet("Pet0"));
		assertNotNull(owner.getPet("Pet2"));
		assertNull(owner.getPet("NonExistent"));
	}

	@Test
	public void testOwnerGetPetIgnoreNew() {
		Owner owner = new Owner();
		Pet newPet = new Pet();
		newPet.setName("NewPet");
		owner.addPet(newPet);

		Pet retrievedIgnoreNew = owner.getPet("NewPet", true);
		assertNull(retrievedIgnoreNew);

		Pet retrievedIncludeNew = owner.getPet("NewPet", false);
		assertNotNull(retrievedIncludeNew);
	}

	@Test
	public void testOwnerIsNew() {
		Owner owner = new Owner();
		assertTrue(owner.isNew());

		owner.setId(5);
		assertFalse(owner.isNew());
	}

}
