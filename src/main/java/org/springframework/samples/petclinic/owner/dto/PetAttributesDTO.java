package org.springframework.samples.petclinic.owner.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class PetAttributesDTO {

	@NotNull(message = "Pet ID is required")
	private int petId;

	@NotBlank(message = "Temperament is required")
	@Size(max = 100, message = "Temperament must not exceed 100 characters")
	private String temperament;

	@NotNull
	@DecimalMin(value = "0.1", inclusive = true, message = "Length must be at least 0.1 cm")
	@DecimalMax(value = "500.0", inclusive = true, message = "Length must be less than or equal to 500 cm")
	private BigDecimal lengthCm;

	@NotNull
	@DecimalMin(value = "0.1", inclusive = true, message = "Weight must be at least 0.1 kg")
	@DecimalMax(value = "500.0", inclusive = true, message = "Weight must be less than or equal to 500 kg")
	private BigDecimal weightKg;

	// Getters and setters

	public int getPetId() {
		return petId;
	}

	public void setPetId(int petId) {
		this.petId = petId;
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

}
