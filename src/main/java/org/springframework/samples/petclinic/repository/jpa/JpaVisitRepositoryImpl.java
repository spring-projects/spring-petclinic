package org.springframework.samples.petclinic.repository.jpa;



import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.VisitRepository;
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
public class JpaVisitRepositoryImpl implements VisitRepository {

	@PersistenceContext
	private EntityManager em;


	public void save(Visit visit) {
		this.em.merge(visit);
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Visit> findByPetId(Integer petId) { 
		Query query = this.em.createQuery("SELECT visit FROM Visit v where v.pets.id= :id");
		query.setParameter("id", petId);
		return query.getResultList();
	}

}
