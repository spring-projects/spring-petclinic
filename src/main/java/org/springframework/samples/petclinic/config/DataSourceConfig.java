package org.springframework.samples.petclinic.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jndi.JndiObjectFactoryBean;

@Configuration
@PropertySource("classpath:spring/data-access.properties")
public class DataSourceConfig {

	@Autowired
	private Environment env;
	
	@Bean(name = "dataSource")
	@Description("DataSource configuration for the tomcat jdbc connection pool")
	@NotProfile("javaee")
	public DataSource dataSource() {
		// See here for more details on commons-dbcp versus tomcat-jdbc:
		// http://blog.ippon.fr/2013/03/13/improving-the-performance-of-the-spring-petclinic-sample-application-part-3-of-5/-->
		org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
		dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
		dataSource.setUrl(env.getProperty("jdbc.url"));
		dataSource.setUsername(env.getProperty("jdbc.username"));
		dataSource.setPassword(env.getProperty("jdbc.password"));
		return dataSource;
	}

	@Bean(name = "dataSource")
	@Description("JNDI DataSource for JEE environments")
	@Profile("javaee")
	public JndiObjectFactoryBean jndiDataSource()
			throws IllegalArgumentException {
		JndiObjectFactoryBean dataSource = new JndiObjectFactoryBean();
		dataSource.setExpectedType(DataSource.class);
		dataSource.setJndiName(env.getProperty("java:comp/env/jdbc/petclinic"));
		return dataSource;
	}
	
}
