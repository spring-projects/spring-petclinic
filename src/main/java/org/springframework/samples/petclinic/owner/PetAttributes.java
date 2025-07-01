package org.springframework.samples.petclinic.owner;


import org.springframework.samples.petclinic.model.BaseEntity;

import jakarta.persistence.*;

@Entity
@Table(name = "pet_attributes")
public class PetAttributes extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "petId", nullable = false, unique = true)
	private Long petId;

	@Column(name = "temperament")
	private String temperament;

	@Column(name = "length")
	private Double length;

	@Column(name = "weight")
	private Double weight;

	@Column(name = "energy_level")
	private Integer energyLevel;

	// Getters and Setters

	public Long getPetId() {
		return petId;
	}

	public void setPetId(Long petId) {
		this.petId = petId;
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

	public Integer getEnergyLevel() {
		return energyLevel;
	}

	public void setEnergyLevel(Integer energyLevel) {
		this.energyLevel = energyLevel;
	}

	@Override
	public String toString() {
		return "PetAttributes [petId=" + petId + ", temperament=" + temperament + ", length=" + length + ", weight="
				+ weight + ", energyLevel=" + energyLevel + "]";
	}
	

}
