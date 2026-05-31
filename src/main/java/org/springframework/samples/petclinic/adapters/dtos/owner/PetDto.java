package org.springframework.samples.petclinic.adapters.dtos.owner;

import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetDto {

	private Integer id;

	private String name;

	private LocalDate birthDate;

	private PetTypeDto type;

	private Set<VisitDto> visits = new LinkedHashSet<>();

	public boolean isNew() {
		return this.id == null;
	}

	public Collection<VisitDto> getVisits() {
		return this.visits;
	}

	public void addVisit(VisitDto visit) {
		this.visits.add(visit);
	}

}
