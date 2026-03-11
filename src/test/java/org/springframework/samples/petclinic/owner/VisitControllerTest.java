package org.springframework.samples.petclinic.owner;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class VisitControllerTest {

	@Test
	void loadPetWithVisit_whenOwnerNotFound_throws() {
		OwnerRepository owners = mock(OwnerRepository.class);
		when(owners.findById(anyInt())).thenReturn(Optional.empty());

		VisitController controller = new VisitController(owners);

		Map<String, Object> model = new HashMap<>();
		Assertions.assertThrows(IllegalArgumentException.class, () -> controller.loadPetWithVisit(1, 1, model));
	}

	@Test
	void loadPetWithVisit_whenPetNotFound_throws() {
		OwnerRepository owners = mock(OwnerRepository.class);
		Owner owner = new Owner(); // no pets
		when(owners.findById(1)).thenReturn(Optional.of(owner));

		VisitController controller = new VisitController(owners);

		Map<String, Object> model = new HashMap<>();
		Assertions.assertThrows(IllegalArgumentException.class, () -> controller.loadPetWithVisit(1, 42, model));
	}

}
