package org.springframework.samples.petclinic.genai;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This REST controller implements a default behavior for the chat client when AI profile
 * is not in use. It will return a default message that chat is not available.
 *
 * @author Oded Shopen
 */
@RestController
@RequestMapping("/")
@Profile("!openai")
public class PetclinicDisabledChatClient {

	@PostMapping("/chatclient")
	public String exchange(@RequestBody String query) {
		return "Chat is currently unavailable. Please try again later.";
	}

}
