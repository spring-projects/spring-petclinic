package org.springframework.samples.petclinic.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.samples.petclinic.model.common.WebSocketMessage;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class WebSocketSchedulerConfiguration {

	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;

	@Scheduled(fixedDelay = 3000)
	public void sendMessages() {

		simpMessagingTemplate.convertAndSend("/topic/user", new WebSocketMessage("Fixed Delay Scheduler"));
	}
}
