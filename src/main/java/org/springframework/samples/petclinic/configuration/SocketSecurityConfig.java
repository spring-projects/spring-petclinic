package org.springframework.samples.petclinic.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class SocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

	@Override
	protected void configureInbound(MessageSecurityMetadataSourceRegistry message) {
		// @formatter:off

		// message types other than MESSAGE and SUBSCRIBE
		message.simpDestMatchers("/app/**").permitAll()
			.simpSubscribeDestMatchers("/topic/**").permitAll()
			// catch all
			.anyMessage().denyAll();

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
