package org.springframework.samples.petclinic.service;

import org.modelmapper.ModelMapper;
import org.springframework.samples.petclinic.dto.VetDTO;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

@Service("VetService")
public class VetService implements BaseService<Vet, VetDTO> {

	private final VetRepository vetRepository;

	private final ModelMapper modelMapper = new ModelMapper();

	public VetService(VetRepository vetRepository) {
		this.vetRepository = vetRepository;
	}

	@Override
	public Vet dtoToEntity(VetDTO dto) {
		return modelMapper.map(dto, Vet.class);
	}

	@Override
	public VetDTO entityToDTO(Vet entity) {
		return modelMapper.map(entity, VetDTO.class);
	}

	@Override
	public Collection<VetDTO> entitiesToDTOS(Collection<Vet> entities) {
		Collection<VetDTO> dtos = new HashSet<>();

		for (Vet entity : entities) {
			dtos.add(entityToDTO(entity));
		}

		return dtos;
	}

	@Override
	public Collection<Vet> dtosToEntities(Collection<VetDTO> dtos) {
		Collection<Vet> entities = new HashSet<>();

		for (VetDTO dto : dtos) {
			entities.add(dtoToEntity(dto));
		}

		return entities;
	}

	public Collection<VetDTO> findAll() {
		Collection<Vet> vets = vetRepository.findAll();
		return entitiesToDTOS(vets);
	}

}
