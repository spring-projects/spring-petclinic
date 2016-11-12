/*
 * Copyright 2016 the original author or authors.
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

package org.springframework.samples.petclinic.repository.jpa;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.VisitRepositoryExt;
import org.springframework.stereotype.Repository;

/**
 * @author Vitaliy Fedoriv
 *
 */

@Repository
@Qualifier("VisitRepositoryExt")
public class JpaVisitRepositoryExtImpl extends JpaVisitRepositoryImpl implements VisitRepositoryExt {
	
    @PersistenceContext
    private EntityManager em;

	@Override
	public Visit findById(int id) throws DataAccessException {
		return this.em.find(Visit.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Visit> findAll() throws DataAccessException {
        return this.em.createQuery("SELECT v FROM Visit v").getResultList();
	}

	@Override
	public void delete(Visit visit) throws DataAccessException {
		String visitId = visit.getId().toString();
		this.em.createQuery("DELETE FROM Visit visit WHERE id=" + visitId).executeUpdate();
		if (em.contains(visit)) {
			em.remove(visit);
		}
	}

}
