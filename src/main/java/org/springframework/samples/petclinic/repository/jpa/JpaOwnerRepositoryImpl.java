package org.springframework.samples.petclinic.repository.jpa;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * JPA implementation of the ClinicService interface using EntityManager.
 *
 * <p>The mappings are defined in "orm.xml" located in the META-INF directory.
 *
 * @author Mike Keith
 * @author Rod Johnson
 * @author Sam Brannen
 * @author Michael Isvy
 * @since 22.4.2006
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
