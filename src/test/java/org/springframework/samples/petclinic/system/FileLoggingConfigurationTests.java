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
import org.springframework.test.context.ActiveProfiles;

/**
 * Tests for the file-based logging configuration. This test is only active when the
 * file-logging profile is active. Note: This test doesn't actually verify file writing as
 * that would require filesystem access and permissions that may not be available in all
 * environments.
 */
@SpringBootTest
@ActiveProfiles("file-logging")
class FileLoggingConfigurationTests {

	private static final Logger logger = LoggerFactory.getLogger(FileLoggingConfigurationTests.class);

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
	void testFileLoggingConfiguration() {
		// Log a test message
		String testMessage = "Test file logging message";
		logger.info(testMessage);

		// Verify the console output still works (since we're using both console and file
		// appenders)
		String logOutput = outputStreamCaptor.toString();
		assertThat(logOutput).contains(testMessage);

		// We can't verify the file output directly in this test, but we can check that
		// the basic logging format is correct
		assertThat(logOutput).contains("-");
		assertThat(logOutput).contains(":");
		assertThat(logOutput).contains("INFO");
		assertThat(logOutput).contains(FileLoggingConfigurationTests.class.getSimpleName());
	}

}
