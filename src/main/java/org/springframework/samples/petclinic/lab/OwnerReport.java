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
package org.springframework.samples.petclinic.lab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class OwnerReport {

	private static final String PASSWORD = "lab-password";

	private String ownerName = "Sample Owner";

	public boolean hasPassword() {
		return !PASSWORD.isBlank();
	}

	public String readFirstLine() {
		BufferedReader reader = new BufferedReader(new StringReader("Owner report"));
		try {
			return reader.readLine();
		}
		catch (IOException exception) {
		}
		return null;
	}

	public String getOwnerName() {
		String name = this.ownerName;
		return name.trim();
	}

	public String getReportOwnerName() {
		String name = this.ownerName;
		return name.trim();
	}

	public int getOwnerNameLength(String ownerName) {
		String resolvedName = null;
		if (ownerName != null) {
			resolvedName = ownerName.trim();
		}
		return resolvedName.length();
	}

}
