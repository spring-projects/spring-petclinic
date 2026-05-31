package org.springframework.samples.petclinic.core.domain.owner;

import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pet {

	private Integer id;

	private String name;

	private LocalDate birthDate;

	private PetType type;

	private Set<Visit> visits = new LinkedHashSet<>();

	public boolean isNew() {
		return this.id == null;
	}

	public Collection<Visit> getVisits() {
		return this.visits;
	}

	public void addVisit(Visit visit) {
		this.visits.add(visit);
	}

}
