/*
 * Integration tests that submit VisitDto via REST client (no Thymeleaf interaction)
 */
package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisabledInNativeImage
@AutoConfigureWebClient
class VisitDtoSubmissionTest {

	@Autowired
	private TestRestTemplate rest;

	private static final int OWNER_ID = 1;
	private static final int PET_ID = 1;

	@Test
	void testSuccessfultSubmission() {
		// Prepare form-urlencoded payload
		MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
		form.add("visitDate", LocalDate.now().toString());
		String description = "Routine check via REST";
		form.add("visitDescription", description);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		ResponseEntity<String> response = rest.postForEntity(
			"/owners/" + OWNER_ID + "/pets/" + PET_ID + "/visits/new",
			new HttpEntity<>(form, headers),
			String.class
		);

		// TestRestTemplate follows redirects by default, so we should land on owner page (200 OK)
		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		assertThat(response.getBody()).isNotNull();
		// The owner details page should contain the description we just submitted
		assertThat(response.getBody()).contains(description);
	}

	@Test
	void testFailureDueToMissingDescription() {
		// Missing/blank visitDescription should fail validation (@NotBlank)
		MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
		form.add("visitDate", LocalDate.now().toString());
		form.add("visitDescription", "");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		ResponseEntity<String> response = rest.postForEntity(
			"/owners/" + OWNER_ID + "/pets/" + PET_ID + "/visits/new",
			new HttpEntity<>(form, headers),
			String.class
		);

		// Expect no redirect; the form should be returned with validation error
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		// The generic 'Error' label is shown when a field has errors (from fragments/inputField.html)
		assertThat(response.getBody()).contains("Error");
		// The form title should be present as well
		assertThat(response.getBody()).contains("New", "Visit");
	}

	@Test
	void testFailureDueToWrongDateFormat() {
		// Provide a non-ISO date format; expected pattern is yyyy-MM-dd
		MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
		form.add("visitDate", "18/08/2025");
		form.add("visitDescription", "Wrong date format");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		ResponseEntity<String> response = rest.postForEntity(
			"/owners/" + OWNER_ID + "/pets/" + PET_ID + "/visits/new",
			new HttpEntity<>(form, headers),
			String.class
		);

		// Expect re-rendered form with validation error for date binding
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody()).contains("Error");
		// In case the model attribute is missing due to binding failure, the "New" prefix may be omitted
		assertThat(response.getBody()).contains("Visit");
	}
}
