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

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository class for <code>Parent</code> domain objects All method names are compliant with Spring Data naming
 * conventions so this interface can easily be extended for Spring Data See here: http://static.springsource.org/spring-data/jpa/docs/current/reference/html/jpa.repositories.html#jpa.query-methods.query-creation
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 */
public interface ParentRepository extends Repository<Parent, Integer> {

    /**
     * Retrieve {@link Parent}s from the data store by last name, returning all parents
     * whose last name <i>starts</i> with the given name.
     * @param lastName Value to search for
     * @return a Collection of matching {@link Parent}s (or an empty Collection if none
     * found)
     */
    @Query("SELECT DISTINCT parent FROM Parent parent left join fetch parent.kids WHERE parent.lastName LIKE :lastName%")
    @Transactional(readOnly = true)
    Collection<Parent> findByLastName(@Param("lastName") String lastName);

    /**
     * Retrieve an {@link Parent} from the data store by id.
     * @param id the id to search for
     * @return the {@link Parent} if found
     */
    @Query("SELECT parent FROM Parent parent left join fetch parent.kids WHERE parent.id =:id")
    @Transactional(readOnly = true)
    Parent findById(@Param("id") Integer id);

    /**
     * Save an {@link Parent} to the data store, either inserting or updating it.
     * @param owner the {@link Parent} to save
     */
    void save(Parent parent);


}
