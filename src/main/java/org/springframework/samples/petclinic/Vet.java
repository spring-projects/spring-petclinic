package org.springframework.samples.petclinic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

/**
 * Simple JavaBean domain object representing a veterinarian.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Arjen Poutsma
 */
@Entity @Table(name="vets")
public class Vet extends Person {

	@ManyToMany @JoinTable (name="vet_specialties",joinColumns = @JoinColumn(name = "vet_id"), 
										inverseJoinColumns= @JoinColumn(name = "specialty_id"))
	private Set<Specialty> specialties;


	protected void setSpecialtiesInternal(Set<Specialty> specialties) {
		this.specialties = specialties;
	}

	protected Set<Specialty> getSpecialtiesInternal() {
		if (this.specialties == null) {
			this.specialties = new HashSet<Specialty>();
		}
		return this.specialties;
	}

	@XmlElement
	public List<Specialty> getSpecialties() {
		List<Specialty> sortedSpecs = new ArrayList<Specialty>(getSpecialtiesInternal());
		PropertyComparator.sort(sortedSpecs, new MutableSortDefinition("name", true, true));
		return Collections.unmodifiableList(sortedSpecs);
	}

	public int getNrOfSpecialties() {
		return getSpecialtiesInternal().size();
	}

	public void addSpecialty(Specialty specialty) {
		getSpecialtiesInternal().add(specialty);
	}

}
