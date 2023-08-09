package org.springframework.samples.petclinic.adapters;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;

@Component
public class PetVaccinationServiceFacade implements PetVaccinationService {

	public static final String VACCINES_RECORDS_URL = "https://647f4bb4c246f166da9084c7.mockapi.io/api/vetcheck/vaccines";

	private String MakeHttpCall(String url) throws IOException {

		Request getAllVaccinesRequest = new Request.Builder().url(url).build();
		OkHttpClient client = new OkHttpClient();
		Response getAllVaccinesResult = client.newCall(getAllVaccinesRequest).execute();
		return getAllVaccinesResult.body().string();
	}

	@Override
	@WithSpan
	public VaccinnationRecord[] AllVaccines() throws JSONException, IOException {

		var vaccineListString = MakeHttpCall(VACCINES_RECORDS_URL);
		JSONArray jArr = new JSONArray(vaccineListString);
		var vaccinnationRecords = new ArrayList<VaccinnationRecord>();

		for (int i = 0; i < jArr.length(); i++) {

			VaccinnationRecord record = parseVaccinationRecord(jArr.getJSONObject(i));
			vaccinnationRecords.add(record);

		}
		return vaccinnationRecords.toArray(VaccinnationRecord[]::new);

	}

	@Override
	@WithSpan
	public VaccinnationRecord VaccineRecord(int vaccinationRecordId) throws JSONException, IOException {

		var idUrl = VACCINES_RECORDS_URL + "/" + vaccinationRecordId;

		var vaccineListString = MakeHttpCall(idUrl);

		JSONObject vaccineJson = new JSONObject(vaccineListString);
		return parseVaccinationRecord(vaccineJson);

	}

	@NotNull
	private static VaccinnationRecord parseVaccinationRecord(JSONObject jsonObject) throws JSONException {
		Integer petId = jsonObject.getInt("pet_id");
		Integer id = jsonObject.getInt("id");
		String vaccineDateString = jsonObject.getString("vaccine_date");
		var vaccineDate = Instant.parse(vaccineDateString);
		return new VaccinnationRecord(id, petId, vaccineDate);
	}

}
