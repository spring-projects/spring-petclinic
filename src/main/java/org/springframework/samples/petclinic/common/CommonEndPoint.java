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

	private CommonEndPoint() {
		throw new IllegalStateException("Utility class");
	}
}
