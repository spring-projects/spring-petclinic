package org.springframework.samples.petclinic;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;
import java.util.function.Supplier;

@Aspect
@Component
public class LoggingAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @FunctionalInterface
    private interface MethodLogger {
        void log(String className, String methodName, long executionTime);
    }

    private final MethodLogger defaultLogger = (className, methodName, executionTime) -> 
        logger.info("Method {}.{} completed in {} ms", className, methodName, executionTime);

    @Around("execution(* org.springframework.samples.petclinic..*.*(..))")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        Supplier<String> methodInfo = () -> String.format("%s.%s", className, methodName);
        logger.info("Starting method: {}", methodInfo.get());
        
        long startTime = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            return result;
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            defaultLogger.log(className, methodName, executionTime);
        }
    }
}
