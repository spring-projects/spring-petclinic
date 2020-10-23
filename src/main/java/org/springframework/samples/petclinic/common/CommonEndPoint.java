package org.springframework.samples.petclinic.common;

/**
 * Class for const endpoint names to prevent error with endpoint names
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
public final class CommonEndPoint {

	public static final String OWNERS = "/owners";

	public static final String OWNERS_FIND = "/owners/find";

	public static final String OWNERS_ID = "/owners/{ownerId}";

	public static final String OWNERS_ID_EDIT = "/owners/{ownerId}/edit";

	public static final String OWNERS_NEW = "/owners/new";

	public static final String PETS_NEW = "/pets/new";

	public static final String PETS_ID_EDIT = "/pets/{petId}/edit";

	public static final String VETS = "/vets";

	public static final String VETS_HTML = "/vets.html";

	public static final String VISITS_NEW = "/owners/*/pets/{petId}/visits/new";

	public static final String VISITS_EDIT = "/owners/{ownerId}/pets/{petId}/visits/new";

	private CommonEndPoint() {
		throw new IllegalStateException("Utility class");
	}

}
