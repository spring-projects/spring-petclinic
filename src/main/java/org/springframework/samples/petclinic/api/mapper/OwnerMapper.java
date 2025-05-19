package org.springframework.samples.petclinic.api.mapper;

import org.springframework.samples.petclinic.api.dto.OwnerDto;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.stereotype.Component;

@Component
public class OwnerMapper {
	public OwnerDto toDto(Owner owner) {
		OwnerDto dto = new OwnerDto();
		dto.setId(owner.getId());
		dto.setFirstName(owner.getFirstName());
		dto.setLastName(owner.getLastName());
		dto.setAddress(owner.getAddress());
		dto.setCity(owner.getCity());
		dto.setTelephone(owner.getTelephone());
		return dto;
	}

	public Owner toEntity(OwnerDto dto) {
		Owner owner = new Owner();
		owner.setId(dto.getId());
		owner.setFirstName(dto.getFirstName());
		owner.setLastName(dto.getLastName());
		owner.setAddress(dto.getAddress());
		owner.setCity(dto.getCity());
		owner.setTelephone(dto.getTelephone());
		return owner;
	}
}
