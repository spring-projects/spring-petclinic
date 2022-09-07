package org.springframework.samples.petclinic.api.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newrelic.api.agent.Trace;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DogFactClient {
	private final ObjectMapper objectMapper;

	public DogFactClient(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Trace
	public DogFact fetchDogFact() {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpGet get = new HttpGet("https://www.dogfactsapi.ducnguyen.dev/api/v1/facts/?number=1");
			try (CloseableHttpResponse response = httpClient.execute(get)) {
				return objectMapper.readValue(response.getEntity().getContent(), DogFact.class);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
