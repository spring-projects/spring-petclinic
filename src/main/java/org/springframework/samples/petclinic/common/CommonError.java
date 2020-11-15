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

	public static final String FORMAT_BETWEEN = "Length should be between : ";

	public static final String FORMAT_LESS = "Length should less than : ";

	public static final String EMAIL_FORMAT = "Not a valid email address !";

	public static final String PHONE_FORMAT = "Not a valid phone number !";

	private CommonError() {
		throw new IllegalStateException("Utility class");
	}

}
