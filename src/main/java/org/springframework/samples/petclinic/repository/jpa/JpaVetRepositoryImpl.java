package org.springframework.samples.petclinic.repository.jpa;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.samples.petclinic.Vet;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.stereotype.Repository;

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
public class JpaVetRepositoryImpl implements VetRepository {

	@PersistenceContext
	private EntityManager em;


	@SuppressWarnings("unchecked")
	public Collection<Vet> findAll() {
		return this.em.createQuery("SELECT vet FROM Vet vet join fetch vet.specialties ORDER BY vet.lastName, vet.firstName").getResultList();
	}

}
