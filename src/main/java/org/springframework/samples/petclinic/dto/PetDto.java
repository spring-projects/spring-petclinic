package org.springframework.samples.petclinic.dto;

import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.PetType;

import java.time.LocalDate;

public class PetDto {

	private Long id;

	private LocalDate birthDate;

	private PetType type;

	private Owner owner;

	public PetDto() {
	}

	public PetDto(Long id, LocalDate birthDate, PetType type, Owner owner) {
		this.id = id;
		this.birthDate = birthDate;
		this.type = type;
		this.owner = owner;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public PetType getType() {
		return type;
	}

	public void setType(PetType type) {
		this.type = type;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

}
