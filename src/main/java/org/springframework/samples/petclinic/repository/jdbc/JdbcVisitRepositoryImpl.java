package org.springframework.samples.petclinic.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.samples.petclinic.Pet;
import org.springframework.samples.petclinic.Visit;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.samples.petclinic.service.ClinicService;
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
public class JdbcVisitRepositoryImpl implements VisitRepository {

	private JdbcTemplate jdbcTemplate;

	private SimpleJdbcInsert insertVisit;

	@Autowired
	public void init(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);

		this.insertVisit = new SimpleJdbcInsert(dataSource)
			.withTableName("visits")
			.usingGeneratedKeyColumns("id");
	}


	@Transactional
	public void storeVisit(Visit visit) throws DataAccessException {
		if (visit.isNew()) {
			Number newKey = this.insertVisit.executeAndReturnKey(
					createVisitParameterSource(visit));
			visit.setId(newKey.intValue());
		}
		else {
			throw new UnsupportedOperationException("Visit update not supported");
		}
	}

	public void deletePet(int id) throws DataAccessException {
		this.jdbcTemplate.update("DELETE FROM pets WHERE id=?", id);
	}

	// END of ClinicService implementation section ************************************


	/**
	 * Creates a {@link MapSqlParameterSource} based on data values from the
	 * supplied {@link Visit} instance.
	 */
	private MapSqlParameterSource createVisitParameterSource(Visit visit) {
		return new MapSqlParameterSource()
			.addValue("id", visit.getId())
			.addValue("visit_date", visit.getDate())
			.addValue("description", visit.getDescription())
			.addValue("pet_id", visit.getPet().getId());
	}



	@Override
	public List<Visit> findByPetId(Integer petId) {
		final List<Visit> visits = this.jdbcTemplate.query(
				"SELECT id, visit_date, description FROM visits WHERE pet_id=?",
				new ParameterizedRowMapper<Visit>() {
					public Visit mapRow(ResultSet rs, int row) throws SQLException {
						Visit visit = new Visit();
						visit.setId(rs.getInt("id"));
						visit.setDate(rs.getTimestamp("visit_date"));
						visit.setDescription(rs.getString("description"));
						return visit;
					}
				},
				petId);
		return visits;
	}
	
	 

	/**
	 * {@link ParameterizedRowMapper} implementation mapping data from a
	 * {@link ResultSet} to the corresponding properties of the {@link JdbcPet} class.
	 */
	private class JdbcPetRowMapper implements ParameterizedRowMapper<JdbcPet> {

		public JdbcPet mapRow(ResultSet rs, int rownum) throws SQLException {
			JdbcPet pet = new JdbcPet();
			pet.setId(rs.getInt("id"));
			pet.setName(rs.getString("name"));
			pet.setBirthDate(rs.getDate("birth_date"));
			pet.setTypeId(rs.getInt("type_id"));
			pet.setOwnerId(rs.getInt("owner_id"));
			return pet;
		}
	}

}
