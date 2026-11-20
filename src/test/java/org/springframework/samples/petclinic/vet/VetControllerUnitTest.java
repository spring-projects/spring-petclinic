package org.springframework.samples.petclinic.vet;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VetControllerUnitTest {

	@Mock
	private VetRepository vetRepository;

	@InjectMocks
	private VetController vetController;

	private Vet vet1;

	private Vet vet2;

	@BeforeEach
	void setUp() {
		vet1 = new Vet();
		vet1.setId(1);
		vet1.setFirstName("Elena");
		vet1.setLastName("Verdi");

		vet2 = new Vet();
		vet2.setId(2);
		vet2.setFirstName("Luca");
		vet2.setLastName("Galli");
	}

	@Test
	void showResourcesVetList_WhenVetsExist_ReturnsVetsList() {
		when(vetRepository.findAll()).thenReturn(List.of(vet1, vet2));

		Vets result = vetController.showResourcesVetList();

		assertEquals(2, result.getVetList().size());
		assertTrue(result.getVetList().containsAll(List.of(vet1, vet2)));
		verify(vetRepository, times(1)).findAll();
	}

	@Test
	void showResourcesVetList_WhenNoVetsExist_ReturnsEmptyList() {
		when(vetRepository.findAll()).thenReturn(Collections.emptyList());

		Vets result = vetController.showResourcesVetList();

		assertTrue(result.getVetList().isEmpty());
		verify(vetRepository, times(1)).findAll();
	}

}