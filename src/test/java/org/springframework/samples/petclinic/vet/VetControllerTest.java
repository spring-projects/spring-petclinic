/*
 * Copyright 2012-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.vet;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VetControllerTest {

	private VetRepository vetRepository;

	private VetController vetController;

	@BeforeEach
	void setup() {
		vetRepository = mock(VetRepository.class);
		vetController = new VetController(vetRepository);
	}

	@Test
	@DisplayName("Test showResourcesVetList returns correct list of veterinarians")
	void testShowResourcesVetList() {
		// Given
		Vet vet1 = new Vet();
		vet1.setFirstName("John");
		vet1.setLastName("Doe");

		Vet vet2 = new Vet();
		vet2.setFirstName("Jane");
		vet2.setLastName("Smith");

		List<Vet> vetsList = Arrays.asList(vet1, vet2);
		given(vetRepository.findAll()).willReturn(vetsList);

		// When
		Vets vets = vetController.showResourcesVetList();

		// Then
		assertThat(vets).isNotNull();
		assertThat(vets.getVetList()).hasSize(2);
		assertThat(vets.getVetList()).extracting(Vet::getFirstName).containsExactly("John", "Jane");
		assertThat(vets.getVetList()).extracting(Vet::getLastName).containsExactly("Doe", "Smith");
	}

}
