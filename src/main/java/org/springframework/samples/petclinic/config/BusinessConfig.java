package org.springframework.samples.petclinic.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan("org.springframework.samples.petclinic.service")
// Configurer that replaces ${...} placeholders with values from a properties file
// (in this case, JDBC-related settings for the JPA EntityManager definition below)
@PropertySource("classpath:spring/data-access.properties")
@EnableTransactionManagement
@Import({DataSourceConfig.class, InitDataSourceConfig.class, JdbcConfig.class, SharedJpaConfig.class, JpaConfig.class, SpringDataJpaConfig.class})
public class BusinessConfig {
		

}
