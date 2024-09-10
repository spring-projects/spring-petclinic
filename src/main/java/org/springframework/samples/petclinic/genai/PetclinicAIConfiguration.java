package org.springframework.samples.petclinic.genai;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class PetclinicAIConfiguration {

	@Bean
	public RestClient restClient() {
		return RestClient.create();
	}

	@Bean
	public ChatMemory chatMemory() {
		return new InMemoryChatMemory();
	}

}
