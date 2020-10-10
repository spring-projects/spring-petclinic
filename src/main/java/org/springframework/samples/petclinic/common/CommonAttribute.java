package org.springframework.samples.petclinic.common;

/**
 * Class for const attributes names to prevent error with attributes names
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
public final class CommonAttribute {
	public static final String NAME = "name";
	public static final String SELECTIONS = "selections";

	public static final String OWNER = "owner";
	public static final String OWNER_ID = "id";
	public static final String OWNER_LAST_NAME = "lastName";
	public static final String OWNER_FIRST_NAME = "firstName";
	public static final String OWNER_PHONE = "telephone";
	public static final String OWNER_ADDRESS = "address";
	public static final String OWNER_CITY = "city";
	public static final String OWNER_PETS = "pets";

	public static final String PET = "pet";

	private CommonAttribute() {
		throw new IllegalStateException("Utility class");
	}
}
