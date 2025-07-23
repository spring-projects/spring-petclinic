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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import jakarta.annotation.PostConstruct;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

/**
 * Configuration for logging in the application. Supports different logging formats based
 * on active profiles.
 */
@Configuration
public class LoggingConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(LoggingConfiguration.class);

	/**
	 * Default logging configuration using standard format.
	 */
	@Configuration
	@Profile("!json-logging & !file-logging")
	public static class StandardLoggingConfiguration {

		@PostConstruct
		public void init() {
			logger.info("Using standard logging format");
		}

	}

	/**
	 * JSON logging configuration activated with the 'json-logging' profile.
	 */
	@Configuration
	@Profile("json-logging")
	public static class JsonLoggingConfiguration {

		@PostConstruct
		public void init() {
			try {
				LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
				JoranConfigurator configurator = new JoranConfigurator();
				configurator.setContext(context);
				context.reset();
				configurator.doConfigure(getClass().getClassLoader().getResourceAsStream("logback-json.xml"));
				logger.info("Configured JSON logging format");
			}
			catch (JoranException e) {
				logger.error("Failed to configure JSON logging", e);
			}
		}

	}

	/**
	 * File-based logging configuration activated with the 'file-logging' profile. Useful
	 * for container environments where logs should be persisted to a volume.
	 */
	@Configuration
	@Profile("file-logging")
	public static class FileLoggingConfiguration {

		@PostConstruct
		public void init() {
			try {
				LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
				JoranConfigurator configurator = new JoranConfigurator();
				configurator.setContext(context);
				context.reset();
				configurator.doConfigure(getClass().getClassLoader().getResourceAsStream("logback-file.xml"));
				logger.info("Configured file-based logging");

				// Log the file location for user reference
				logger.info("Log files will be written to /logs/petclinic.log");
				logger.info("For container environments, ensure this directory is mounted as a volume");
			}
			catch (JoranException e) {
				logger.error("Failed to configure file-based logging", e);
			}
		}

	}

}
