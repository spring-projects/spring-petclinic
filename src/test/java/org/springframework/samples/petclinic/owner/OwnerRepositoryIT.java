package org.springframework.samples.petclinic.owner;

import java.util.List;

import org.assertj.core.api.Assertions;
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

	@Container
	static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
		.withDatabaseName("petclinic")
		.withUsername("pet")
		.withPassword("pet");

	@DynamicPropertySource
	static void dbProps(DynamicPropertyRegistry r) {
		r.add("spring.datasource.url", postgres::getJdbcUrl);
		r.add("spring.datasource.username", postgres::getUsername);
		r.add("spring.datasource.password", postgres::getPassword);
		r.add("spring.jpa.hibernate.ddl-auto", () -> "update");
		r.add("spring.sql.init.mode", () -> "never");
	}

	@Autowired
	OwnerRepository owners;

	@BeforeEach
	void seed() {
		Owner o = new Owner();
		o.setFirstName("Grace");
		o.setLastName("Hopper");
		o.setAddress("123 Main St");
		o.setCity("NYC");
		o.setTelephone("1234567890");
		owners.save(o);
	}

	@Test
	void findsOwnerByLastName() {
		List<Owner> result = owners.findByLastName("Hopper");
		Assertions.assertThat(result).hasSize(1);
		Assertions.assertThat(result.get(0).getFirstName()).isEqualTo("Grace");
	}

}
