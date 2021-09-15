package org.springframework.samples.petclinic.system;

import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.autoconfig.instrument.web.SleuthWebProperties;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@Component
public class AddSpanTagsToServlet extends GenericFilterBean {

	@Value("${petclinic.inboundExternalService.serviceType:LB}")
	String serviceType;

	@Value("${petclinic.inboundExternalService.ApplicationName:Proxy}")
	String applicationName;

	@Value("${petclinic.inboundExternalService.ComponentName:locallb}")
	String componentName;

	private final Tracer tracer;

	AddSpanTagsToServlet(Tracer tracer) {
		this.tracer = tracer;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		Span currentSpan = this.tracer.currentSpan();

		if (currentSpan == null) {
			chain.doFilter(request, response);
			return;
		}
		currentSpan.tag("_inboundExternalService", serviceType);
		currentSpan.tag("_externalApplication", applicationName);
		currentSpan.tag("_externalComponent", componentName);

		try {
			chain.doFilter(request, response);
		}
		catch (Exception e) {
			currentSpan.event(String.valueOf(e));
			throw e;
		}
	}

}
