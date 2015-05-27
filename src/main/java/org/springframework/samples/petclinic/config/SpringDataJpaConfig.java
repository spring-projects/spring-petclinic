package org.springframework.samples.petclinic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Profile("spring-data-jpa")
@EnableJpaRepositories("org.springframework.samples.petclinic.repository.springdatajpa")
public class SpringDataJpaConfig {

}
