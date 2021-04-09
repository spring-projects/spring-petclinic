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
package org.springframework.samples.petclinic.owner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotEmpty;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.visit.Visit;

/**
 * Simple business object representing a pet.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 */
@Document
public class Pet {

	@Id
	@GeneratedValue(strategy = GenerationStrategy.UNIQUE)
	private String id;

	@NotEmpty
	private String name;

	private String birthDate;

	private String petType;

	@NotEmpty
	private String ownerId;

	private List<Visit> visits = new ArrayList<>();

	public Pet() {
	}

	public Pet(String id, @NotEmpty String name, String birthDate, String petType, @NotEmpty String ownerId,
			List<Visit> visits) {
		this.id = id;
		this.name = name;
		this.birthDate = birthDate;
		this.petType = petType;
		this.ownerId = ownerId;
		this.visits = visits;
	}

	public String getPetType() {
		return petType;
	}

	public void setPetType(String petType) {
		this.petType = petType;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getBirthDate() {
		return this.birthDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setVisits(List<Visit> visits) {
		this.visits = visits;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public List<Visit> getVisits() {
		List<Visit> sortedVisits = new ArrayList<>(visits);
		PropertyComparator.sort(sortedVisits, new MutableSortDefinition("visitDate", false, false));
		return Collections.unmodifiableList(sortedVisits);
	}

	public void addVisit(Visit visit) {
		visits.add(visit);
	}

	public boolean isNew() {
		return this.id == null;
	}

}
