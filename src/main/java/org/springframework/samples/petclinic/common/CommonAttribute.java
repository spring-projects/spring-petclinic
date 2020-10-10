package org.springframework.samples.petclinic.common;

public final class CommonAttribute {
	public static String NAME = "name";

	public static String OWNER = "owner";
	public static String OWNER_LAST_NAME = "lastName";
	public static String OWNER_FIRST_NAME = "firstName";
	public static String OWNER_PHONE = "telephone";
	public static String OWNER_ADDRESS = "address";
	public static String OWNER_CITY = "city";
	public static String OWNER_PETS = "pets";

	private CommonAttribute() {
		throw new IllegalStateException("Utility class");
	}
}
