package org.springframework.samples.petclinic.owner;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PetControllerTest {

	@Test
	void findOwner_whenNotFound_throws() {
		OwnerRepository owners = mock(OwnerRepository.class);
		when(owners.findById(anyInt())).thenReturn(Optional.empty());

		PetController controller = new PetController(owners, mock(PetTypeRepository.class));

		Assertions.assertThrows(IllegalArgumentException.class, () -> controller.findOwner(1));
	}

	@Test
	void findPet_whenOwnerNotFound_throws() {
		OwnerRepository owners = mock(OwnerRepository.class);
		when(owners.findById(anyInt())).thenReturn(Optional.empty());

		PetController controller = new PetController(owners, mock(PetTypeRepository.class));

		// petId provided, but owner lookup fails -> should throw
		Assertions.assertThrows(IllegalArgumentException.class, () -> controller.findPet(1, 5));
	}

}
