package org.springframework.samples.petclinic.pet.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import org.springframework.samples.petclinic.owner.PetType;

import java.io.Serializable;

/**
 * @author Rohit Lalwani
 */
@Entity
public class PetAttribute implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull
	private String temperament;

	@NotNull
	private Double weight;

	@NotNull
	private Double length;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_id")
	private PetType petType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTemperament() {
		return temperament;
	}

	public void setTemperament(String temperament) {
		this.temperament = temperament;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public PetType getPetType() {
		return petType;
	}

	public void setPetType(PetType petType) {
		this.petType = petType;
	}

}
