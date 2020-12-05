package org.springframework.samples.petclinic.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class SocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

	@Override
	protected void configureInbound(MessageSecurityMetadataSourceRegistry message) {
		message.simpDestMatchers("/app/**").permitAll().simpSubscribeDestMatchers("/topic/**").permitAll().anyMessage()
				.permitAll();

		// @formatter:on
	}

	/**
	 * Disables CSRF for Websockets.
	 */
	@Override
	protected boolean sameOriginDisabled() {
		return true;
	}

}
