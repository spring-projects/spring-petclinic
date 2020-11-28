package org.springframework.samples.petclinic.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.samples.petclinic.model.common.WebSocketMessage;

/**
 * Class to extend for calling websocket sending messages
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
public class WebSocketSender {

	private static final int WAITING_TIME = 200; // delay of frontend page changing

	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;


	public void sendMessage(String message, String type) {
		// Send message asynchronously
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(WAITING_TIME);
				}
				catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
				simpMessagingTemplate.convertAndSend("/topic/public", new WebSocketMessage(message, type));
			}
		}).start();
	}

	public void sendAlertMessage(String message) {
		sendMessage(message, WebSocketMessage.ALERT);
	}

	public void sendErrorMessage(String message) {
		sendMessage(message, WebSocketMessage.ERROR);
	}

	public void sendInfoMessage(String message) {
		sendMessage(message, WebSocketMessage.INFO);
	}

	public void sendSuccessMessage(String message) {
		sendMessage(message, WebSocketMessage.SUCCESS);
	}

	public void sendWarningMessage(String message) {
		sendMessage(message, WebSocketMessage.WARNING);
	}

}
