package org.springframework.samples.petclinic.util;

import static java.lang.System.*;

public class DependencyLogger {

	public static void log(String message) {
		System.out.println("Trace Log: " + message);
	}

}
