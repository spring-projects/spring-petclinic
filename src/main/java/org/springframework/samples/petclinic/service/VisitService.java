package org.springframework.samples.petclinic.service;

import org.modelmapper.ModelMapper;
import org.springframework.samples.petclinic.dto.VisitDTO;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

@Service("VisitService")
public class VisitService implements BaseService<Visit, VisitDTO> {

	private final VisitRepository visitRepository;

	private final ModelMapper modelMapper = new ModelMapper();

	public VisitService(VisitRepository visitRepository) {
		this.visitRepository = visitRepository;
	}

	@Override
	public Visit dtoToEntity(VisitDTO dto) {
		return modelMapper.map(dto, Visit.class);
	}

	@Override
	public VisitDTO entityToDTO(Visit entity) {
		return modelMapper.map(entity, VisitDTO.class);
	}

	@Override
	public Collection<VisitDTO> entitiesToDTOS(Collection<Visit> entities) {
		Collection<VisitDTO> dtos = new HashSet<>();

		for (Visit entity : entities) {
			dtos.add(entityToDTO(entity));
		}
		return dtos;
	}

	@Override
	public Collection<Visit> dtosToEntities(Collection<VisitDTO> dtos) {
		Collection<Visit> entities = new HashSet<>();

		for (VisitDTO dto : dtos) {
			entities.add(dtoToEntity(dto));
		}

		return entities;
	}

	public void save(VisitDTO visitDTO) {
		Visit visit = dtoToEntity(visitDTO);
		visitRepository.save(visit);
	}

	public Collection<VisitDTO> findByPetId(Integer petId) {
		Collection<Visit> visits = visitRepository.findByPetId(petId);
		Collection<VisitDTO> visitDTOS = new HashSet<>();

		for (Visit visit : visits) {
			visitDTOS.add(entityToDTO(visit));
		}

		return visitDTOS;
	}

}
