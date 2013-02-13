package org.springframework.samples.petclinic.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
public class JdbcVetRepositoryImpl implements VetRepository {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private JdbcTemplate jdbcTemplate;

	private final List<Vet> vets = new ArrayList<Vet>();

	@Autowired
	public JdbcVetRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Refresh the cache of Vets that the ClinicService is holding.
	 * @see org.springframework.samples.petclinic.model.service.ClinicService#findVets()
	 */
	@ManagedOperation
	@Transactional(readOnly = true)
	public void refreshVetsCache() throws DataAccessException {
		synchronized (this.vets) {
			this.logger.info("Refreshing vets cache");

			// Retrieve the list of all vets.
			this.vets.clear();
			this.vets.addAll(this.jdbcTemplate.query(
					"SELECT id, first_name, last_name FROM vets ORDER BY last_name,first_name",
					ParameterizedBeanPropertyRowMapper.newInstance(Vet.class)));

			// Retrieve the list of all possible specialties.
			final List<Specialty> specialties = this.jdbcTemplate.query(
					"SELECT id, name FROM specialties",
					ParameterizedBeanPropertyRowMapper.newInstance(Specialty.class));

			// Build each vet's list of specialties.
			for (Vet vet : this.vets) {
				final List<Integer> vetSpecialtiesIds = this.jdbcTemplate.query(
						"SELECT specialty_id FROM vet_specialties WHERE vet_id=?",
						new ParameterizedRowMapper<Integer>() {
							public Integer mapRow(ResultSet rs, int row) throws SQLException {
								return Integer.valueOf(rs.getInt(1));
							}},
						vet.getId().intValue());
				for (int specialtyId : vetSpecialtiesIds) {
					Specialty specialty = EntityUtils.getById(specialties, Specialty.class, specialtyId);
					vet.addSpecialty(specialty);
				}
			}
		}
	}

	public Collection<Vet> findAll() throws DataAccessException {
		synchronized (this.vets) {
			if (this.vets.isEmpty()) {
				refreshVetsCache();
			}
			return this.vets;
		}
	}


}
