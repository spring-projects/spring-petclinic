package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.samples.petclinic.Owner;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Using native JPA here because of this query:
 * "SELECT owner FROM Owner owner left join fetch owner.pets WHERE owner.lastName LIKE :lastName"
 * See https://jira.springsource.org/browse/DATAJPA-292 for more details.
 *
 * @author Michael Isvy
 */
@Repository
@Transactional
public class JpaOwnerRepositoryImpl implements OwnerRepository {

	@PersistenceContext
	private EntityManager em;
	

	@SuppressWarnings("unchecked")
	public Collection<Owner> findByLastName(String lastName) {
		// using 'join fetch' because a single query should load both owners and pets
		// using 'left join fetch' because it might happen that an owner does not have pets yet
		Query query = this.em.createQuery("SELECT owner FROM Owner owner left join fetch owner.pets WHERE owner.lastName LIKE :lastName");
		query.setParameter("lastName", lastName + "%");
		return query.getResultList();
	}

	public Owner findById(int id) {
		// using 'join fetch' because a single query should load both owners and pets
		// using 'left join fetch' because it might happen that an owner does not have pets yet
		Query query = this.em.createQuery("SELECT owner FROM Owner owner left join fetch owner.pets WHERE owner.id =:id");
		query.setParameter("id", id);
		return  (Owner) query.getSingleResult();
	}


	public void save(Owner owner) {
		this.em.merge(owner);

	}

}
