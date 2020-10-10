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

	public static final String REQUIRED_ARGS = "required";

	public static final String REQUIRED_MESSAGE = "required";

	private CommonError() {
		throw new IllegalStateException("Utility class");
	}

}
