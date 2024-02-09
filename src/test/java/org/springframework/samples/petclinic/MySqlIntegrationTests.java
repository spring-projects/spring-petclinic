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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("mysql")
@Testcontainers(disabledWithoutDocker = true)
@DisabledInNativeImage
@DisabledInAotMode
class MySqlIntegrationTests {

	@ServiceConnection
	@Container
	static MySQLContainer<?> container = new MySQLContainer<>("mysql:8.2");

	@LocalServerPort
	int port;

	@Autowired
	private VetRepository vets;

	@Autowired
	private RestTemplateBuilder builder;

	void simulateVulnerableMethodCall() throws Exception {
		/*
		Sonatype Runtime Agent - [TIME]: *** Vulnerable CLASS LOADED [className=org/springframework/core/io/buffer/DefaultDataBuffer] by the JVM
		Sonatype Runtime Agent - [TIME]: Assigning label 'Runtime-Class-Loaded' to component 22d73bef97aff8a74a99 in application: a50576c3cd894d20b24dc0d98eea084b
		Sonatype Runtime Agent - [TIME]: Component evaluation for [ComponentEvaluation{hash='22d73bef97aff8a74a99'}] in application a50576c3cd894d20b24dc0d98eea084b successful. Result URL=api/v2/evaluation/applications/a50576c3cd894d20b24dc0d98eea084b/results/3d62858ec88e49e0afd552066cb160ad
		Sonatype Runtime Agent - [TIME]: *** Class with vulnerable METHOD LOADED [className=org/springframework/core/io/buffer/DefaultDataBuffer, methodName=split, methodDescriptor=(I)Lorg/springframework/core/io/buffer/DataBuffer;] by the JVM
		Sonatype Runtime Agent - [TIME]: Assigning label 'Runtime-Method-Loaded' to component 22d73bef97aff8a74a99 in application: a50576c3cd894d20b24dc0d98eea084b
		Sonatype Runtime Agent - [TIME]: Component evaluation for [ComponentEvaluation{hash='22d73bef97aff8a74a99'}] in application a50576c3cd894d20b24dc0d98eea084b successful. Result URL=api/v2/evaluation/applications/a50576c3cd894d20b24dc0d98eea084b/results/47fa37da85d8447f8c101d4db35ec797
		Sonatype Runtime Agent - [TIME]: *** Vulnerable METHOD CALLED [className=org/springframework/core/io/buffer/DefaultDataBuffer, methodName=split, methodDescriptor=(I)Lorg/springframework/core/io/buffer/DataBuffer;]
		Sonatype Runtime Agent - [TIME]: Assigning label 'Runtime-Method-Called' to component 22d73bef97aff8a74a99 in application: a50576c3cd894d20b24dc0d98eea084b
		Sonatype Runtime Agent - [TIME]: Component evaluation for [ComponentEvaluation{hash='22d73bef97aff8a74a99'}] in application a50576c3cd894d20b24dc0d98eea084b successful. Result URL=api/v2/evaluation/applications/a50576c3cd894d20b24dc0d98eea084b/results/6a969f11748f45abba95870fcd7747bb
		 */
		DefaultDataBufferFactory defaultDataBufferFactory = new DefaultDataBufferFactory();
		DefaultDataBuffer defaultDataBuffer = defaultDataBufferFactory.allocateBuffer(1024);
		defaultDataBuffer.split(0);
	}

	@Test
	void testFindAll() throws Exception {
		// Demo: Simulate Runtime-Method-Called
		simulateVulnerableMethodCall();

		vets.findAll();
		vets.findAll(); // served from cache
	}

	@Test
	void testOwnerDetails() {
		RestTemplate template = builder.rootUri("http://localhost:" + port).build();
		ResponseEntity<String> result = template.exchange(RequestEntity.get("/owners/1").build(), String.class);
		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

}
