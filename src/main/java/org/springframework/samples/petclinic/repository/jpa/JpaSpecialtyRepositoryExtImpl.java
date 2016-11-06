package org.springframework.samples.petclinic.repository.jpa;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.repository.SpecialtyRepositoryExt;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("SpecialtyRepositoryExt")
public class JpaSpecialtyRepositoryExtImpl implements SpecialtyRepositoryExt {
	
    @PersistenceContext
    private EntityManager em;

	@Override
	public Specialty findById(int id) {
		return this.em.find(Specialty.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Specialty> findAll() throws DataAccessException {
		return this.em.createQuery("SELECT s FROM Specialty s").getResultList();
	}

	@Override
	public void save(Specialty specialty) throws DataAccessException {
		if (specialty.getId() == null) {
            this.em.persist(specialty);
        } else {
            this.em.merge(specialty);
        }
	}

	@Override
	public void delete(Specialty specialty) throws DataAccessException {
		this.em.remove(this.em.contains(specialty) ? specialty : this.em.merge(specialty));
	}

}
