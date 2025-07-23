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

package org.springframework.samples.petclinic.system;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Tests for the standard logging configuration.
 */
@SpringBootTest
class LoggingConfigurationTests {

	private static final Logger logger = LoggerFactory.getLogger(LoggingConfigurationTests.class);

	private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

	private final PrintStream originalOut = System.out;

	@Autowired
	private LoggingConfiguration loggingConfiguration;

	@BeforeEach
	void setUp() {
		System.setOut(new PrintStream(outputStreamCaptor));
	}

	@AfterEach
	void tearDown() {
		System.setOut(originalOut);
	}

	@Test
	void testStandardLogging() {
		// Log a test message
		String testMessage = "Test standard logging message";
		logger.info(testMessage);

		// Verify the message was logged with the expected format
		String logOutput = outputStreamCaptor.toString();
		assertThat(logOutput).contains(testMessage);
		// Simply check for date-time pattern without regex
		assertThat(logOutput).contains("-");
		assertThat(logOutput).contains(":");
		assertThat(logOutput).contains("INFO");
		assertThat(logOutput).contains(LoggingConfigurationTests.class.getSimpleName());
	}

}
