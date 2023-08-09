package org.springframework.samples.petclinic.domain;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.adapters.PetVaccinationService;
import org.springframework.samples.petclinic.adapters.PetVaccinationServiceFacade;
import org.springframework.samples.petclinic.adapters.VaccinnationRecord;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetVaccine;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class PetVaccinationStatusService {

	@Autowired
	private PetVaccinationService adapter;

	@WithSpan
	public void UpdateVaccinationStatus(Pet[] pets) {

		for (Pet pet : pets) {
			try {
				var vaccinationRecords = this.adapter.AllVaccines();
				for (VaccinnationRecord record : vaccinationRecords) {

					var recordInfo = this.adapter.VaccineRecord(record.recordId());
					if (recordInfo.petId() == pet.getId()) {
						var date = LocalDateTime.ofInstant(recordInfo.vaccineDate(), ZoneId.systemDefault());
						PetVaccine petVaccine = new PetVaccine();
						petVaccine.setDate(date.toLocalDate());
						pet.addVaccine(petVaccine);
					}
				}

			}
			catch (JSONException | IOException e) {
				// Fail silently
				Span.current().recordException(e);
			}

		}

	}

}
