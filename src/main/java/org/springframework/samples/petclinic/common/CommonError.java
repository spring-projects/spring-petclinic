package org.springframework.samples.petclinic.common;

/**
 * Class for const error names to prevent errors
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
public final class CommonError {
	public static final String DUPLICATE_ARGS = "duplicate";
	public static final String DUPLICATE_MESSAGE = "already exists";
	public static final String NOT_FOUND_ARGS = "notFound";
	public static final String NOT_FOUND_MESSAGE = "notFound";

	private CommonError() {
		throw new IllegalStateException("Utility class");
	}
}
