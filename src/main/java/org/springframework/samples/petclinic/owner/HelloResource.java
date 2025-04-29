package org.springframework.samples.petclinic.owner;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloResource {

	@GetMapping("/hello")
	public String sayHello() {
		return "Hello from Copilot!";
	}

	@GetMapping("/greet")
	public String greetUser() {
		return "Greetings from Copilot!";
	}

}
