package org.springframework.samples.petclinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.samples.petclinic.service.common.UserDetailsServiceImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
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

	private final static String USER_EMAIL = "Sam.Schultz@petclinic.com";

	private final static String USER_PASSWORD = "PASSWORD_TEST9879879$^m$*ùm*^$*ù";

	private final static String ROLE_NAME = "ROLE_TEST";

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		auth.inMemoryAuthentication().passwordEncoder(encoder).withUser(USER_EMAIL).password(USER_PASSWORD)
				.roles(ROLE_NAME);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/owners/**", "/pets/**", "/users/**", "/visits/**").authenticated()
				.antMatchers("/**").permitAll().and().httpBasic();
	}

}
