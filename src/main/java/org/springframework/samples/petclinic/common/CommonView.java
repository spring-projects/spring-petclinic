package org.springframework.samples.petclinic.common;

/**
 * Class for const view names to prevent error with view names
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
public final class CommonView {

	public static final String HOME = "redirect:/";

	public static final String OWNER_OWNERS_R = "redirect:/owners/";

	public static final String OWNER_OWNERS_ID_R = "redirect:/owners/{ownerId}";

	public static final String OWNER_CREATE_OR_UPDATE = "owners/createOrUpdateOwnerForm";

	public static final String OWNER_FIND_OWNERS = "owners/findOwners";

	public static final String OWNER_OWNERS_LIST = "owners/ownersList";

	public static final String OWNER_DETAILS = "owners/ownerDetails";

	public static final String PET_CREATE_OR_UPDATE = "pets/createOrUpdatePetForm";

	public static final String USER_REGISTRATION = "users/registration";

	public static final String USER_LOGIN = "/login";

	public static final String USER_LOGIN_R = "redirect:/login";

	public static final String USER_USERS_ID_R = "redirect:/users/{userId}";

	public static final String USER_CREATE_OR_UPDATE = "users/createOrUpdateUserForm";

	public static final String USER_DETAILS = "users/userDetails";

	public static final String USER_ADD = "users/user-add";

	public static final String USER_READ = "users/user-read";

	public static final String USER_READ_R = "redirect:/users/read/";

	public static final String USER_UPDATE = "users/user-update";

	public static final String USER_UPDATE_R = "redirect:/users/edit/";

	public static final String USER_UPDATE_PASSWORD = "users/user-password";

	public static final String USER_UPDATE_PASSWORD_R = "redirect:/users/password/edit/";

	public static final String VET_VETS_LIST = "vets/vetList";

	public static final String VISIT_CREATE_OR_UPDATE = "pets/createOrUpdateVisitForm";

	private CommonView() {
		throw new IllegalStateException("Utility class");
	}

}
