package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.common.AuthProvider;

import java.util.List;

/**
 * Repository class for <code>AuthProvider</code> domain objects All method names are
 * compliant with Spring Data naming conventions so this interface can easily be extended
 * for Spring
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
public interface AuthProviderRepository extends Repository<AuthProvider, Integer> {

	/**
	 * Retrieve a {@link AuthProvider} from the data store by id.
	 * @param providerId the id to search for
	 * @return the {@link AuthProvider} if found
	 */
	AuthProvider findById(Integer providerId);

	/**
	 * Retrieve a {@link AuthProvider} from the data store by id.
	 * @param providerName the name to search for
	 * @return the {@link AuthProvider} if found
	 */
	AuthProvider findByName(String providerName);

	/**
	 * Retrieve all {@link AuthProvider}s from the data store
	 * @return a Collection of {@link AuthProvider}s (or an empty Collection if none
	 */
	List<AuthProvider> findAll();

	/**
	 * Save a {@link AuthProvider} to the data store, either inserting or updating it.
	 * @param authProvider the {@link AuthProvider} to save
	 */
	AuthProvider save(AuthProvider authProvider);

}
