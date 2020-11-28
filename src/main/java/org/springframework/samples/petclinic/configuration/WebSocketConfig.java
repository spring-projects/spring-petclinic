package org.springframework.samples.petclinic.configuration;

import org.springframework.context.annotation.Configuration;

import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * Configuration class to enable WebSocket and STOMP messaging.
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

	/*
	 * @Override public void registerStompEndpoints(StompEndpointRegistry
	 * stompEndpointRegistry) {
	 * stompEndpointRegistry.addEndpoint("/websocket").withSockJS(); }
	 *
	 * @Override public void configureMessageBroker(MessageBrokerRegistry registry) {
	 * registry.enableSimpleBroker("/topic");
	 * registry.setApplicationDestinationPrefixes("/app"); }
	 *
	 */

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic");
		config.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/websocket").setAllowedOrigins("*").withSockJS();
	}

	@Override
	protected void configureInbound(MessageSecurityMetadataSourceRegistry message) {
		message.nullDestMatcher().permitAll().simpDestMatchers("/app/**").permitAll()
				.simpSubscribeDestMatchers("/topic/**").permitAll().anyMessage().denyAll();
	}

	@Override
	protected boolean sameOriginDisabled() {
		return true;
	}

}
