package org.springframework.samples.petclinic.common;

/**
 * Class for WebSocket messages
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
public class CommonWebSocket {

	public static final String OWNER_FIND_ERROR = "No Owner found !";

	public static final String OWNER_CREATED = "Owner created";

	public static final String OWNER_CREATION_ERROR = "Error creating Owner !";

	public static final String OWNER_UPDATED = "Owner updated";

	public static final String OWNER_UPDATED_ERROR = "Error updating Owner !";

	public static final String OWNER_DELETED = "Owner deleted";

	public static final String OWNER_DELETED_ERROR = "Error deleting Owner !";

	public static final String PET_FIND_ERROR = "No Pet found !";

	public static final String PET_CREATED = "Pet created";

	public static final String PET_CREATION_ERROR = "Error creating Pet !";

	public static final String PET_UPDATED = "Pet updated";

	public static final String PET_UPDATED_ERROR = "Error updating Pet !";

	public static final String PET_DELETED = "Pet deleted";

	public static final String PET_DELETED_ERROR = "Error deleting Pet !";

	public static final String VET_FIND_ERROR = "No Vet found !";

	public static final String VET_CREATED = "Vet created";

	public static final String VET_CREATION_ERROR = "Error creating Vet !";

	public static final String VET_UPDATED = "Vet updated";

	public static final String VET_UPDATED_ERROR = "Error updating Vet !";

	public static final String VET_DELETED = "Vet deleted";

	public static final String VET_DELETED_ERROR = "Error deleting Vet !";

	public static final String VISIT_FIND_ERROR = "No Visit found !";

	public static final String VISIT_CREATED = "Visit created";

	public static final String VISIT_CREATION_ERROR = "Error creating Visit !";

	public static final String VISIT_UPDATED = "Visit updated";

	public static final String VISIT_UPDATED_ERROR = "Error updating Visit !";

	public static final String VISIT_DELETED = "Visit deleted";

	public static final String VISIT_DELETED_ERROR = "Eror deleting Visit !";

	private CommonWebSocket() {
		throw new IllegalStateException("Utility class");
	}

}
