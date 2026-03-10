package org.springframework.samples.petclinic.owner;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PetTypeTest {

	@Test
	public void testPetTypeName() {
		PetType petType = new PetType();
		petType.setName("Dog");
		assertEquals("Dog", petType.getName());
	}

	@Test
	public void testPetTypeId() {
		PetType petType = new PetType();
		petType.setId(1);
		assertEquals(1, petType.getId());
	}

	@Test
	public void testPetTypeMultipleTypes() {
		PetType dog = new PetType();
		PetType cat = new PetType();
		dog.setName("Dog");
		cat.setName("Cat");
		assertNotEquals(dog.getName(), cat.getName());
	}

}
