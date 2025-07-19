package org.springframework.samples.petclinic.vaccine.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VaccinationHistory {

	private String petName;

	private String vaccineName;

	private LocalDate vaccinationDate;

	private Boolean injected;

}
