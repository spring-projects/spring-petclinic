package org.springframework.samples.petclinic.controller.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.samples.petclinic.model.common.WebSocketMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Slf4j
@Component
public class WebSocketEventListener {

	@Autowired
	private SimpMessageSendingOperations sendingOperations;

	@EventListener
	public void handlewebSocketConnectListener(final SessionConnectedEvent event) {
		log.info("Ding dong. We have a new connection!");
	}

	@EventListener
	public void handlewebSocketDisconnectListener(final SessionConnectedEvent event) {

		final StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

		// final String username = (String)
		// headerAccessor.getSessionAttributes().get("username");

		sendingOperations.convertAndSend("/topic/user", new WebSocketMessage("Hello in event lmistener"));
	}

}
