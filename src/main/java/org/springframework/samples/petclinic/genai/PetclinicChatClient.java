package org.springframework.samples.petclinic.genai;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.DEFAULT_CHAT_MEMORY_CONVERSATION_ID;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This REST controller is being invoked by the in order to interact with the LLM
 *
 * @author Oded Shopen
 */
@RestController
@RequestMapping("/")
@Profile("openai")
public class PetclinicChatClient {

	// ChatModel is the primary interfaces for interacting with an LLM
	// it is a request/response interface that implements the ModelModel
	// interface. Make suer to visit the source code of the ChatModel and
	// checkout the interfaces in the core spring ai package.
	private final ChatClient chatClient;

	public PetclinicChatClient(ChatClient.Builder builder, ChatMemory chatMemory) {
		// @formatter:off
		this.chatClient = builder
				.defaultSystem("""
			      		You are a friendly AI assistant designed to help with the management of a veterinarian pet clinic called Spring Petclinic.
			      		Your job is to answer questions about the existing veterinarians and to perform actions on the customer's behalf, mainly around
			      		pet owners, their pets and their visits.
			      		You are required to answer an a professional manner. If you don't know the answer, politely tell the customer
			      		you don't know the answer, then ask the customer a followup qusetion to try and clarify the question they are asking.
			      		If you do know the answer, provide the answer but do not provide any additional helpful followup questions.
			      		""")
				.defaultAdvisors(
						// Chat memory helps us keep context when using the chatbot for up to 10 previous messages.
						new MessageChatMemoryAdvisor(chatMemory, DEFAULT_CHAT_MEMORY_CONVERSATION_ID, 10), // CHAT MEMORY
						new LoggingAdvisor()
						)
				.build();
  }

  @PostMapping("/chatclient")
  public String exchange(@RequestBody String query) {
	  //All chatbot messages go through this endpoint and are passed to the LLM
	  return
	  this.chatClient
	  .prompt()
      .user(
          u ->
              u.text(query)
              )
      .call()
      .content();
  }
}
