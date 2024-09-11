package org.springframework.samples.petclinic.genai;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ai.chat.client.AdvisedRequest;
import org.springframework.ai.chat.client.RequestResponseAdvisor;

/**
 * A ChatClient Advisor that adds logs on the requests being sent to the LLM.
 *
 * @author Oded Shopen
 */
public class LoggingAdvisor implements RequestResponseAdvisor {

	private static final Log log = LogFactory.getLog(LoggingAdvisor.class);

	@Override
	public AdvisedRequest adviseRequest(AdvisedRequest request, Map<String, Object> context) {
		log.info("Request: {}" + request);
		return request;
	}

}