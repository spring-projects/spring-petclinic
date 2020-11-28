package org.springframework.samples.petclinic.common;

public class CommonParameter {

	public static final int CITY_MAX = 50;

	public static final int COUNTRY_MAX = 50;

	public static final String DEFAULT_PROVIDER = "local";

	public static final int EMAIL_MAX = 255;

	public static final int EMAIL_MIN = 4;

	public static final String EMAIL_REGEXP = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

	public static final int FIRSTNAME_MAX = 50;

	public static final int FIRSTNAME_MIN = 2;

	public static final int LASTNAME_MAX = 50;

	public static final int LASTNAME_MIN = 2;

	public static final int PASSWORD_MAX = 255;

	public static final int PASSWORD_MIN = 4;

	public static final int PHONE_MAX = 14;

	public static final String PHONE_REGEXP = "^(?:(?:\\+|00)33[\\s.-]{0,3}(?:\\(0\\)[\\s.-]{0,3})?|0)[1-9](?:(?:[\\s.-]?\\d{2}){4}|\\d{2}(?:[\\s.-]?\\d{3}){2}|)$";

	public static final int STATUS_MAX = 10;

	public static final int STREET_MAX = 50;

	public static final int TOKEN_EXPIRATION = 60 * 24;

	public static final int ROLE_MAX = 10;

	public static final int PRIVILEGE_MAX = 10;

	public static final int ZIP_MAX = 6;

	public static final int ZIP_MIN = 5;

	private CommonParameter() {
		throw new IllegalStateException("Utility class");
	}

}
