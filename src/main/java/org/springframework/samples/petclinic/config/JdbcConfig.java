package org.springframework.samples.petclinic.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
@Profile("jdbc")
@ComponentScan("org.springframework.samples.petclinic.repository.jdbc")
public class JdbcConfig {
	
	@Autowired
	private DataSource dataSource;

	
    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager() {
    	return new DataSourceTransactionManager(dataSource);
    }
    
    @Bean(name="transactionManager")
    public JdbcTemplate jdbcTemplate() {
    	return new JdbcTemplate(dataSource);
    }
    
    @Bean

    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
    	return new NamedParameterJdbcTemplate(dataSource);
    }


}
