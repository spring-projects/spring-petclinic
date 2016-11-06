package org.springframework.samples.petclinic.repository.jdbc;

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
import org.springframework.samples.petclinic.repository.OwnerRepositoryExt;
import org.springframework.stereotype.Repository;

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
	public void delete(Owner owner) throws DataAccessException {
		Map<String, Object> params = new HashMap<>();
        params.put("id", owner.getId());
        this.namedParameterJdbcTemplate.update("DELETE FROM owners WHERE id=:id", params);
	}

}
