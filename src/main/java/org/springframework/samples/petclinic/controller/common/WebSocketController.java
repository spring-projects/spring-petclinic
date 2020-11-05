package org.springframework.samples.petclinic.controller.common;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.samples.petclinic.model.common.WebSocketMessage;
import org.springframework.stereotype.Controller;

public class WebSocketController {

	@MessageMapping("/user")
	@SendTo("/topic/user")
	public WebSocketMessage sendMessage(@Payload final WebSocketMessage message) {
		return message;
	}

	@MessageMapping("/websocket.newUser")
	@SendTo("/topic/public")
	public WebSocketMessage newUser(@Payload final WebSocketMessage message, SimpMessageHeaderAccessor headerAccessor) {
		headerAccessor.getSessionAttributes().put("username", message.getSender());
		return message;
	}

}
