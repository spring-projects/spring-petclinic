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
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.OwnerRepositoryExt;
import org.springframework.samples.petclinic.repository.PetRepositoryExt;
import org.springframework.samples.petclinic.repository.VisitRepositoryExt;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Repository;

/**
 * @author Vitaliy Fedoriv
 *
 */

@Repository
@Qualifier("PetRepositoryExt")
public class JdbcPetRepositoryExtImpl extends JdbcPetRepositoryImpl implements PetRepositoryExt {
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
    public JdbcPetRepositoryExtImpl(DataSource dataSource,
    		@Qualifier("OwnerRepositoryExt") OwnerRepositoryExt ownerRepository,
    		@Qualifier("VisitRepositoryExt") VisitRepositoryExt visitRepository) {
		super(dataSource, ownerRepository, visitRepository);
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public Collection<Pet> findAll() throws DataAccessException {
		Map<String, Object> params = new HashMap<>();
		Collection<Pet> pets = new ArrayList<Pet>();
		Collection<JdbcPet> jdbcPets = new ArrayList<JdbcPet>();
		jdbcPets = this.namedParameterJdbcTemplate.
    			query("SELECT pets.id, name, birth_date, type_id, owner_id FROM pets",
        		params,
        		new JdbcPetRowMapper());
		Collection<PetType> petTypes = this.namedParameterJdbcTemplate.query(
	            "SELECT id, name FROM types ORDER BY name", new HashMap<String, Object>(),
	            BeanPropertyRowMapper.newInstance(PetType.class));
		Collection<Owner> owners = this.namedParameterJdbcTemplate.query(
	            "SELECT id, first_name, last_name, address, city, telephone FROM owners ORDER BY last_name", new HashMap<String, Object>(),
	            BeanPropertyRowMapper.newInstance(Owner.class));
		for(JdbcPet jdbcPet : jdbcPets){
			jdbcPet.setType(EntityUtils.getById(petTypes, PetType.class, jdbcPet.getTypeId()));
			jdbcPet.setOwner(EntityUtils.getById(owners, Owner.class, jdbcPet.getOwnerId()));
			pets.add(jdbcPet);
		}
		return pets;
	}

	@Override
	public void delete(Pet pet) throws DataAccessException {
		Map<String, Object> pet_params = new HashMap<>();
		pet_params.put("id", pet.getId());
        List<Visit> visits = pet.getVisits();
        // cascade delete visits
        for (Visit visit : visits){
        	Map<String, Object> visit_params = new HashMap<>();
        	visit_params.put("id", visit.getId());
        	this.namedParameterJdbcTemplate.update("DELETE FROM visits WHERE id=:id", visit_params);
        }
        this.namedParameterJdbcTemplate.update("DELETE FROM pets WHERE id=:id", pet_params);
	}

}
