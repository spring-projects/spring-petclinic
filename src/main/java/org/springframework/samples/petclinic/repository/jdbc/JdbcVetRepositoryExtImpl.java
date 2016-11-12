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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.VetRepositoryExt;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Repository;

/**
 * @author Vitaliy Fedoriv
 *
 */

@Repository
@Qualifier("VetRepositoryExt")
public class JdbcVetRepositoryExtImpl extends JdbcVetRepositoryImpl implements VetRepositoryExt {

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private SimpleJdbcInsert insertVet;

	@Autowired
	public JdbcVetRepositoryExtImpl(DataSource dataSource, JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate);
		this.insertVet = new SimpleJdbcInsert(dataSource).withTableName("vets").usingGeneratedKeyColumns("id");
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public Vet findById(int id) throws DataAccessException {
		Vet vet;
		try {
			Map<String, Object> vet_params = new HashMap<>();
			vet_params.put("id", id);
			vet = this.namedParameterJdbcTemplate.queryForObject(
					"SELECT id, first_name, last_name FROM vets WHERE id= :id",
					vet_params,
					BeanPropertyRowMapper.newInstance(Vet.class));

			final List<Specialty> specialties = this.namedParameterJdbcTemplate.query(
					"SELECT id, name FROM specialties", vet_params, BeanPropertyRowMapper.newInstance(Specialty.class));

			final List<Integer> vetSpecialtiesIds = this.namedParameterJdbcTemplate.query(
					"SELECT specialty_id FROM vet_specialties WHERE vet_id=:id",
					vet_params,
					new BeanPropertyRowMapper<Integer>() {
						@Override
						public Integer mapRow(ResultSet rs, int row) throws SQLException {
							return rs.getInt(1);
						}
					});
			for (int specialtyId : vetSpecialtiesIds) {
				Specialty specialty = EntityUtils.getById(specialties, Specialty.class, specialtyId);
				vet.addSpecialty(specialty);
			}

		} catch (EmptyResultDataAccessException ex) {
			throw new ObjectRetrievalFailureException(Vet.class, id);
		}
		return vet;
	}

	@Override
	public void save(Vet vet) throws DataAccessException {
		BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(vet);
		if (vet.isNew()) {
			Number newKey = this.insertVet.executeAndReturnKey(parameterSource);
			vet.setId(newKey.intValue());
		} else {
			this.namedParameterJdbcTemplate
					.update("UPDATE vets SET first_name=:firstName, last_name=:lastName WHERE id=:id", parameterSource);
		}
	}

	@Override
	public void delete(Vet vet) throws DataAccessException {
		Map<String, Object> params = new HashMap<>();
		params.put("id", vet.getId());
		this.namedParameterJdbcTemplate.update("DELETE FROM vets WHERE id=:id", params);
	}

}
