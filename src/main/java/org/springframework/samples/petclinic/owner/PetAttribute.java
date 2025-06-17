package org.springframework.samples.petclinic.owner;

import org.springframework.samples.petclinic.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table (name="pet_attributes")
public class PetAttribute extends BaseEntity{


	@OneToOne
	@JoinColumn(name="pet_id")
	private Pet pet;
	
	//@NotBlank(message="Temperament is required")
	private String temperament;

	private Double length;

	private Double weight;

	public Pet getPet() {
		return pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
	}

	public String getTemperament() {
		return temperament;
	}

	public void setTemperament(String temperament) {
		this.temperament = temperament;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}
	
	

}
