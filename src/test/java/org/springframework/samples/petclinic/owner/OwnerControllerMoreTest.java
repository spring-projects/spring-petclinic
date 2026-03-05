package org.springframework.samples.petclinic.owner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

class OwnerControllerMoreTest {

	@Test
	void findOwner_missingOwner_throwsIllegalArgument() {
		OwnerRepository owners = org.mockito.Mockito.mock(OwnerRepository.class);
		org.mockito.Mockito.when(owners.findById(org.mockito.Mockito.anyInt())).thenReturn(Optional.empty());

		OwnerController controller = new OwnerController(owners);

		assertThrows(IllegalArgumentException.class, () -> controller.findOwner(999));
	}

	@Test
	void showOwner_missingOwner_throwsIllegalArgument() {
		OwnerRepository owners = org.mockito.Mockito.mock(OwnerRepository.class);
		org.mockito.Mockito.when(owners.findById(org.mockito.Mockito.anyInt())).thenReturn(Optional.empty());

		OwnerController controller = new OwnerController(owners);

		assertThrows(IllegalArgumentException.class, () -> controller.showOwner(12345));
	}

	@Test
	void processUpdateOwnerForm_idMismatch_redirectsToEdit() {
		OwnerRepository owners = org.mockito.Mockito.mock(OwnerRepository.class);

		OwnerController controller = new OwnerController(owners);

		Owner owner = new Owner();
		owner.setId(10);

		BeanPropertyBindingResult result = new BeanPropertyBindingResult(owner, "owner");

		String view = controller.processUpdateOwnerForm(owner, result, 5, new RedirectAttributesModelMap());

		assertEquals("redirect:/owners/{ownerId}/edit", view);
	}

}
