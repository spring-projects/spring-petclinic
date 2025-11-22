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
package org.springframework.samples.petclinic.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import org.jspecify.annotations.Nullable;

/**
 * Simple JavaBean domain object representing an animal.
 */
@MappedSuperclass
public class Animal extends BaseEntity {

	@Column(name = "name")
	@NotBlank
	private @Nullable String name;

	@Column(name = "species")
	@NotBlank
	private @Nullable String species;

	@Column(name = "breed")
	@NotBlank
	private @Nullable String breed;

	@Column(name = "age")
	@PositiveOrZero
	private @Nullable Integer age;

	@Column(name = "color")
	@NotBlank
	private @Nullable String color;

	@Column(name = "description")
	private @Nullable String description;

	public @Nullable String getName() {
		return this.name;
	}

	public void setName(@Nullable String name) {
		this.name = name;
	}

	public @Nullable String getSpecies() {
		return this.species;
	}

	public void setSpecies(@Nullable String species) {
		this.species = species;
	}

	public @Nullable String getBreed() {
		return this.breed;
	}

	public void setBreed(@Nullable String breed) {
		this.breed = breed;
	}

	public @Nullable Integer getAge() {
		return this.age;
	}

	public void setAge(@Nullable Integer age) {
		this.age = age;
	}

	public @Nullable String getColor() {
		return this.color;
	}

	public void setColor(@Nullable String color) {
		this.color = color;
	}

	public @Nullable String getDescription() {
		return this.description;
	}

	public void setDescription(@Nullable String description) {
		this.description = description;
	}
}
