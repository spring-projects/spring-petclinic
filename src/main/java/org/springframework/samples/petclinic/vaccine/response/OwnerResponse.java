package org.springframework.samples.petclinic.vaccine.response;

import lombok.Data;

import java.util.Set;

@Data
public class OwnerResponse {

	private String ownerName;

	private Set<VaccinationHistory> vaccinationHistories;

}
