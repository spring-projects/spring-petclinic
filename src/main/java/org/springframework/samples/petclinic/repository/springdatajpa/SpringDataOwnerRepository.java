package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.Owner;
import org.springframework.samples.petclinic.repository.OwnerRepository;

/**
 *
 * @author Michael Isvy
 * @since 15.1.2013
 */
public interface SpringDataOwnerRepository extends OwnerRepository, Repository<Owner, Integer> {
	
	@Query("SELECT owner FROM Owner owner left join fetch owner.pets WHERE owner.lastName LIKE :lastName%")
	Collection<Owner> findByLastName(String lastName) throws DataAccessException;
}
