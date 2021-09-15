package org.springframework.samples.petclinic.system;

import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AddSpansToRepository {

	@Value("${petclinic.db.type:local}")
	String dbType;

	@Value("${petclinic.db.instance:localDB}")
	String dbInstance;

	final Tracer tracer;

	public AddSpansToRepository(Tracer tracer) {
		this.tracer = tracer;
	}

	// @Around("execution(* org.springframework.data.repository.Repository+.*(..)))")
	@Around("execution(* org.springframework.samples.petclinic.*.*Repository+.*(..))")
	public Object AddSpan(ProceedingJoinPoint joinpoint) throws Throwable {
		String joinPointName = joinpoint.getSignature().toString();
		Span newSpan = this.tracer.nextSpan().name(joinPointName).start();

		newSpan.tag("span.kind", "client");
		newSpan.tag("component", "java-jdbc");
		newSpan.tag("db.type", dbType);
		newSpan.tag("db.instance", dbInstance);

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
