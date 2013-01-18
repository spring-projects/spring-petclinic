package org.springframework.samples.petclinic.repository.jdbc;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.samples.petclinic.Owner;
import org.springframework.samples.petclinic.Pet;
import org.springframework.samples.petclinic.PetType;
import org.springframework.samples.petclinic.Visit;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @author Sam Brannen
 * @author Thomas Risberg
 * @author Mark Fisher
 */
@Repository
public class JdbcPetRepositoryImpl implements PetRepository {

	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private SimpleJdbcInsert insertPet;
	
	@Autowired
	private OwnerRepository ownerRepository;
	
	@Autowired
	private VisitRepository visitRepository;

	@Autowired
	public void init(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

		this.insertPet = new SimpleJdbcInsert(dataSource)
			.withTableName("pets")
			.usingGeneratedKeyColumns("id");
	}

	public List<PetType> findPetTypes() throws DataAccessException {
		return this.jdbcTemplate.query(
				"SELECT id, name FROM types ORDER BY name",
				ParameterizedBeanPropertyRowMapper.newInstance(PetType.class));
	}

	public Pet findById(int id) throws DataAccessException {
		JdbcPet pet;
		try {
			pet = this.jdbcTemplate.queryForObject(
					"SELECT id, name, birth_date, type_id, owner_id FROM pets WHERE id=?",
					new JdbcPetRowMapper(),
					id);
		}
		catch (EmptyResultDataAccessException ex) {
			throw new ObjectRetrievalFailureException(Pet.class, new Integer(id));
		}
		Owner owner = this.ownerRepository.findById(pet.getOwnerId());
		owner.addPet(pet);
		pet.setType(EntityUtils.getById(findPetTypes(), PetType.class, pet.getTypeId()));
		
		List<Visit> visits = this.visitRepository.findByPetId(pet.getId());
		for (Visit visit : visits) {
			pet.addVisit(visit);
		}
		return pet;
	}

	public void save(Pet pet) throws DataAccessException {
		if (pet.isNew()) {
			Number newKey = this.insertPet.executeAndReturnKey(
					createPetParameterSource(pet));
			pet.setId(newKey.intValue());
		}
		else {
			this.namedParameterJdbcTemplate.update(
					"UPDATE pets SET name=:name, birth_date=:birth_date, type_id=:type_id, " +
					"owner_id=:owner_id WHERE id=:id",
					createPetParameterSource(pet));
		}
	}

	/**
	 * Creates a {@link MapSqlParameterSource} based on data values from the
	 * supplied {@link Pet} instance.
	 */
	private MapSqlParameterSource createPetParameterSource(Pet pet) {
		return new MapSqlParameterSource()
			.addValue("id", pet.getId())
			.addValue("name", pet.getName())
			.addValue("birth_date", pet.getBirthDate())
			.addValue("type_id", pet.getType().getId())
			.addValue("owner_id", pet.getOwner().getId());
	}

}
