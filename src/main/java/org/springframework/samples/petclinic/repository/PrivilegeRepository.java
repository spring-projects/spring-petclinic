package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.common.Privilege;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Repository class for <code>Privilege</code> domain objects All method names are
 * compliant with Spring Data naming conventions so this interface can easily be extended
 * for Spring
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
public interface PrivilegeRepository extends Repository<Privilege, Integer> {

	/**
	 * Retrieve a {@link Privilege} from the data store by id.
	 * @param id the id to search for
	 * @return the {@link Privilege} if found
	 */
	@Query("SELECT privilege FROM Privilege privilege join fetch privilege.roles WHERE privilege.id =:id")
	@Transactional(readOnly = true)
	Privilege findById(@Param("id") Integer id);

	/**
	 * Retrieve a {@link Privilege} from the data store by id.
	 * @param name the name to search for
	 * @return the {@link Privilege} if found
	 */
	@Query("SELECT privilege FROM Privilege privilege left join fetch privilege.roles WHERE privilege.name =:name")
	@Transactional(readOnly = true)
	Privilege findByName(@Param("name") String name);

	/**
	 * Retrieve all {@link Privilege}s from the data store
	 * @return a Collection of {@link Privilege}s (or an empty Collection if none
	 */
	Collection<Privilege> findAll();

	/**
	 * Save a {@link Privilege} to the data store, either inserting or updating it.
	 * @param privilege the {@link Privilege} to save
	 * @return the {@link Privilege} saved
	 */
	Privilege save(Privilege privilege);

}
