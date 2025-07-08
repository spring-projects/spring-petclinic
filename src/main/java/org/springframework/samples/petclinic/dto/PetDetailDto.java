package org.springframework.samples.petclinic.dto;

public record PetDetailDto(
	String temperament,
	Double weight,
	Double length
) {
}
