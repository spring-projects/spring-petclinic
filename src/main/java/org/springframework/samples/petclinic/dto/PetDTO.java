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
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.*;

/**
 * Simple Data Transfert Object representing a pet.
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
public class PetDTO extends NamedDTO {

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthDate;

	private PetTypeDTO type;

	private OwnerDTO owner;

	private Set<VisitDTO> visits = new LinkedHashSet<>();

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public LocalDate getBirthDate() {
		return this.birthDate;
	}

	public PetTypeDTO getType() {
		return this.type;
	}

	public void setType(PetTypeDTO type) {
		this.type = type;
	}

	public OwnerDTO getOwner() {
		return owner;
	}

	public void setOwner(OwnerDTO owner) {
		this.owner = owner;
	}

	protected Set<VisitDTO> getVisitsInternal() {
		if (this.visits == null) {
			this.visits = new HashSet<>();
		}
		return this.visits;
	}

	public void setVisitsInternal(Collection<VisitDTO> visits) {
		this.visits = new LinkedHashSet<>(visits);
	}

	public List<VisitDTO> getVisits() {
		List<VisitDTO> sortedVisits = new ArrayList<>(getVisitsInternal());
		PropertyComparator.sort(sortedVisits, new MutableSortDefinition("date", false, false));
		return Collections.unmodifiableList(sortedVisits);
	}

	public void addVisit(VisitDTO visit) {
		getVisitsInternal().add(visit);
		visit.setPetId(this.getId());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof PetDTO))
			return false;

		PetDTO petDTO = (PetDTO) o;

		if (!getBirthDate().equals(petDTO.getBirthDate()))
			return false;
		if (!getType().equals(petDTO.getType()))
			return false;
		if (!getOwner().equals(petDTO.getOwner()))
			return false;
		return getVisits() != null ? getVisits().equals(petDTO.getVisits()) : petDTO.getVisits() == null;
	}

}
