package org.springframework.samples.petclinic.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:application.properties")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String CLIENT_PROPERTY_KEY = "spring.security.oauth2.client.registration.";

	@Autowired
	private UserDetailsService userDetailsService;

	@Resource
	private Environment env;

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager customAuthenticationManager() throws Exception {
		return authenticationManager();
	}

	@Bean
	public OAuth2AuthorizedClientService authorizedClientService() {

		return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off

		http.authorizeRequests()
			.antMatchers("/").permitAll()
			.antMatchers("/login", "/logout", "/register","/confirm-account").permitAll()
			.antMatchers("/websocket/**", "/topic/**", "/app/**").permitAll()
			.antMatchers("/resources/**").permitAll()
			.antMatchers("/h2-console/**").permitAll()
			.antMatchers("/**").authenticated()
			.antMatchers("/edit/**").authenticated()
			.anyRequest().authenticated()
			.and()
				.formLogin()
				.loginPage("/login")
				.loginProcessingUrl("/login")
				.defaultSuccessUrl("/login/success", true)
				.usernameParameter("email")
				.passwordParameter("password")
				.failureUrl("/login")
				.permitAll()
			.and()
				.logout()
				.logoutUrl("/logout")
				.logoutSuccessUrl("/logout/success")
				.invalidateHttpSession(true)
				.permitAll()
			.and()
				.oauth2Login()
				.loginPage("/login")
				.defaultSuccessUrl("/oauth2/success", true)
				.failureUrl("/login")
				.clientRegistrationRepository(clientRegistrationRepository())
				.authorizedClientService(authorizedClientService())
			.and()
				.csrf().disable();

		// @formatter:on
	}

	@Bean
	public ClientRegistrationRepository clientRegistrationRepository() {
		List<String> clients = Arrays.asList("google", "facebook", "github");

		List<ClientRegistration> registrations = clients.stream().map(c -> getRegistration(c))
				.filter(registration -> registration != null).collect(Collectors.toList());

		return new InMemoryClientRegistrationRepository(registrations);
	}

	private ClientRegistration getRegistration(String client) {
		String clientId = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-id");

		if (clientId == null) {
			return null;
		}

		String clientSecret = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-secret");

		if (client.equals("google")) {
			return CommonOAuth2Provider.GOOGLE.getBuilder(client).clientId(clientId).clientSecret(clientSecret).build();
		}
		if (client.equals("facebook")) {
			return CommonOAuth2Provider.FACEBOOK.getBuilder(client).clientId(clientId).clientSecret(clientSecret)
					.build();
		}
		if (client.equals("github")) {
			return CommonOAuth2Provider.GITHUB.getBuilder(client).clientId(clientId).clientSecret(clientSecret).build();
		}

		return null;
	}

}
