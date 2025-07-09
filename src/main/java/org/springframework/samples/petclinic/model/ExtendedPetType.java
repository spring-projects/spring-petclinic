package org.springframework.samples.petclinic.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pet_type")
public class ExtendedPetType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;

	private String temperament;

	private Double length;

	private Double weight;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
