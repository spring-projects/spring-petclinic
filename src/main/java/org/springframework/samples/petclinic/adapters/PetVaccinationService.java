package org.springframework.samples.petclinic.adapters;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import org.json.JSONException;

import java.io.IOException;

public interface PetVaccinationService {
	@WithSpan
	VaccinnationRecord[] AllVaccines() throws JSONException, IOException;

	@WithSpan
	VaccinnationRecord VaccineRecord(int vaccinationRecordId) throws JSONException, IOException;
}
