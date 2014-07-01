package org.springframework.samples.petclinic.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("jpa")
@ComponentScan("org.springframework.samples.petclinic.repository.jpa")
public class JpaConfig {

}
