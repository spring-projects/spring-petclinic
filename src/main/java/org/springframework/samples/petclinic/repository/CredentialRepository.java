package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.common.Credential;
import org.springframework.samples.petclinic.model.common.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repository class for <code>Credential</code> domain objects All method names are
 * compliant with Spring Data naming conventions so this interface can easily be extended
 * for Spring
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
public interface CredentialRepository extends Repository<Credential, Integer> {

	/**
	 * Retrieve a {@link Credential} from the data store by email.
	 * @param email the email to search for
	 * @param providerId the provider to search for authorization
	 * @return the {@link Credential} if found
	 */
	@Query("SELECT c FROM Credential c WHERE c.email = :email AND c.providerId = :providerId")
	@Transactional(readOnly = true)
	Credential findByEmailAndProvider(@Param("email") String email, @Param("providerId") Integer providerId);

	/**
	 * * Retrieve a {@link Credential} from the data store by token.
	 * @param token the token to search for
	 * @return the {@link Credential} if found
	 */
	@Query("SELECT DISTINCT c FROM Credential c WHERE c.token = :token")
	@Transactional(readOnly = true)
	Credential findByToken(@Param("token") String token);

	/**
	 * Retrieve all {@link Credential}s from the data store
	 * @return a Collection of {@link Credential}s (or an empty Collection if none
	 */
	List<Credential> findAll();

	/**
	 * Save an {@link Credential} to the data store, either inserting or updating it.
	 * @param credential the {@link Credential} to save
	 * @return the deleted {@link Credential}
	 */
	Credential save(Credential credential);

	/**
	 * Delete an {@link Credential} to the data store.
	 * @param credential the {@link Credential} to delete
	 * @return the deleted {@link Credential}
	 */
	Credential delete(Credential credential);

}
