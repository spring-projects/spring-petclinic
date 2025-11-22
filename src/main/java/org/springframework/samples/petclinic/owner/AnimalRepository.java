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
package org.springframework.samples.petclinic.animal;

import java.util.Optional;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.petclinic.model.Animal;

/**
 * Repository class for {@link Animal} domain objects. All method names follow
 * Spring Data JPA naming conventions. See:
 * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation
 *
 * Provides operations for querying Animals by their attributes.
 * 
 * Author list follows PetClinic conventions.
 * 
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Wick Dynex
 */
public interface AnimalRepository extends JpaRepository<Animal, Integer> {

	/**
	 * Retrieve {@link Animal}s from the data store by species, returning all animals
	 * whose species <i>starts</i> with the given value.
	 * @param species value to search for
	 * @return a Page of matching {@link Animal}s (or an empty page if none found)
	 */
	Page<Animal> findBySpeciesStartingWith(String species, Pageable pageable);

	/**
	 * Retrieve an {@link Animal} from the data store by id.
	 * <p>
	 * This method returns an {@link Optional} containing the {@link Animal} if found.
	 * If no animal is found with the provided id, it returns an empty {@link Optional}.
	 * </p>
	 * @param id the id to search for
	 * @return an {@link Optional} containing the {@link Animal} if found, or empty otherwise
	 * @throws IllegalArgumentException if the id is null
	 */
	Optional<Animal> findById(Integer id);

}
