/**
 * 
 */
package org.springframework.samples.petclinic.repository.jpa;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;
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
		String petId = pet.getId().toString();
		this.em.createQuery("DELETE FROM Visit visit WHERE pet_id=" + petId).executeUpdate();
		this.em.createQuery("DELETE FROM Pet pet WHERE id=" + petId).executeUpdate();
	}

}
