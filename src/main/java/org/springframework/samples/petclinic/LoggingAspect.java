package org.springframework.samples.petclinic;


import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

	//Pointcut: all methods in package "model"
	@Before("execution(* model.BaseEntity.getId())")
	public void logBeforeMethod(){
		System.out.println("model.BaseEntity.getId...");
	}

	@Before("execution(* model.NamedEntity.getName())")
	public void logBeforeMethod2(){
		System.out.println("model.NamedEntity.getName...");
	}

	@Before("execution(* model.Person.*())")
	public void logBeforeMethod3(){
		System.out.println("model.Person");
	}
}
