package org.springframework.samples.petclinic.system;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
// Ordering is needed to be executed before cache interceptor
@Order(1)
@Component
public class AddSpansToCache {

	@Value("${petclinic.outboudExternalService.serviceType:ehcache}")
	String serviceType;

	@Value("${wavefront.application.name:Cache}")
	String applicationName;

	@Value("${petclinic.outboundExternalService.ComponentName:ehcache}")
	String componentName;

	final Tracer tracer;

	public AddSpansToCache(Tracer tracer) {
		this.tracer = tracer;
	}

	@Around("@annotation(org.springframework.cache.annotation.Cacheable)")
	public Object AddSpan(ProceedingJoinPoint joinpoint) throws Throwable {

		String joinPointName = joinpoint.getSignature().toString();
		Span newSpan = this.tracer.nextSpan().name(joinPointName).start();

		newSpan.tag("span.kind", String.valueOf(Span.Kind.CLIENT));
		newSpan.tag("_outboundExternalService", serviceType);
		newSpan.tag("_externalApplication", applicationName);
		newSpan.tag("_externalComponent", componentName);
		try {
			return joinpoint.proceed();
		}
		catch (RuntimeException | Error e) {
			newSpan.error(e);
			newSpan.event(String.valueOf(e));
			throw e;
		}
		finally {
			newSpan.end();
		}

	}

}
