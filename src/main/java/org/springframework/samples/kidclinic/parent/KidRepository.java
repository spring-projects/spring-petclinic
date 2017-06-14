/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.kidclinic.parent;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository class for <code>Kid</code> domain objects All method names are compliant with Spring Data naming
 * conventions so this interface can easily be extended for Spring Data See here: http://static.springsource.org/spring-data/jpa/docs/current/reference/html/jpa.repositories.html#jpa.query-methods.query-creation
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 */
public interface KidRepository extends Repository<Kid, Integer> {

    /**
     * Retrieve all {@link KidGender}s from the data store.
     * @return a Collection of {@link KidGender}s.
     */
    @Query("SELECT ptype FROM KidGender ptype ORDER BY ptype.name")
    @Transactional(readOnly = true)
    List<KidGender> findKidGenders();

    /**
     * Retrieve a {@link Kid} from the data store by id.
     * @param id the id to search for
     * @return the {@link Kid} if found
     */
    @Transactional(readOnly = true)
    Kid findById(Integer id);

    /**
     * Save a {@link Kid} to the data store, either inserting or updating it.
     * @param kid the {@link Kid} to save
     */
    void save(Kid kid);

}

