package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.Vet;

/**
 * The high-level PetClinic business interface.
 *
 * <p>This is basically a data access object.
 * PetClinic doesn't have a dedicated business facade.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 */
public interface VetRepository {

	/**
	 * Retrieve all <code>Vet</code>s from the data store.
	 * @return a <code>Collection</code> of <code>Vet</code>s
	 */
	Collection<Vet> findAll() throws DataAccessException;


}
