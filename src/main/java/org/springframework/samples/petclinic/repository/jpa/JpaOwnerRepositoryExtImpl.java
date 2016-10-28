package org.springframework.samples.petclinic.repository.jpa;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.repository.OwnerRepositoryExt;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("OwnerRepositoryExt")
public class JpaOwnerRepositoryExtImpl extends JpaOwnerRepositoryImpl implements OwnerRepositoryExt {
	
    @PersistenceContext
    private EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Owner> findAll() throws DataAccessException {
		// TODO Select only owner or still join with pets ?
		Query query = this.em.createQuery("SELECT DISTINCT owner FROM Owner owner left join fetch owner.pets");
        return query.getResultList();
	}

	@Override
	public void delete(Owner owner) throws DataAccessException {
		// TODO need null check, throw Exception etc ?
		if(!(owner.getId() == null)){
			this.em.remove(owner);
		}

	}

}
