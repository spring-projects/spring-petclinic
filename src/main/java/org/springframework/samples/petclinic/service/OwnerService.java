package org.springframework.samples.petclinic.service;

import org.modelmapper.ModelMapper;
import org.springframework.samples.petclinic.dto.OwnerDTO;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

@Service("OwnerService")
public class OwnerService implements BaseService<Owner, OwnerDTO> {

	private final OwnerRepository ownerRepository;

	private final ModelMapper modelMapper = new ModelMapper();

	public OwnerService(OwnerRepository ownerRepository) {
		this.ownerRepository = ownerRepository;
	}

	@Override
	public Owner dtoToEntity(OwnerDTO dto) {
		return modelMapper.map(dto, Owner.class);
	}

	@Override
	public OwnerDTO entityToDTO(Owner entity) {
		return modelMapper.map(entity, OwnerDTO.class);
	}

	@Override
	public Collection<OwnerDTO> entitiesToDTOS(Collection<Owner> entities) {
		Collection<OwnerDTO> dtos = new HashSet<>();

		for (Owner entity : entities) {
			dtos.add(entityToDTO(entity));
		}

		return dtos;
	}

	@Override
	public Collection<Owner> dtosToEntities(Collection<OwnerDTO> dtos) {
		Collection<Owner> entities = new HashSet<>();

		for (OwnerDTO dto : dtos) {
			entities.add(dtoToEntity(dto));
		}

		return entities;
	}

	public void save(OwnerDTO ownerDTO) {
		Owner owner = dtoToEntity(ownerDTO);
		ownerRepository.save(owner);
	}

	public Collection<OwnerDTO> findByLastName(String lastName) {
		Collection<Owner> owners = ownerRepository.findByLastName(lastName);
		return entitiesToDTOS(owners);
	}

	public OwnerDTO findById(int ownerId) {
		Owner owner = ownerRepository.findById(ownerId);
		return entityToDTO(owner);
	}

}
