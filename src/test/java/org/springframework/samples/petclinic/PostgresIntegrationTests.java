/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import ch.qos.logback.core.net.HardenedObjectInputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.DockerClientFactory;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = { "spring.docker.compose.skip.in-tests=false", //
		"spring.docker.compose.profiles.active=postgres" })
@ActiveProfiles("postgres")
@DisabledInNativeImage
public class PostgresIntegrationTests {

	@LocalServerPort
	int port;

	@Autowired
	private VetRepository vets;

	@Autowired
	private RestTemplateBuilder builder;

	@BeforeAll
	static void available() {
		assumeTrue(DockerClientFactory.instance().isDockerAvailable(), "Docker not available");
	}

	public static void main(String[] args) {
		new SpringApplicationBuilder(PetClinicApplication.class) //
			.profiles("postgres") //
			.properties( //
					"spring.docker.compose.profiles.active=postgres" //
			) //
			.listeners(new PropertiesLogger()) //
			.run(args);
	}

	void simulateVulnerableClassWithMethodLoaded() {
		/*
		 * Sonatype Runtime Agent - [TIME]: *** Vulnerable CLASS LOADED
		 * [className=ch/qos/logback/core/net/HardenedObjectInputStream] by the JVM
		 * Sonatype Runtime Agent - [TIME]: Assigning label 'Runtime-Class-Loaded' to
		 * component 2f9f280219a9922a7420 in application: a50576c3cd894d20b24dc0d98eea084b
		 * Sonatype Runtime Agent - [TIME]: Component evaluation for
		 * [ComponentEvaluation{hash='2f9f280219a9922a7420'}] in application
		 * a50576c3cd894d20b24dc0d98eea084b successful. Result
		 * URL=api/v2/evaluation/applications/a50576c3cd894d20b24dc0d98eea084b/results/
		 * 74387681c75446a5924812d032c77cad Sonatype Runtime Agent - [TIME]: *** Class
		 * with vulnerable METHOD LOADED
		 * [className=ch/qos/logback/core/net/HardenedObjectInputStream,
		 * methodName=<init>,
		 * methodDescriptor=(Ljava/io/InputStream;[Ljava/lang/String;)V] by the JVM
		 * Sonatype Runtime Agent - [TIME]: Assigning label 'Runtime-Method-Loaded' to
		 * component 2f9f280219a9922a7420 in application: a50576c3cd894d20b24dc0d98eea084b
		 * Sonatype Runtime Agent - [TIME]: Component evaluation for
		 * [ComponentEvaluation{hash='2f9f280219a9922a7420'}] in application
		 * a50576c3cd894d20b24dc0d98eea084b successful. Result
		 * URL=api/v2/evaluation/applications/a50576c3cd894d20b24dc0d98eea084b/results/
		 * 488f999c6730499a8cd454b37d3201b2 >>
		 * org.springframework.samples.petclinic.PostgresIntegrationTests loaded
		 * ch.qos.logback.core.net.HardenedObjectInputStream
		 */
		System.out.println(">> " + getClass().getName() + " loaded " + HardenedObjectInputStream.class.getName());
	}

	@Test
	void testFindAll() throws Exception {
		// Demo: Simulate Runtime-Class-Loaded and Runtime-Method-Loaded
		simulateVulnerableClassWithMethodLoaded();

		vets.findAll();
		vets.findAll(); // served from cache
	}

	@Test
	void testOwnerDetails() {
		RestTemplate template = builder.rootUri("http://localhost:" + port).build();
		ResponseEntity<String> result = template.exchange(RequestEntity.get("/owners/1").build(), String.class);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	static class PropertiesLogger implements ApplicationListener<ApplicationPreparedEvent> {

		private static final Log log = LogFactory.getLog(PropertiesLogger.class);

		private ConfigurableEnvironment environment;

		private boolean isFirstRun = true;

		@Override
		public void onApplicationEvent(ApplicationPreparedEvent event) {
			if (isFirstRun) {
				environment = event.getApplicationContext().getEnvironment();
				printProperties();
			}
			isFirstRun = false;
		}

		public void printProperties() {
			for (EnumerablePropertySource<?> source : findPropertiesPropertySources()) {
				log.info("PropertySource: " + source.getName());
				String[] names = source.getPropertyNames();
				Arrays.sort(names);
				for (String name : names) {
					String resolved = environment.getProperty(name);
					String value = source.getProperty(name).toString();
					if (resolved.equals(value)) {
						log.info(name + "=" + resolved);
					}
					else {
						log.info(name + "=" + value + " OVERRIDDEN to " + resolved);
					}
				}
			}
		}

		private List<EnumerablePropertySource<?>> findPropertiesPropertySources() {
			List<EnumerablePropertySource<?>> sources = new LinkedList<>();
			for (PropertySource<?> source : environment.getPropertySources()) {
				if (source instanceof EnumerablePropertySource enumerable) {
					sources.add(enumerable);
				}
			}
			return sources;
		}

	}

}
