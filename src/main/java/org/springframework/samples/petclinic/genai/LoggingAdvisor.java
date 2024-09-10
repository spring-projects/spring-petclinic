package org.springframework.samples.petclinic.genai;

import java.util.Map;

import org.springframework.ai.chat.client.AdvisedRequest;
import org.springframework.ai.chat.client.RequestResponseAdvisor;

public class LoggingAdvisor implements RequestResponseAdvisor {

	@Override
	public AdvisedRequest adviseRequest(AdvisedRequest request, Map<String, Object> context) {
		System.out.println("Request: " + request);
		return request;
	}
}