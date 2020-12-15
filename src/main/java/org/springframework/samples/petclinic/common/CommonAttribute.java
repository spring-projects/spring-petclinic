package org.springframework.samples.petclinic.common;

/**
 * Class for const attributes names to prevent error with attributes names
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
public final class CommonAttribute {

	public static final String DESCRIPTION = "description";

	public static final String ID = "id";

	public static final String EMAIL = "email";

	public static final String GITHUB = "github";

	public static final String GITHUB_FIRSTNAME = "login";

	public static final String GITHUB_LASTNAME = "name";

	public static final String GITHUB_PROVIDER_ID = "id";

	public static final String GOOGLE = "google";

	public static final String GOOGLE_FIRSTNAME = "given_name";

	public static final String GOOGLE_LASTNAME = "family_name";

	public static final String GOOGLE_PROVIDER_ID = "sub";

	public static final String NAME = "name";

	public static final String NEW = "new";

	public static final String OWNER = "owner";

	public static final String OWNER_ID = "id";

	public static final String OWNER_LAST_NAME = "lastName";

	public static final String OWNER_FIRST_NAME = "firstName";

	public static final String OWNER_PHONE = "telephone";

	public static final String OWNER_ADDRESS = "address";

	public static final String OWNER_CITY = "city";

	public static final String OWNER_PETS = "pets";

	public static final String PASSWORD = "password";

	public static final String PET = "pet";

	public static final String PET_BIRTH_DATE = "birthDate";

	public static final String PET_NAME = "name";

	public static final String PET_TYPE = "type";

	public static final String SELECTIONS = "selections";

	public static final String TOKEN = "token";

	public static final String URLS = "urls";

	public static final String USER = "user";

	public static final String USER_ID = "userId";

	public static final String VETS = "vets";

	public static final String VISIT = "visit";

	public static final String VISIT_DATE = "date";

	public static final String VISIT_DESCRIPTION = "description";

	public static final String VISIT_PET_ID = "petId";

	public static final String VISITS = "visits";

	private CommonAttribute() {
		throw new IllegalStateException("Utility class");
	}

}
