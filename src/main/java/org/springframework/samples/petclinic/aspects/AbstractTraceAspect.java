package org.springframework.samples.petclinic.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Aspect to illustrate Spring-driven load-time weaving.
 *
 * @author Ramnivas Laddad
 * @since 2.5
 */
@Aspect
public abstract class AbstractTraceAspect {

	private static final Logger logger = LoggerFactory.getLogger(AbstractTraceAspect.class);
	
	@Pointcut
	public abstract void traced();
	
	@Before("traced()")
	public void trace(JoinPoint.StaticPart jpsp) {
		if (logger.isTraceEnabled()) {
			logger.trace("Entering " + jpsp.getSignature().toLongString());
		}
	}

}
