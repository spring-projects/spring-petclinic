package org.springframework.samples.petclinic.adapters.dtos.owner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnerDto {

	private Integer id;

	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;

	@NotBlank
	private String address;

	@NotBlank
	private String city;

	@NotBlank
	@Pattern(regexp = "\\d{10}", message = "{telephone.invalid}")
	private String telephone;

	private List<PetDto> pets = new ArrayList<>();

	public boolean isNew() {
		return this.id == null;
	}

	public PetDto getPet(String name) {
		return getPet(name, false);
	}

	public PetDto getPet(Integer id) {
		for (PetDto pet : getPets()) {
			if (!pet.isNew()) {
				if (Objects.equals(pet.getId(), id)) {
					return pet;
				}
			}
		}
		return null;
	}

	public PetDto getPet(String name, boolean ignoreNew) {
		for (PetDto pet : getPets()) {
			String compName = pet.getName();
			if (compName != null && compName.equalsIgnoreCase(name)) {
				if (!ignoreNew || !pet.isNew()) {
					return pet;
				}
			}
		}
		return null;
	}

	public void addPet(PetDto pet) {
		if (pet.isNew()) {
			getPets().add(pet);
		}
	}

}
