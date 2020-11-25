package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.common.User;

import java.util.Collection;
import java.util.List;

/**
 * Repository class for <code>User</code> domain objects All method names are compliant
 * with Spring Data naming conventions so this interface can easily be extended for Spring
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
public interface UserRepository extends Repository<User, Integer> {

	User findById(Integer id);

	User findByEmail(String email);

	Boolean existsByEmail(String email);

	/**
	 * Retrieve all {@link User}s from the data store
	 * @return a Collection of {@link User}s (or an empty Collection if none
	 */
	Collection<User> findAll();

	/**
	 * Save an {@link User} to the data store, either inserting or updating it.
	 * @param user the {@link User} to delete
	 * @return the deleted {@link User}
	 */
	User save(User user);

	/**
	 * Delete an {@link User} to the data store.
	 * @param user the {@link User} to delete
	 * @return the deleted {@link User}
	 */
	User delete(User user);

}
