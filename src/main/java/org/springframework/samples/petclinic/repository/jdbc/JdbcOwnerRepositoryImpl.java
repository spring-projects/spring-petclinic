package org.springframework.samples.petclinic.repository.jdbc;

import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.samples.petclinic.Owner;
import org.springframework.samples.petclinic.Pet;
import org.springframework.samples.petclinic.PetType;
import org.springframework.samples.petclinic.Visit;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * A simple JDBC-based implementation of the {@link ClinicService} interface.
 *
 * <p>This class uses Java 5 language features and the {@link SimpleJdbcTemplate}
 * plus {@link SimpleJdbcInsert}. It also takes advantage of classes like
 * {@link BeanPropertySqlParameterSource} and
 * {@link ParameterizedBeanPropertyRowMapper} which provide automatic mapping
 * between JavaBean properties and JDBC parameters or query results.
 *
 * <p>JdbcClinicImpl is a rewrite of the AbstractJdbcClinic which was the base
 * class for JDBC implementations of the ClinicService interface for Spring 2.0.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @author Sam Brannen
 * @author Thomas Risberg
 * @author Mark Fisher
 */
@Service
public class JdbcOwnerRepositoryImpl implements OwnerRepository {

	@Autowired
	private PetRepository petRepository;
	
	@Autowired
	private VisitRepository visitRepository;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private SimpleJdbcInsert insertOwner;

	@Autowired
	public void init(DataSource dataSource) {

		this.insertOwner = new SimpleJdbcInsert(dataSource)
			.withTableName("owners")
			.usingGeneratedKeyColumns("id");
	}


	

	/**
	 * Loads {@link Owner Owners} from the data store by last name, returning
	 * all owners whose last name <i>starts</i> with the given name; also loads
	 * the {@link Pet Pets} and {@link Visit Visits} for the corresponding
	 * owners, if not already loaded.
	 */
	@Transactional(readOnly = true)
	public Collection<Owner> findByLastName(String lastName) throws DataAccessException {
		List<Owner> owners = this.jdbcTemplate.query(
				"SELECT id, first_name, last_name, address, city, telephone FROM owners WHERE last_name like ?",
				ParameterizedBeanPropertyRowMapper.newInstance(Owner.class),
				lastName + "%");
		loadOwnersPetsAndVisits(owners);
		return owners;
	}

	/**
	 * Loads the {@link Owner} with the supplied <code>id</code>; also loads
	 * the {@link Pet Pets} and {@link Visit Visits} for the corresponding
	 * owner, if not already loaded.
	 */
	@Transactional(readOnly = true)
	public Owner findById(int id) throws DataAccessException {
		Owner owner;
		try {
			owner = this.jdbcTemplate.queryForObject(
					"SELECT id, first_name, last_name, address, city, telephone FROM owners WHERE id=?",
					ParameterizedBeanPropertyRowMapper.newInstance(Owner.class),
					id);
		}
		catch (EmptyResultDataAccessException ex) {
			throw new ObjectRetrievalFailureException(Owner.class, new Integer(id));
		}
		loadPetsAndVisits(owner);
		return owner;
	}
	
	public void loadPetsAndVisits(final Owner owner) {
		final List<JdbcPet> pets = this.jdbcTemplate.query(
				"SELECT id, name, birth_date, type_id, owner_id FROM pets WHERE owner_id=?",
				new JdbcPetRowMapper(),
				owner.getId().intValue());
		for (JdbcPet pet : pets) {
			owner.addPet(pet);
			pet.setType(EntityUtils.getById(getPetTypes(), PetType.class, pet.getTypeId()));
			List<Visit> visits = this.visitRepository.findByPetId(pet.getId());
			for (Visit visit : visits) {
				pet.addVisit(visit);
			}
		}
	}

	

	@Transactional
	public void save(Owner owner) throws DataAccessException {
		if (owner.isNew()) {
			Number newKey = this.insertOwner.executeAndReturnKey(
					new BeanPropertySqlParameterSource(owner));
			owner.setId(newKey.intValue());
		}
		else {
			this.namedParameterJdbcTemplate.update(
					"UPDATE owners SET first_name=:firstName, last_name=:lastName, address=:address, " +
					"city=:city, telephone=:telephone WHERE id=:id",
					new BeanPropertySqlParameterSource(owner));
		}
	}

	


	
	@Transactional(readOnly = true)
	public Collection<PetType> getPetTypes() throws DataAccessException {
		return this.jdbcTemplate.query(
				"SELECT id, name FROM types ORDER BY name",
				ParameterizedBeanPropertyRowMapper.newInstance(PetType.class));
	}

	/**
	 * Loads the {@link Pet} and {@link Visit} data for the supplied
	 * {@link List} of {@link Owner Owners}.
	 *
	 * @param owners the list of owners for whom the pet and visit data should be loaded
	 * @see #loadPetsAndVisits(Owner)
	 */
	private void loadOwnersPetsAndVisits(List<Owner> owners) {
		for (Owner owner : owners) {
			loadPetsAndVisits(owner);
		}
	}


}
