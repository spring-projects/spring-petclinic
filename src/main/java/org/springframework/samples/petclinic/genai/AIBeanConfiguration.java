package org.springframework.samples.petclinic.genai;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * A Configuration class for beans used by the Chat Client.
 *
 * @author Oded Shopen
 */
@Configuration
@Profile("openai")
public class AIBeanConfiguration {

	@Bean
	public ChatMemory chatMemory() {
		return new InMemoryChatMemory();
	}

	@Bean
	VectorStore vectorStore(EmbeddingModel embeddingModel) {
		return new SimpleVectorStore(embeddingModel);
	}

}
