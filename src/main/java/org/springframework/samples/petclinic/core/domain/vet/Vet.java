package org.springframework.samples.petclinic.core.domain.vet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Vet {

	private Integer id;

	private String firstName;

	private String lastName;

	private Set<Specialty> specialties;

	protected Set<Specialty> getSpecialtiesInternal() {
		if (this.specialties == null) {
			this.specialties = new HashSet<>();
		}
		return this.specialties;
	}

	public List<Specialty> getSpecialties() {
		List<Specialty> sorted = new ArrayList<>(getSpecialtiesInternal());
		sorted.sort(Comparator.comparing(Specialty::getName));
		return Collections.unmodifiableList(sorted);
	}

	public int getNrOfSpecialties() {
		return getSpecialtiesInternal().size();
	}

	public void addSpecialty(Specialty specialty) {
		getSpecialtiesInternal().add(specialty);
	}

	public boolean isNew() {
		return this.id == null;
	}

}
