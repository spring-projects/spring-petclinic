package org.springframework.samples.petclinic.common;

/**
 * Class for const view names to prevent error with view names
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
public final class CommonView {

	public static final String OWNER_OWNERS_R = "redirect:/owners/";

	public static final String OWNER_OWNERS_ID_R = "redirect:/owners/{ownerId}";

	public static final String OWNER_CREATE_OR_UPDATE = "owners/createOrUpdateOwnerForm";

	public static final String OWNER_FIND_OWNERS = "owners/findOwners";

	public static final String OWNER_OWNERS_LIST = "owners/ownersList";

	public static final String OWNER_DETAILS = "owners/ownerDetails";

	public static final String PET_CREATE_OR_UPDATE = "pets/createOrUpdatePetForm";

	public static final String VET_VETS_LIST = "vets/vetList";

	public static final String VISIT_CREATE_OR_UPDATE = "pets/createOrUpdateVisitForm";

	private CommonView() {
		throw new IllegalStateException("Utility class");
	}

}
