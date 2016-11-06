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
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.PetTypeRepositoryExt;
import org.springframework.stereotype.Repository;

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
		Map<String, Object> params = new HashMap<>();
        params.put("id", petType.getId());
        this.namedParameterJdbcTemplate.update("DELETE FROM types WHERE id=:id", params);
	}

}
