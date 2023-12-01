package org.springframework.samples.petclinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.oracle.OracleContainer;

/**
 * PetClinic Spring Boot Application
 *
 * @author Radu Ghiorma
 *
 */
@Configuration
public class OracleTestApplication {

	@ServiceConnection
	@Profile("oracle")
	@Bean
	static OracleContainer container() {
		return new OracleContainer("gvenzl/oracle-free:slim-faststart");
	}

	public static void main(String[] args) {
		SpringApplication.run(PetClinicApplication.class, "--spring.profiles.active=oracle");
	}

}
