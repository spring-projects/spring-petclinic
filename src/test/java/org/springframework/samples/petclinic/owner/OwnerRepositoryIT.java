package org.springframework.samples.petclinic.owner;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Integration test that uses a real PostgreSQL instance via Testcontainers.
 */
@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class OwnerRepositoryIT {

	// PostgreSQL container used as the backing database for tests
	@Container
	static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
		.withDatabaseName("petclinic")
		.withUsername("pet")
		.withPassword("pet");

	// Configure Spring DataSource properties to point to the container
	@DynamicPropertySource
	static void dbProps(DynamicPropertyRegistry r) {
		r.add("spring.datasource.url", postgres::getJdbcUrl);
		r.add("spring.datasource.username", postgres::getUsername);
		r.add("spring.datasource.password", postgres::getPassword);
		r.add("spring.jpa.hibernate.ddl-auto", () -> "update"); // automatically create
																// schema for tests
		r.add("spring.sql.init.mode", () -> "never"); // skip default SQL initialization
	}

	@Autowired
	OwnerRepository owners;

	private Owner seeded;

	// Insert a sample Owner before each test
	@BeforeEach
	void seed() {
		Owner o = new Owner();
		o.setFirstName("Grace");
		o.setLastName("Hopper");
		o.setAddress("123 Main St");
		o.setCity("NYC");
		o.setTelephone("1234567890");
		seeded = owners.save(o);
	}

	// Remove all Owners after each test to ensure isolation
	@AfterEach
	void cleanup() {
		owners.deleteAll();
	}

	// Verify that searching by last name returns the expected Owner with all fields
	@Test
	void findsOwnerByLastName_andChecksAllFields() {
		List<Owner> result = owners.findByLastName("Hopper");

		Assertions.assertThat(result).hasSize(1);

		Owner found = result.get(0);
		Assertions.assertThat(found.getId()).isNotNull();
		Assertions.assertThat(found.getFirstName()).isEqualTo("Grace");
		Assertions.assertThat(found.getLastName()).isEqualTo("Hopper");
		Assertions.assertThat(found.getAddress()).isEqualTo("123 Main St");
		Assertions.assertThat(found.getCity()).isEqualTo("NYC");
		Assertions.assertThat(found.getTelephone()).isEqualTo("1234567890");
		Assertions.assertThat(found.getPets()).isEmpty();
	}

	// Verify that searching for an unknown last name returns no results
	@Test
	void returnsEmptyListForUnknownLastName() {
		List<Owner> result = owners.findByLastName("DoesNotExist");
		Assertions.assertThat(result).isEmpty();
	}

}
