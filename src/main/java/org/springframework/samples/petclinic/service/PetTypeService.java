package org.springframework.samples.petclinic.service;

import org.modelmapper.ModelMapper;
import org.springframework.samples.petclinic.dto.PetTypeDTO;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

@Service("PerTypeService")
public class PetTypeService implements BaseService<PetType, PetTypeDTO> {

	private final PetRepository petRepository;

	private final ModelMapper modelMapper = new ModelMapper();

	public PetTypeService(PetRepository petRepository) {
		this.petRepository = petRepository;
	}

	@Override
	public PetType dtoToEntity(PetTypeDTO dto) {
		return modelMapper.map(dto, PetType.class);
	}

	@Override
	public PetTypeDTO entityToDTO(PetType entity) {
		return modelMapper.map(entity, PetTypeDTO.class);
	}

	@Override
	public Collection<PetTypeDTO> entitiesToDTOS(Collection<PetType> entities) {
		Collection<PetTypeDTO> dtos = new HashSet<>();

		for (PetType entity : entities) {
			dtos.add(entityToDTO(entity));
		}

		return dtos;
	}

	@Override
	public Collection<PetType> dtosToEntities(Collection<PetTypeDTO> dtos) {
		Collection<PetType> entities = new HashSet<>();

		for (PetTypeDTO dto : dtos) {
			entities.add(dtoToEntity(dto));
		}

		return entities;
	}

	public Collection<PetTypeDTO> findPetTypes() {
		Collection<PetType> petTypes = petRepository.findPetTypes();
		return entitiesToDTOS(petTypes);
	}

}
