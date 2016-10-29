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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.VisitRepositoryExt;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("VisitRepositoryExt")
public class JdbcVisitRepositoryExtImpl extends JdbcVisitRepositoryImpl implements VisitRepositoryExt {
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public JdbcVisitRepositoryExtImpl(DataSource dataSource) {
		super(dataSource);
		// TODO Auto-generated constructor stub
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public Visit findById(int id) throws DataAccessException {
		Visit visit;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id", id);
            visit = this.namedParameterJdbcTemplate.queryForObject(
                "SELECT id, pet_id, visit_date, description FROM visits WHERE id= :id",
                params,
                BeanPropertyRowMapper.newInstance(Visit.class));
        } catch (EmptyResultDataAccessException ex) {
            throw new ObjectRetrievalFailureException(Visit.class, id);
        }
        return visit;
	}

	@Override
	public Collection<Visit> findAll() throws DataAccessException {
		Map<String, Object> params = new HashMap<>();
        return this.namedParameterJdbcTemplate.query(
            "SELECT id, pet_id, visit_date, description FROM visits",
            params,
            BeanPropertyRowMapper.newInstance(Visit.class));
	}

	@Override
	public void delete(Visit visit) throws DataAccessException {
		Map<String, Object> params = new HashMap<>();
        params.put("id", visit.getId());
        this.namedParameterJdbcTemplate.update("DELETE FROM visits WHERE id=:id", params);
	}

}
