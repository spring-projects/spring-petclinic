/*
 * Copyright 2012-2025 the original author or authors.
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
package org.springframework.samples.petclinic.owner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for {@link DeletionService} (MLG-13).
 */
@ExtendWith(MockitoExtension.class)
class DeletionServiceTests {

	private static final int OWNER_ID = 1;

	private static final int PET_ID = 10;

	private static final int VISIT_ID = 100;

	@Mock
	private OwnerRepository owners;

	private DeletionService service;

	private Owner owner;

	private Pet pet;

	private Visit visit;

	@BeforeEach
	void setUp() {
		service = new DeletionService(owners);

		owner = new Owner();
		owner.setId(OWNER_ID);
		pet = new Pet();
		pet.setId(PET_ID);
		pet.setName("Leo");
		visit = new Visit();
		visit.setId(VISIT_ID);
		visit.setDescription("checkup");
		pet.addVisit(visit);
		owner.addPet(pet);

		lenient().when(owners.findById(OWNER_ID)).thenReturn(Optional.of(owner));
	}

	@Test
	void deleteVisitSucceeds() {
		service.deleteVisit(OWNER_ID, PET_ID, VISIT_ID);

		assertThat(pet.getVisits()).isEmpty();
		verify(owners).save(owner);
	}

	@Test
	void deleteVisitNotFoundWhenVisitBelongsToAnotherPet() {
		Pet otherPet = new Pet();
		otherPet.setId(11);
		owner.addPet(otherPet);

		assertThatThrownBy(() -> service.deleteVisit(OWNER_ID, 11, VISIT_ID))
			.isInstanceOf(ResourceNotFoundException.class)
			.hasMessageContaining("Visit");

		assertThat(pet.getVisits()).containsExactly(visit);
		verify(owners, never()).save(owner);
	}

	@Test
	void deleteVisitNotFoundWhenPetBelongsToAnotherOwner() {
		Owner otherOwner = new Owner();
		otherOwner.setId(2);
		given(owners.findById(2)).willReturn(Optional.of(otherOwner));

		assertThatThrownBy(() -> service.deleteVisit(2, PET_ID, VISIT_ID)).isInstanceOf(ResourceNotFoundException.class)
			.hasMessageContaining("Pet");

		assertThat(pet.getVisits()).containsExactly(visit);
	}

	@Test
	void deleteVisitNotFoundWhenOwnerMissing() {
		given(owners.findById(99)).willReturn(Optional.empty());

		assertThatThrownBy(() -> service.deleteVisit(99, PET_ID, VISIT_ID))
			.isInstanceOf(ResourceNotFoundException.class)
			.hasMessageContaining("Owner");
	}

	@Test
	void deletePetBlockedWhenVisitsExist() {
		assertThatThrownBy(() -> service.deletePet(OWNER_ID, PET_ID)).isInstanceOf(DeleteBlockedException.class)
			.hasMessageContaining("1 visit(s)");

		assertThat(owner.getPets()).containsExactly(pet);
		verify(owners, never()).save(owner);
	}

	@Test
	void deletePetSucceedsWhenNoVisits() {
		pet.getVisits().clear();

		service.deletePet(OWNER_ID, PET_ID);

		assertThat(owner.getPets()).isEmpty();
		verify(owners).save(owner);
	}

	@Test
	void deletePetNotFoundWhenPetBelongsToAnotherOwner() {
		Owner otherOwner = new Owner();
		otherOwner.setId(2);
		given(owners.findById(2)).willReturn(Optional.of(otherOwner));

		assertThatThrownBy(() -> service.deletePet(2, PET_ID)).isInstanceOf(ResourceNotFoundException.class);

		assertThat(owner.getPets()).containsExactly(pet);
	}

	@Test
	void deleteOwnerBlockedWhenPetsExist() {
		assertThatThrownBy(() -> service.deleteOwner(OWNER_ID)).isInstanceOf(DeleteBlockedException.class)
			.hasMessageContaining("1 pet(s)");

		verify(owners, never()).delete(owner);
	}

	@Test
	void deleteOwnerSucceedsWhenNoPets() {
		owner.getPets().clear();

		service.deleteOwner(OWNER_ID);

		verify(owners).delete(owner);
	}

	@Test
	void deleteOwnerNotFound() {
		given(owners.findById(99)).willReturn(Optional.empty());

		assertThatThrownBy(() -> service.deleteOwner(99)).isInstanceOf(ResourceNotFoundException.class);
	}

}
