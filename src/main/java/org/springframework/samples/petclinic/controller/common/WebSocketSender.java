package org.springframework.samples.petclinic.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.samples.petclinic.model.common.WebSocketMessage;

public class WebSocketSender {
	public static final String OWNER_CREATED = "Owner created";
	public static final String OWNER_UPDATED = "Owner updated";
	public static final String OWNER_DELETED = "Owner deleted";

	public static final String PET_CREATED = "Pet created";
	public static final String PET_UPDATED = "Pet updated";
	public static final String PET_DELETED = "Pet deleted";

	public static final String VET_CREATED = "Vet created";
	public static final String VET_UPDATED = "Vet updated";
	public static final String VET_DELETED = "Vet deleted";

	public static final String VISIT_CREATED = "Visit created";
	public static final String VISIT_UPDATED = "Visit updated";
	public static final String VISIT_DELETED = "Visit deleted";


	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;

	public void sendMessages(String message) {
		simpMessagingTemplate.convertAndSend("/topic/public", new WebSocketMessage(message));
	}
}
