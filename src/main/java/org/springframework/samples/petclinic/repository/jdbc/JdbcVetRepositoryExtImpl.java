package org.springframework.samples.petclinic.repository.jdbc;

import java.util.HashMap;
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
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.VetRepositoryExt;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("VetRepositoryExt")
public class JdbcVetRepositoryExtImpl extends JdbcVetRepositoryImpl implements VetRepositoryExt {
	
	//private JdbcTemplate jdbcTemplate;
	
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private SimpleJdbcInsert insertVet;

	@Autowired
	public JdbcVetRepositoryExtImpl(DataSource dataSource, JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate);
		// TODO Auto-generated constructor stub
		//this.jdbcTemplate = jdbcTemplate;
		
        this.insertVet = new SimpleJdbcInsert(dataSource)
                .withTableName("vets")
                .usingGeneratedKeyColumns("id");
        
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public Vet findById(int id) throws DataAccessException {
        Vet vet;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            vet = this.namedParameterJdbcTemplate.queryForObject(
                "SELECT id, first_name, last_name FROM vets WHERE id= :id",
                params,
                BeanPropertyRowMapper.newInstance(Vet.class));
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
            this.namedParameterJdbcTemplate.update(
                "UPDATE vets SET first_name=:firstName, last_name=:lastName WHERE id=:id",
                parameterSource);
        }
	}

	@Override
	public void delete(Vet vet) throws DataAccessException {
		Map<String, Object> params = new HashMap<>();
        params.put("id", vet.getId());
        this.namedParameterJdbcTemplate.update("DELETE FROM vets WHERE id=:id", params);
	}

}
