package org.springframework.samples.petclinic.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newrelic.api.agent.Trace;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CatFactClient {
	final ObjectMapper objectMapper;

	public CatFactClient(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Trace
	public CatFact fetchFact() {
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			HttpGet get = new HttpGet("https://catfact.ninja/fact");
			try (CloseableHttpResponse response = httpClient.execute(get)) {
				return objectMapper.readValue(response.getEntity().getContent(), CatFact.class);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
