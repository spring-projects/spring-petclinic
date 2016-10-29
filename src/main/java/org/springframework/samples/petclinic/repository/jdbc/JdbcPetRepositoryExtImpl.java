package org.springframework.samples.petclinic.repository.jdbc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.OwnerRepositoryExt;
import org.springframework.samples.petclinic.repository.PetRepositoryExt;
import org.springframework.samples.petclinic.repository.VisitRepositoryExt;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier("PetRepositoryExt")
public class JdbcPetRepositoryExtImpl extends JdbcPetRepositoryImpl implements PetRepositoryExt {
	
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
    public JdbcPetRepositoryExtImpl(DataSource dataSource,
    		@Qualifier("OwnerRepositoryExt") OwnerRepositoryExt ownerRepository,
    		@Qualifier("VisitRepositoryExt") VisitRepositoryExt visitRepository) {
		super(dataSource, ownerRepository, visitRepository);
		// TODO  super () ?
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public Collection<Pet> findAll() throws DataAccessException {
		Map<String, Object> params = new HashMap<>();
        return this.namedParameterJdbcTemplate.query(
            "SELECT id, name, birth_date, type_id, owner_id FROM pets",
            params,
            BeanPropertyRowMapper.newInstance(Pet.class));
	}

	@Override
	public void delete(Pet pet) throws DataAccessException {
		Map<String, Object> params = new HashMap<>();
        params.put("id", pet.getId());
        this.namedParameterJdbcTemplate.update("DELETE FROM pets WHERE id=:id", params);
	}

}
