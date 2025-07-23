/*
 * Copyright 2012-2025 the original author or authors.
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Simple test to verify that Spring test context works in native image. This test is
 * specifically designed to check if the WebAppConfiguration class and other Spring test
 * context classes are properly included in the native image.
 */
@SpringBootTest
class NativeImageTestVerification {

	private static final Logger logger = LoggerFactory.getLogger(NativeImageTestVerification.class);

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	void contextLoads() {
		logger.info("Testing Spring context in native image");
		assertThat(applicationContext).isNotNull();

		// Log some information about the test context to verify it's working
		logger.info("Application name: {}", applicationContext.getApplicationName());
		logger.info("Bean definition count: {}", applicationContext.getBeanDefinitionCount());

		// Verify that we can access some beans from the context
		assertThat(applicationContext.getBeanDefinitionCount()).isGreaterThan(0);
		assertThat(applicationContext.getEnvironment()).isNotNull();
	}

}
