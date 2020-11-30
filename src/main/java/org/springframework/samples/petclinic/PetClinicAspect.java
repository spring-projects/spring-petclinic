package org.springframework.samples.petclinic;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import brave.Span;
import brave.Span.Kind;
import brave.Tracer;

@Aspect
@Component
public class PetClinicAspect {

	@Value("${petclinic.db.type:local}")
	String dbType;

	@Value("${petclinic.db.instance:localDB}")
	String dbInstance;

	@Autowired
	Tracer tracer;

	// @Around("execution(* org.springframework.data.repository.Repository+.*(..)))")
	@Around("execution(* org.springframework.samples.petclinic.*.*Repository+.*(..)))")
	public Object AddSpan(ProceedingJoinPoint joinpoint) throws Throwable {
		Span newSpan = this.tracer.nextSpan().name(joinpoint.getSignature().toString()).start();

		try {
			newSpan.tag("component", "java-jdbc");
			newSpan.kind(Kind.CLIENT);
			newSpan.tag("db.type", dbType);
			newSpan.tag("db.instance", dbInstance);
			Object result = joinpoint.proceed();

			return result;
		}
		finally {
			newSpan.finish();
		}

	}

}