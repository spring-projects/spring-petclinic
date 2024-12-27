public class PetIdentifier {
	private final String name;
	private final Integer id;

	public PetIdentifier(Integer id) {
		this.id = id;
		this.name = null;
	}

	public PetIdentifier(String name) {
		this.name = name;
		this.id = null;
	}

	public boolean matches(Pet pet) {
		if (id != null) {
			return pet.getId().equals(id);
		}
		return pet.getName().equalsIgnoreCase(name);
	}
}
