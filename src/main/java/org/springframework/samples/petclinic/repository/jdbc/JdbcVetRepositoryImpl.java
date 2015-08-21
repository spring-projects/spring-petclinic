/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Repository;

/**
 * A simple JDBC-based implementation of the {@link VetRepository} interface.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @author Sam Brannen
 * @author Thomas Risberg
 * @author Mark Fisher
 * @author Michael Isvy
 */
@Repository
public class JdbcVetRepositoryImpl implements VetRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcVetRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Refresh the cache of Vets that the ClinicService is holding.
     */
    @Override
    public Collection<Vet> findAll() throws DataAccessException {
        List<Vet> vets = new ArrayList<>();
        // Retrieve the list of all vets.
        vets.addAll(this.jdbcTemplate.query(
                "SELECT id, first_name, last_name FROM vets ORDER BY last_name,first_name",
                BeanPropertyRowMapper.newInstance(Vet.class)));

        // Retrieve the list of all possible specialties.
        final List<Specialty> specialties = this.jdbcTemplate.query(
                "SELECT id, name FROM specialties",
                BeanPropertyRowMapper.newInstance(Specialty.class));

        // Build each vet's list of specialties.
        for (Vet vet : vets) {
            final List<Integer> vetSpecialtiesIds = this.jdbcTemplate.query(
                    "SELECT specialty_id FROM vet_specialties WHERE vet_id=?",
                    new BeanPropertyRowMapper<Integer>() {
                        @Override
                        public Integer mapRow(ResultSet rs, int row) throws SQLException {
                            return rs.getInt(1);
                        }
                    },
                    vet.getId());
            for (int specialtyId : vetSpecialtiesIds) {
                Specialty specialty = EntityUtils.getById(specialties, Specialty.class, specialtyId);
                vet.addSpecialty(specialty);
            }
        }
        return vets;
    }
}
