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
package org.springframework.samples.petclinic.owner;

/**
 * DTO for owner search results that efficiently includes aggregated pet names without
 * triggering N+1 queries.
 */
public class OwnerSearchResult {

	private final Integer id;

	private final String firstName;

	private final String lastName;

	private final String address;

	private final String city;

	private final String telephone;

	private final String petNames; // Comma-separated pet names

	public OwnerSearchResult(Integer id, String firstName, String lastName, String address, String city,
			String telephone, String petNames) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.telephone = telephone;
		this.petNames = petNames;
	}

	public Integer getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getAddress() {
		return address;
	}

	public String getCity() {
		return city;
	}

	public String getTelephone() {
		return telephone;
	}

	public String getPetNames() {
		return petNames != null ? petNames : "";
	}

	public boolean hasPets() {
		return petNames != null && !petNames.trim().isEmpty();
	}

}
