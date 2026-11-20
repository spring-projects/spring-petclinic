package org.springframework.samples.petclinic.owner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.ModelAndView;

@ExtendWith(MockitoExtension.class)
class OwnerControllerUnitTest {

	@Mock
	private OwnerRepository owners;

	@InjectMocks
	private OwnerController ownerController;

	private Owner sampleOwner;

	@BeforeEach
	void setUp() {
		sampleOwner = new Owner();
		sampleOwner.setId(1);
		sampleOwner.setFirstName("Mario");
		sampleOwner.setLastName("Rossi");
		sampleOwner.setAddress("Via Corso 10");
		sampleOwner.setCity("Napoli");
		sampleOwner.setTelephone("0819876543");
	}

	@Test
	void findOwner_WhenOwnerIdIsNull_ReturnsNewEmptyOwner() {
		Owner result = ownerController.findOwner(null);

		assertNotNull(result);
		assertTrue(result.isNew());
		verifyNoInteractions(owners);
	}

	@Test
	void findOwner_WhenOwnerExists_ReturnsPersistedOwner() {
		when(owners.findById(1)).thenReturn(Optional.of(sampleOwner));

		Owner result = ownerController.findOwner(1);

		assertEquals(sampleOwner, result);
		verify(owners, times(1)).findById(1);
	}

	@Test
	void findOwner_WhenOwnerDoesNotExist_ThrowsIllegalArgumentException() {
		when(owners.findById(99)).thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class, () -> ownerController.findOwner(99));
		verify(owners, times(1)).findById(99);
	}

	@Test
	void showOwner_WhenOwnerExists_ReturnsDetailsViewAndModel() {
		when(owners.findById(1)).thenReturn(Optional.of(sampleOwner));

		ModelAndView mav = ownerController.showOwner(1);

		assertEquals("owners/ownerDetails", mav.getViewName());
		assertEquals(sampleOwner, mav.getModel().get("owner"));
		verify(owners, times(1)).findById(1);
	}

	@Test
	void showOwner_WhenOwnerDoesNotExist_ThrowsIllegalArgumentException() {
		when(owners.findById(99)).thenReturn(Optional.empty());

		assertThrows(IllegalArgumentException.class, () -> ownerController.showOwner(99));
		verify(owners, times(1)).findById(99);
	}

}