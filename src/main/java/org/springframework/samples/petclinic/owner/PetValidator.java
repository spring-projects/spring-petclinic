/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import okhttp3.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.io.IOException;

/**
 * <code>Validator</code> for <code>Pet</code> forms.
 * <p>
 * We're not using Bean Validation annotations here because it is easier to define such
 * validation rule in Java.
 * </p>
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 */
public class PetValidator implements Validator {

	private static final String REQUIRED = "required";

	@Override
	public void validate(Object obj, Errors errors) {
		Pet pet = (Pet) obj;
		String name = pet.getName();
		// name validation
		if (!StringUtils.hasLength(name)) {
			errors.rejectValue("name", REQUIRED, REQUIRED);
		}

		// type validation
		if (pet.isNew() && pet.getType() == null) {
			errors.rejectValue("type", REQUIRED, REQUIRED);
		}

		// birth date validation
		if (pet.getBirthDate() == null) {
			errors.rejectValue("birthDate", REQUIRED, REQUIRED);
		}

		try {
			updateVaccinationStatus(pet);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@WithSpan
	private boolean updateVaccinationStatus(Pet pet) throws IOException {
		// OkHttpClient client = new OkHttpClient();
		//
		// Request request = new
		// Request.Builder().url("https://647f4bb4c246f166da9084c7.mockapi.io/api/vetcheck/vaccines")
		// .build();
		//
		// String responseText = "";
		// try (Response response = client.newCall(request).execute()) {
		// responseText = response.body().string();
		// JSONObject Jobject = new JSONObject(responseText);
		// JSONArray Jarray = Jobject.getJSONArray("employees");
		//
		// }
		// catch (JSONException e) {
		// throw new RuntimeException(e);
		// }
		return true;

	}

	/**
	 * This Validator validates *just* Pet instances
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return Pet.class.isAssignableFrom(clazz);
	}

}
