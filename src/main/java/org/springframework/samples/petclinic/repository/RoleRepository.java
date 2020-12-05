package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.common.Role;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Repository class for <code>Role</code> domain objects All method names are compliant
 * with Spring Data naming conventions so this interface can easily be extended for Spring
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
public interface RoleRepository extends Repository<Role, Integer> {

	/**
	 * Retrieve a {@link Role} from the data store by id.
	 * @param id the id to search for
	 * @return the {@link Role} if found
	 */
	@Query("SELECT role FROM Role role join fetch role.privileges WHERE role.id =:id")
	@Transactional(readOnly = true)
	Role findById(@Param("id") Integer id);

	/**
	 * Retrieve a {@link Role} from the data store by id.
	 * @param name the name to search for
	 * @return the {@link Role} if found
	 */
	@Query("SELECT role FROM Role role left join fetch role.users WHERE role.name =:name")
	@Transactional(readOnly = true)
	Role findByName(@Param("name") String name);

	/**
	 * Retrieve all {@link Role}s from the data store
	 * @return a Collection of {@link Role}s (or an empty Collection if none
	 */
	Collection<Role> findAll();

	/**
	 * Save a {@link Role} to the data store, either inserting or updating it.
	 * @param role the {@link Role} to save
	 * @return the {@link Role} saved
	 */
	Role save(Role role);

	/**
	 * Delete a {@link Role} to the data store.
	 * @param role the {@link Role} to delete
	 * @return the {@link Role} deleted
	 */
	Role delete(Role role);

}
