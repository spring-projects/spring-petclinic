package org.springframework.samples.petclinic.service;

import org.modelmapper.ModelMapper;
import org.springframework.samples.petclinic.dto.SpecialtyDTO;
import org.springframework.samples.petclinic.vet.Specialty;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

@Service("SpecialityService")
public class SpecialityService implements BaseService<Specialty, SpecialtyDTO> {
	private final ModelMapper modelMapper = new ModelMapper();

	@Override
	public Specialty dtoToEntity(SpecialtyDTO dto) {
		return modelMapper.map(dto, Specialty.class);
	}

	@Override
	public SpecialtyDTO entityToDTO(Specialty entity) {
		return modelMapper.map(entity, SpecialtyDTO.class);
	}

	@Override
	public Collection<SpecialtyDTO> entitiesToDTOS(Collection<Specialty> entities) {
		Collection<SpecialtyDTO> dtos = new HashSet<>();

		for(Specialty entity:entities) {
			dtos.add(entityToDTO(entity));
		}

		return dtos;
	}

	@Override
	public Collection<Specialty> dtosToEntities(Collection<SpecialtyDTO> dtos) {
		Collection<Specialty> entities = new HashSet<>();

		for(SpecialtyDTO dto: dtos) {
			entities.add(dtoToEntity(dto));
		}

		return entities;
	}
}
