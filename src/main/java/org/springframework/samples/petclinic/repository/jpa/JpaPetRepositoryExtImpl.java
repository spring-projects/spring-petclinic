/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.repository.jpa;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.PetRepositoryExt;
import org.springframework.stereotype.Repository;

/**
 * @author Vitaliy Fedoriv
 *
 */
@Repository
@Qualifier("PetRepositoryExt")
public class JpaPetRepositoryExtImpl extends JpaPetRepositoryImpl implements PetRepositoryExt {
	
    @PersistenceContext
    private EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Pet> findAll() throws DataAccessException {
		return this.em.createQuery("SELECT pet FROM Pet pet").getResultList();
	}

	@Override
	public void delete(Pet pet) throws DataAccessException {
		//this.em.remove(this.em.contains(pet) ? pet : this.em.merge(pet));
		String petId = pet.getId().toString();
		this.em.createQuery("DELETE FROM Visit visit WHERE pet_id=" + petId).executeUpdate();
		this.em.createQuery("DELETE FROM Pet pet WHERE id=" + petId).executeUpdate();
		if (em.contains(pet)) {
			em.remove(pet);
		}
	}

}
