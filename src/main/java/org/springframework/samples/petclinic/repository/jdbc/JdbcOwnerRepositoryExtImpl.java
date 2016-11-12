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

package org.springframework.samples.petclinic.repository.jdbc;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.OwnerRepositoryExt;
import org.springframework.stereotype.Repository;

/**
 * @author Vitaliy Fedoriv
 *
 */

@Repository
@Qualifier("OwnerRepositoryExt")
public class JdbcOwnerRepositoryExtImpl extends JdbcOwnerRepositoryImpl implements OwnerRepositoryExt {
	
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  	@Autowired
	public JdbcOwnerRepositoryExtImpl(DataSource dataSource) {
		super(dataSource);
	    this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public Collection<Owner> findAll() throws DataAccessException {
		List<Owner> owners = this.namedParameterJdbcTemplate.query(
	            "SELECT id, first_name, last_name, address, city, telephone FROM owners",
	            new HashMap<String, Object>(),
	            BeanPropertyRowMapper.newInstance(Owner.class));
		for (Owner owner : owners) {
            loadPetsAndVisits(owner);
        }
	    return owners;
	}

	@Override
	@Transactional
	public void delete(Owner owner) throws DataAccessException {
		Map<String, Object> owner_params = new HashMap<>();
		owner_params.put("id", owner.getId());
        List<Pet> pets = owner.getPets();
        // cascade delete pets
        for (Pet pet : pets){
        	Map<String, Object> pet_params = new HashMap<>();
        	pet_params.put("id", pet.getId());
        	// cascade delete visits
        	List<Visit> visits = pet.getVisits();
            for (Visit visit : visits){
            	Map<String, Object> visit_params = new HashMap<>();
            	visit_params.put("id", visit.getId());
            	this.namedParameterJdbcTemplate.update("DELETE FROM visits WHERE id=:id", visit_params);
            }
            this.namedParameterJdbcTemplate.update("DELETE FROM pets WHERE id=:id", pet_params);
        }
        this.namedParameterJdbcTemplate.update("DELETE FROM owners WHERE id=:id", owner_params);
	}

}
