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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystems;

/**
 * PetClinic Spring Boot Application.
 *
 * @author Dave Syer
 */
@SpringBootApplication(proxyBeanMethods = false)
public class PetClinicApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetClinicApplication.class, args);
	}

	public static String takeAppMapSnapshot() {
		return takeAppMapSnapshot(FileSystems.getDefault().getPath(System.getProperty("user.dir")).resolve("checkpoint.appmap.json").toString());
	}

	public static String takeAppMapSnapshot(String filePath) {
		try {
			Object recorder = Class.forName("com.appland.appmap.record.Recorder").getMethod("getInstance").invoke(null);
			Boolean hasActiveSession = (Boolean) recorder.getClass().getMethod("hasActiveSession").invoke(recorder);
			if ( !hasActiveSession.booleanValue() ) {
				return "There's no AppMap recording in progress. Start an AppMap recording in order to take a snapshot.";
			}

			Object recording = recorder.getClass().getMethod("checkpoint").invoke(recorder);
			recording.getClass().getMethod("moveTo", String.class).invoke(recording, filePath);
			return filePath;
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
