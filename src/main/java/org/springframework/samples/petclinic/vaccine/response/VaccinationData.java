package org.springframework.samples.petclinic.vaccine.response;

import java.time.LocalDate;

public interface VaccinationData {

	String getOwnerName();

	String getPetName();

	String getVaccineName();

	LocalDate getVaccinationDate();

	Boolean getInjected();

}
