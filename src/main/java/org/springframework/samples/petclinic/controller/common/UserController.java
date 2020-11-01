package org.springframework.samples.petclinic.controller.common;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.samples.petclinic.model.common.User;
import org.springframework.samples.petclinic.model.common.WebSocketMessage;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {


	@MessageMapping("/user")
	@SendTo("/topic/user")
	public WebSocketMessage getUser(User user) {

		return new WebSocketMessage("Hi " + user.getFirstName() +  user.getLastName());
	}
}
