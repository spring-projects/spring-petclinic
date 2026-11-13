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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.aot.DisabledInAotMode;

/**
 * Verifies against the real (H2) database that deletes actually remove rows (MLG-13). Not
 * transactional on purpose: each service call commits, as in production.
 */
@SpringBootTest
@DisabledInNativeImage
@DisabledInAotMode
class DeletionServiceIntegrationTests {

	@Autowired
	private DeletionService service;

	@Autowired
	private OwnerRepository owners;

	@Autowired
	private PetTypeRepository types;

	@Autowired
	private JdbcTemplate jdbc;

	@Test
	void visitThenPetThenOwnerAreDeletedInOrder() {
		Owner owner = new Owner();
		owner.setFirstName("Delete");
		owner.setLastName("Me");
		owner.setAddress("1 Test St");
		owner.setCity("Testville");
		owner.setTelephone("1234567890");
		Pet pet = new Pet();
		pet.setName("Ephemeral");
		pet.setType(types.findPetTypes().get(0));
		Visit visit = new Visit();
		visit.setDescription("temp");
		pet.addVisit(visit);
		owner.addPet(pet);
		owner = owners.save(owner);

		int ownerId = owner.getId();
		int petId = owner.getPets().get(0).getId();
		int visitId = owner.getPets().get(0).getVisits().iterator().next().getId();

		assertThatThrownBy(() -> service.deleteOwner(ownerId)).isInstanceOf(DeleteBlockedException.class);
		assertThatThrownBy(() -> service.deletePet(ownerId, petId)).isInstanceOf(DeleteBlockedException.class);

		service.deleteVisit(ownerId, petId, visitId);
		assertThat(count("visits", visitId)).isZero();

		service.deletePet(ownerId, petId);
		assertThat(count("pets", petId)).isZero();

		service.deleteOwner(ownerId);
		assertThat(count("owners", ownerId)).isZero();
	}

	private int count(String table, int id) {
		return jdbc.queryForObject("SELECT COUNT(*) FROM " + table + " WHERE id = ?", Integer.class, id);
	}

}
