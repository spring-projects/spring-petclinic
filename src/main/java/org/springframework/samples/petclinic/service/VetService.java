package org.springframework.samples.petclinic.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.internal.util.Lists;
import org.springframework.samples.petclinic.dto.VetDTO;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Simple Service between Vet entity and VetDTO Data Transfert Object.
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Service("VetService")
public class VetService implements BaseService<Vet, VetDTO> {

	private final VetRepository vetRepository;

	private final ModelMapper modelMapper = new ModelMapper();

	public VetService(VetRepository vetRepository) {
		this.vetRepository = vetRepository;
	}

	@Override
	public Vet dtoToEntity(VetDTO dto) {
		if (dto != null) {
			return modelMapper.map(dto, Vet.class);
		}

		return new Vet();
	}

	@Override
	public VetDTO entityToDTO(Vet entity) {
		if (entity != null) {
			return modelMapper.map(entity, VetDTO.class);
		}
		return new VetDTO();
	}

	@Override
	public List<VetDTO> entitiesToDTOS(List<Vet> entities) {
		List<VetDTO> dtos = new ArrayList<>();

		entities.forEach(entity -> dtos.add(entityToDTO(entity)));

		return dtos;
	}

	@Override
	public List<Vet> dtosToEntities(List<VetDTO> dtos) {
		List<Vet> entities = new ArrayList<>();

		dtos.forEach(dto -> entities.add(dtoToEntity(dto)));

		return entities;
	}

	@Override
	public VetDTO findById(int id) {
		return entityToDTO(vetRepository.findById(id));
	}

	public List<VetDTO> findAll() {
		Collection<Vet> vets = vetRepository.findAll();
		return entitiesToDTOS(Lists.from(vets.iterator()));
	}

	@Override
	public void save(VetDTO dto) {
		vetRepository.save(dtoToEntity(dto));
	}

}
