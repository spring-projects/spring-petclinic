/*
 * Copyright 2012-2019 the original author or authors.
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
package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.business.Specialty;

import java.util.List;

/**
 * Repository class for <code>Speciality</code> domain objects All method names are
 * compliant with Spring Data naming conventions so this interface can easily be extended
 * for Spring
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
public interface SpecialtyRepository extends Repository<Specialty, Integer> {

	/**
	 * Retrieve a {@link Specialty} from the data store by id.
	 * @param id the id to search for
	 * @return the {@link Specialty} if found
	 */
	Specialty findById(Integer id);

	/**
	 * Retrieve all {@link Specialty}s from the data store
	 * @return a Collection of {@link Specialty}s (or an empty Collection if none
	 */
	List<Specialty> findAll();

	/**
	 * Save a {@link Specialty} to the data store, either inserting or updating it.
	 * @param specialty the {@link Specialty} to save
	 */
	Specialty save(Specialty specialty);

}
