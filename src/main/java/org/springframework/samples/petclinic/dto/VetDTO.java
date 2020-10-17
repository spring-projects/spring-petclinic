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
package org.springframework.samples.petclinic.dto;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.samples.petclinic.model.Specialty;

import javax.xml.bind.annotation.XmlElement;
import java.util.*;

/**
 * Simple Data Transfert Object representing a vet.
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
public class VetDTO extends PersonDTO {

	private Set<Specialty> specialties;

	protected Set<Specialty> getSpecialtiesInternal() {
		if (this.specialties == null) {
			this.specialties = new HashSet<>();
		}
		return this.specialties;
	}

	protected void setSpecialtiesInternal(Set<Specialty> specialties) {
		this.specialties = specialties;
	}

	@XmlElement
	public List<Specialty> getSpecialties() {
		List<Specialty> sortedSpecs = new ArrayList<>(getSpecialtiesInternal());
		PropertyComparator.sort(sortedSpecs, new MutableSortDefinition("name", true, true));
		return Collections.unmodifiableList(sortedSpecs);
	}

	public int getNrOfSpecialties() {
		return getSpecialtiesInternal().size();
	}

	public void addSpecialty(Specialty specialty) {
		getSpecialtiesInternal().add(specialty);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof VetDTO))
			return false;
		if (!super.equals(o))
			return false;

		VetDTO vetDTO = (VetDTO) o;

		return getSpecialties().equals(vetDTO.getSpecialties());
	}

}
