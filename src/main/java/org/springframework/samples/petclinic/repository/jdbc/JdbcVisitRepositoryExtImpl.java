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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.VisitRepositoryExt;
import org.springframework.stereotype.Repository;

/**
 * @author Vitaliy Fedoriv
 *
 */

@Repository
@Qualifier("VisitRepositoryExt")
public class JdbcVisitRepositoryExtImpl extends JdbcVisitRepositoryImpl implements VisitRepositoryExt {
	
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	public JdbcVisitRepositoryExtImpl(DataSource dataSource) {
		super(dataSource);
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public Visit findById(int id) throws DataAccessException {
		Visit visit;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            visit = this.namedParameterJdbcTemplate.queryForObject(
                "SELECT id as visit_id, pet_id, visit_date, description FROM visits WHERE id= :id",
                params,
                new JdbcVisitRowMapperExt());
        } catch (EmptyResultDataAccessException ex) {
            throw new ObjectRetrievalFailureException(Visit.class, id);
        }
        return visit;
	}

	@Override
	public Collection<Visit> findAll() throws DataAccessException {
		Map<String, Object> params = new HashMap<>();
		return this.namedParameterJdbcTemplate.query(
            "SELECT id as visit_id, pets.id as pet_id, visit_date, description FROM visits LEFT JOIN pets ON visits.pet_id = pets.id",
            params,
            new JdbcVisitRowMapperExt());
	}
	
    @Override
    public void save(Visit visit) throws DataAccessException {
        if (visit.isNew()) {
            Number newKey = this.insertVisit.executeAndReturnKey(
                createVisitParameterSource(visit));
            visit.setId(newKey.intValue());
        } else {
        	this.namedParameterJdbcTemplate.update(
                    "UPDATE visits SET visit_date=:visit_date, description=:description, pet_id=:pet_id WHERE id=:id ",
                    createVisitParameterSource(visit));
        }
    }

	@Override
	public void delete(Visit visit) throws DataAccessException {
		Map<String, Object> params = new HashMap<>();
        params.put("id", visit.getId());
        this.namedParameterJdbcTemplate.update("DELETE FROM visits WHERE id=:id", params);
	}
	
	protected class JdbcVisitRowMapperExt implements RowMapper<Visit>{
		
		@Override
		public Visit mapRow(ResultSet rs, int rowNum) throws SQLException {
			Visit visit = new Visit();
			JdbcPet pet = new JdbcPet();
			visit.setId(rs.getInt("visit_id"));
		    Date visitDate = rs.getDate("visit_date");
		    visit.setDate(new Date(visitDate.getTime()));
		    visit.setDescription(rs.getString("description"));
		    Map<String, Object> params = new HashMap<>();
	        params.put("id", rs.getInt("pet_id"));
	        pet = JdbcVisitRepositoryExtImpl.this.namedParameterJdbcTemplate.
	        			queryForObject("SELECT pets.id, name, birth_date, type_id, owner_id FROM pets WHERE pets.id=:id",
	            		params,
	            		new JdbcPetRowMapper());
	        visit.setPet(pet);
		    return visit;
		}
	}
}
