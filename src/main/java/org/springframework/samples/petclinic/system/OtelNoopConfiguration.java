package org.springframework.samples.petclinic.system;

import io.opentelemetry.api.OpenTelemetry;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@AutoConfigureOrder(1_000_000)
public class OtelNoopConfiguration {

	/**
	 * loading OpenTelemetry.noop() if OpenTelemetry is not already configured (via OpenTelemetryAutoConfiguration, or any other applicative way)
	 *
	 * @see io.opentelemetry.instrumentation.spring.autoconfigure.OpenTelemetryAutoConfiguration.OpenTelemetryBeanConfig#openTelemetry
	 */
	@ConditionalOnMissingBean(OpenTelemetry.class)
	@Bean
	public OpenTelemetry createOtel() {
		OpenTelemetry otelNoop = OpenTelemetry.noop();
		return otelNoop;
	}

}
