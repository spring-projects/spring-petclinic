package org.springframework.samples.petclinic.service;

import org.modelmapper.ModelMapper;
import org.springframework.samples.petclinic.dto.VisitDTO;
import org.springframework.samples.petclinic.model.business.Visit;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Simple Service between Visit entity and VisitDTO Data Transfert Object.
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Service("VisitService")
public class VisitService implements BaseService<Visit, VisitDTO> {

	private final VisitRepository visitRepository;

	private final ModelMapper modelMapper = new ModelMapper();

	public VisitService(VisitRepository visitRepository) {
		this.visitRepository = visitRepository;
	}

	@Override
	public Visit dtoToEntity(VisitDTO dto) {
		if (dto != null) {
			return modelMapper.map(dto, Visit.class);
		}

		return new Visit();
	}

	@Override
	public VisitDTO entityToDTO(Visit entity) {
		if (entity != null) {
			return modelMapper.map(entity, VisitDTO.class);
		}

		return new VisitDTO();
	}

	@Override
	public List<VisitDTO> entitiesToDTOS(List<Visit> entities) {
		List<VisitDTO> dtos = new ArrayList<>();

		entities.forEach(entity -> dtos.add(entityToDTO(entity)));

		return dtos;
	}

	@Override
	public List<Visit> dtosToEntities(List<VisitDTO> dtos) {
		List<Visit> entities = new ArrayList<>();

		dtos.forEach(dto -> entities.add(dtoToEntity(dto)));

		return entities;
	}

	@Override
	public VisitDTO findById(int id) {
		return entityToDTO(visitRepository.findById(id));
	}

	@Override
	public List<VisitDTO> findAll() {
		return entitiesToDTOS(visitRepository.findAll());
	}

	@Override
	public VisitDTO save(VisitDTO visitDTO) {
		Visit visit = dtoToEntity(visitDTO);
		visit = visitRepository.save(visit);

		return entityToDTO(visit);
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
