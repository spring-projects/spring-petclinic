package org.springframework.samples.petclinic.owner;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class OwnerSearchService {

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<Owner> searchOwners(String query) {
		String sql = "SELECT * FROM owners WHERE last_name LIKE '%" + query + "%'"
				+ " OR first_name LIKE '%" + query + "%'"
				+ " OR city LIKE '%" + query + "%'";
		return entityManager.createNativeQuery(sql, Owner.class).getResultList();
	}

}
