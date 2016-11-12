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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.PetTypeRepositoryExt;
import org.springframework.stereotype.Repository;

/**
 * @author Vitaliy Fedoriv
 *
 */

@Repository
@Qualifier("PetTypeRepositoryExt")
public class JdbcPetTypeRepositoryExtImpl implements PetTypeRepositoryExt {
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	private SimpleJdbcInsert insertPetType;
	
	@Autowired
	public JdbcPetTypeRepositoryExtImpl(DataSource dataSource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		this.insertPetType = new SimpleJdbcInsert(dataSource)
	            .withTableName("types")
	            .usingGeneratedKeyColumns("id");
	}

	@Override
	public PetType findById(int id) {
		PetType petType;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            petType = this.namedParameterJdbcTemplate.queryForObject(
                "SELECT id, name FROM types WHERE id= :id",
                params,
                BeanPropertyRowMapper.newInstance(PetType.class));
        } catch (EmptyResultDataAccessException ex) {
            throw new ObjectRetrievalFailureException(PetType.class, id);
        }
        return petType;
	}

	@Override
	public Collection<PetType> findAll() throws DataAccessException {
		Map<String, Object> params = new HashMap<>();
        return this.namedParameterJdbcTemplate.query(
            "SELECT id, name FROM types",
            params,
            BeanPropertyRowMapper.newInstance(PetType.class));
	}

	@Override
	public void save(PetType petType) throws DataAccessException {
		BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(petType);
		if (petType.isNew()) {
            Number newKey = this.insertPetType.executeAndReturnKey(parameterSource);
            petType.setId(newKey.intValue());
        } else {
            this.namedParameterJdbcTemplate.update("UPDATE types SET name=:name WHERE id=:id",
                parameterSource);
        }
	}

	@Override
	public void delete(PetType petType) throws DataAccessException {
		Map<String, Object> pettype_params = new HashMap<>();
		pettype_params.put("id", petType.getId());
		List<Pet> pets = new ArrayList<Pet>();
		pets = this.namedParameterJdbcTemplate.
    			query("SELECT pets.id, name, birth_date, type_id, owner_id FROM pets WHERE type_id=:id",
    			pettype_params,
    			BeanPropertyRowMapper.newInstance(Pet.class));
		// cascade delete pets
		for (Pet pet : pets){
			Map<String, Object> pet_params = new HashMap<>();
			pet_params.put("id", pet.getId());
			List<Visit> visits = new ArrayList<Visit>();
			visits = this.namedParameterJdbcTemplate.query(
		            "SELECT id, pet_id, visit_date, description FROM visits WHERE pet_id = :id",
		            pet_params,
		            BeanPropertyRowMapper.newInstance(Visit.class));
	        // cascade delete visits
	        for (Visit visit : visits){
	        	Map<String, Object> visit_params = new HashMap<>();
	        	visit_params.put("id", visit.getId());
	        	this.namedParameterJdbcTemplate.update("DELETE FROM visits WHERE id=:id", visit_params);
	        }
	        this.namedParameterJdbcTemplate.update("DELETE FROM pets WHERE id=:id", pet_params);
        }
        this.namedParameterJdbcTemplate.update("DELETE FROM types WHERE id=:id", pettype_params);
	}

}
