package org.springframework.samples.petclinic.owner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "pet_attributes")
public class PetAttributes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pet_id", nullable = false)
	@JsonIgnore
	private Pet pet;

	private String temperament;

	@Column(name = "length_cm", precision = 5, scale = 2)
	private BigDecimal lengthCm;

	@Column(name = "weight_kg", precision = 5, scale = 2)
	private BigDecimal weightKg;

	@Column(name = "additional_attributes", columnDefinition = "json")
	private String additionalAttributes;

	// --- Getters and Setters ---

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public BigDecimal getLengthCm() {
		return lengthCm;
	}

	public void setLengthCm(BigDecimal lengthCm) {
		this.lengthCm = lengthCm;
	}

	public BigDecimal getWeightKg() {
		return weightKg;
	}

	public void setWeightKg(BigDecimal weightKg) {
		this.weightKg = weightKg;
	}

	public String getAdditionalAttributes() {
		return additionalAttributes;
	}

	public void setAdditionalAttributes(String additionalAttributes) {
		this.additionalAttributes = additionalAttributes;
	}

}
