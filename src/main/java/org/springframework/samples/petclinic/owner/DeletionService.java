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

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Centralizes the delete rules for owners, pets and visits (MLG-13).
 * <ul>
 * <li>A visit can always be deleted.</li>
 * <li>A pet can only be deleted when it has no visits.</li>
 * <li>An owner can only be deleted when it has no pets.</li>
 * </ul>
 * Every operation resolves the entity through its parent (owner, then pet, then visit),
 * so an id that does not belong to the parent given in the request is treated as not
 * found.
 */
@Service
public class DeletionService {

	static final String OWNER = "Owner";

	static final String PET = "Pet";

	static final String VISIT = "Visit";

	private final OwnerRepository owners;

	public DeletionService(OwnerRepository owners) {
		this.owners = owners;
	}

	@Transactional(readOnly = true)
	public Owner getOwner(int ownerId) {
		return this.owners.findById(ownerId).orElseThrow(() -> new ResourceNotFoundException(OWNER, ownerId));
	}

	@Transactional(readOnly = true)
	public Pet getPet(int ownerId, int petId) {
		Pet pet = getOwner(ownerId).getPet(petId);
		if (pet == null) {
			throw new ResourceNotFoundException(PET, petId);
		}
		return pet;
	}

	@Transactional(readOnly = true)
	public Visit getVisit(int ownerId, int petId, int visitId) {
		return getPet(ownerId, petId).getVisits()
			.stream()
			.filter(visit -> Objects.equals(visit.getId(), visitId))
			.findFirst()
			.orElseThrow(() -> new ResourceNotFoundException(VISIT, visitId));
	}

	@Transactional
	public void deleteVisit(int ownerId, int petId, int visitId) {
		Owner owner = getOwner(ownerId);
		Pet pet = findPet(owner, petId);
		boolean removed = pet.getVisits().removeIf(visit -> Objects.equals(visit.getId(), visitId));
		if (!removed) {
			throw new ResourceNotFoundException(VISIT, visitId);
		}
		this.owners.save(owner);
	}

	@Transactional
	public void deletePet(int ownerId, int petId) {
		Owner owner = getOwner(ownerId);
		Pet pet = findPet(owner, petId);
		if (!pet.getVisits().isEmpty()) {
			throw new DeleteBlockedException(PET, petId, "the pet still has " + pet.getVisits().size() + " visit(s)");
		}
		owner.getPets().remove(pet);
		this.owners.save(owner);
	}

	@Transactional
	public void deleteOwner(int ownerId) {
		Owner owner = getOwner(ownerId);
		if (!owner.getPets().isEmpty()) {
			throw new DeleteBlockedException(OWNER, ownerId,
					"the owner still has " + owner.getPets().size() + " pet(s)");
		}
		this.owners.delete(owner);
	}

	private Pet findPet(Owner owner, int petId) {
		Pet pet = owner.getPet(petId);
		if (pet == null) {
			throw new ResourceNotFoundException(PET, petId);
		}
		return pet;
	}

}
