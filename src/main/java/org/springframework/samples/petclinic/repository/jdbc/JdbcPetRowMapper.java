package org.springframework.samples.petclinic.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.joda.time.DateTime;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

/**
 * {@link ParameterizedRowMapper} implementation mapping data from a
 * {@link ResultSet} to the corresponding properties of the {@link JdbcPet} class.
 */
class JdbcPetRowMapper implements ParameterizedRowMapper<JdbcPet> {

	public JdbcPet mapRow(ResultSet rs, int rownum) throws SQLException {
		JdbcPet pet = new JdbcPet();
		pet.setId(rs.getInt("id"));
		pet.setName(rs.getString("name"));
		Date birthDate = rs.getDate("birth_date");
		pet.setBirthDate(new DateTime(birthDate));
		pet.setTypeId(rs.getInt("type_id"));
		pet.setOwnerId(rs.getInt("owner_id"));
		return pet;
	}
}
