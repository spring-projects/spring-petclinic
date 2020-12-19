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

	public static final String PASSWORD_WRONG_MESSAGE = "Wrong password !";

	public static final String PASSWORD_NOT_MATCHING_MESSAGE = "New passwords are not matching !";

	public static final String PASSWORD_EMPTY_MESSAGE = "Password can't be empty !";

	public static final String PASSWORD_LENGTH_MESSAGE = "Wrong password lenght !";

	private CommonError() {
		throw new IllegalStateException("Utility class");
	}

}
