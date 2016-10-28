/**
 * 
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
		this.em.remove(pet);
	}

}
