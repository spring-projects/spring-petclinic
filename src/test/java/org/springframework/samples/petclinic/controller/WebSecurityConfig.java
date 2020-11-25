package org.springframework.samples.petclinic.controller;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Web securuty configuration for controllers tests
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@TestConfiguration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	public static final String TEST_USER = "petclinicuser";

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		auth.inMemoryAuthentication().passwordEncoder(encoder).withUser(TEST_USER).password(encoder.encode("secret"))
				.roles("ROLE_USER");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/owners/**", "/pets/**", "/users/**", "/visits/**").authenticated()
				.antMatchers("/**").permitAll().and().httpBasic();
	}

}
