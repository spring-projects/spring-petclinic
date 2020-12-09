package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.common.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Repository class for <code>User</code> domain objects All method names are compliant
 * with Spring Data naming conventions so this interface can easily be extended for Spring
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
public interface UserRepository extends Repository<User, Integer> {

	/**
	 * Retrieve an {@link User} from the data store by email.
	 * @param id the id to search for
	 * @return the {@link User} if found
	 */
	@Query("SELECT user FROM User user left join fetch user.roles WHERE user.id =:id")
	@Transactional(readOnly = true)
	User findById(@Param("id") Integer id);

	/**
	 * Retrieve an {@link User} from the data store by email.
	 * @param email the email to search for
	 * @return the {@link User} if found
	 */
	@Query("SELECT user FROM User user left join fetch user.roles WHERE user.email =:email")
	@Transactional(readOnly = true)
	User findByEmail(@Param("email") String email);

	Boolean existsByEmail(String email);

	/**
	 * Retrieve all {@link User}s from the data store
	 * @return a Collection of {@link User}s (or an empty Collection if none
	 */
	Collection<User> findAll();

	/**
	 * Save an {@link User} to the data store, either inserting or updating it.
	 * @param user the {@link User} to save
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
