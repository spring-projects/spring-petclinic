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
 * Thrown when an entity is not found, or does not belong to the parent it was requested
 * under (for example a pet that belongs to a different owner).
 */
public class ResourceNotFoundException extends RuntimeException {

	private final String entity;

	private final int id;

	public ResourceNotFoundException(String entity, int id) {
		super(entity + " not found with id: " + id);
		this.entity = entity;
		this.id = id;
	}

	public String getEntity() {
		return this.entity;
	}

	public int getId() {
		return this.id;
	}

}
