package org.springframework.samples.petclinic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

@Configuration
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			// Security headers
			.headers(h -> h
				.contentSecurityPolicy(csp -> csp
					.policyDirectives("default-src 'self'; img-src 'self' data:; style-src 'self' 'unsafe-inline'"))
				.frameOptions(f -> f.sameOrigin())
				.referrerPolicy(r -> r.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.SAME_ORIGIN)))
			// Everything is public in this app
			.authorizeHttpRequests(
					a -> a.requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/actuator/**")
						.permitAll()
						.anyRequest()
						.permitAll())
			// Kill default auth mechanisms to avoid 401 challenges
			.httpBasic(h -> h.disable())
			.formLogin(f -> f.disable())
			// Keep CSRF defaults (safe for GETs)
			.csrf(Customizer.withDefaults());

		return http.build();
	}

}
