package org.springframework.samples.petclinic.repository.jdbc;

import java.util.Collection;
import java.util.HashMap;
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
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.repository.SpecialtyRepositoryExt;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("SpecialtyRepositoryExt")
public class JdbcSpecialtyRepositoryExtImpl implements SpecialtyRepositoryExt {
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	private SimpleJdbcInsert insertSpecialty;

	@Autowired
	public JdbcSpecialtyRepositoryExtImpl(DataSource dataSource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		this.insertSpecialty = new SimpleJdbcInsert(dataSource)
	            .withTableName("specialties")
	            .usingGeneratedKeyColumns("id");
	}

	@Override
	public Specialty findById(int id) {
		Specialty specialty;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            specialty = this.namedParameterJdbcTemplate.queryForObject(
                "SELECT id, name FROM specialties WHERE id= :id",
                params,
                BeanPropertyRowMapper.newInstance(Specialty.class));
        } catch (EmptyResultDataAccessException ex) {
            throw new ObjectRetrievalFailureException(Specialty.class, id);
        }
        return specialty;
	}

	@Override
	public Collection<Specialty> findAll() throws DataAccessException {
		Map<String, Object> params = new HashMap<>();
        return this.namedParameterJdbcTemplate.query(
            "SELECT id, name FROM specialties",
            params,
            BeanPropertyRowMapper.newInstance(Specialty.class));
	}

	@Override
	public void save(Specialty specialty) throws DataAccessException {
		BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(specialty);
		if (specialty.isNew()) {
            Number newKey = this.insertSpecialty.executeAndReturnKey(parameterSource);
            specialty.setId(newKey.intValue());
        } else {
            this.namedParameterJdbcTemplate.update("UPDATE specialties SET name=:name WHERE id=:id",
                parameterSource);
        }

	}

	@Override
	public void delete(Specialty specialty) throws DataAccessException {
		Map<String, Object> params = new HashMap<>();
        params.put("id", specialty.getId());
        this.namedParameterJdbcTemplate.update("DELETE FROM specialties WHERE id=:id", params);
	}

}
