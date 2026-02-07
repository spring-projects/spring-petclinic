/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository class for <code>Owner</code> domain objects. All method names are compliant
 * with Spring Data naming conventions so this interface can easily be extended for Spring
 * Data. See:
 * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Wick Dynex
 */
public interface OwnerRepository extends JpaRepository<Owner, Integer> {

	/**
	 * Retrieve {@link Owner}s from the data store by last name, returning all owners
	 * whose last name <i>starts</i> with the given name.
	 * @param lastName Value to search for
	 * @return a Collection of matching {@link Owner}s (or an empty Collection if none
	 * found)
	 */
	Page<Owner> findByLastNameStartingWith(String lastName, Pageable pageable);

	/**
	 * Internal method that retrieves raw data from the database. Use
	 * findOwnerSearchResultsByLastName() instead.
	 */
	@Query(value = """
			SELECT
				o.id,
				o.first_name,
				o.last_name,
				o.address,
				o.city,
				o.telephone,
				COALESCE(GROUP_CONCAT(p.name ORDER BY p.name SEPARATOR ', '), '')
			FROM owners o
			LEFT JOIN pets p ON o.id = p.owner_id
			WHERE LOWER(o.last_name) LIKE LOWER(CONCAT(:lastName, '%'))
			GROUP BY o.id, o.first_name, o.last_name, o.address, o.city, o.telephone
			""", countQuery = """
			SELECT COUNT(DISTINCT o.id)
			FROM owners o
			WHERE LOWER(o.last_name) LIKE LOWER(CONCAT(:lastName, '%'))
			""", nativeQuery = true)
	Page<Object[]> findOwnerSearchResultsRaw(@Param("lastName") String lastName, Pageable pageable);

	/**
	 * Efficiently retrieve owner search results with aggregated pet names using a single
	 * query. This uses a native query with GROUP_CONCAT to avoid N+1 queries.
	 *
	 * The results are projected into OwnerSearchResult DTOs.
	 * @param lastName Value to search for
	 * @param pageable Pagination parameters
	 * @return Page of OwnerSearchResult DTOs
	 */
	default Page<OwnerSearchResult> findOwnerSearchResultsByLastName(String lastName, Pageable pageable) {
		Page<Object[]> rawResults = findOwnerSearchResultsRaw(lastName, pageable);
		return rawResults.map(row -> new OwnerSearchResult(((Number) row[0]).intValue(), // id
				(String) row[1], // firstName
				(String) row[2], // lastName
				(String) row[3], // address
				(String) row[4], // city
				(String) row[5], // telephone
				(String) row[6] // petNames
		));
	}

	/**
	 * Retrieve an {@link Owner} from the data store by id.
	 * <p>
	 * This method returns an {@link Optional} containing the {@link Owner} if found. If
	 * no {@link Owner} is found with the provided id, it will return an empty
	 * {@link Optional}.
	 * </p>
	 * @param id the id to search for
	 * @return an {@link Optional} containing the {@link Owner} if found, or an empty
	 * {@link Optional} if not found.
	 * @throws IllegalArgumentException if the id is null (assuming null is not a valid
	 * input for id)
	 */
	Optional<Owner> findById(Integer id);

	/**
	 * Retrieve an {@link Owner} with pets eagerly loaded using EntityGraph. Use this when
	 * you need to display or work with the owner's pets.
	 * @param id the id to search for
	 * @return an {@link Optional} containing the {@link Owner} with pets loaded
	 */
	@EntityGraph(attributePaths = { "pets", "pets.type" })
	@Query("SELECT o FROM Owner o WHERE o.id = :id")
	Optional<Owner> findByIdWithPets(@Param("id") Integer id);

	/**
	 * Retrieve an {@link Owner} with complete data graph (pets, types, visits). Use this
	 * for detail views where all related data is needed.
	 * @param id the id to search for
	 * @return an {@link Optional} containing the {@link Owner} with complete data
	 */
	@EntityGraph(attributePaths = { "pets", "pets.type", "pets.visits" })
	@Query("SELECT o FROM Owner o WHERE o.id = :id")
	Optional<Owner> findByIdWithPetsAndVisits(@Param("id") Integer id);

}
