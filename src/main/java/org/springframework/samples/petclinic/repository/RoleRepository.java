package org.springframework.samples.petclinic.repository;

import org.springframework.samples.petclinic.model.common.Role;
import org.springframework.data.repository.Repository;

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
	 * @param roleId the id to search for
	 * @return the {@link Role} if found
	 */
	Role findById(Integer roleId);

	/**
	 * Retrieve a {@link Role} from the data store by id.
	 * @param name the name to search for
	 * @return the {@link Role} if found
	 */
	Role findByName(String name);

	/**
	 * Retrieve all {@link Role}s from the data store
	 * @return a Collection of {@link Role}s (or an empty Collection if none
	 */
	Collection<Role> findAll();

	/**
	 * Save a {@link Role} to the data store, either inserting or updating it.
	 * @param role the {@link Role} to save
	 */
	Role save(Role role);

}
