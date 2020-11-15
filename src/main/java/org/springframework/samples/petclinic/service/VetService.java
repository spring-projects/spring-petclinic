package org.springframework.samples.petclinic.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.internal.util.Lists;
import org.springframework.samples.petclinic.dto.SpecialtyDTO;
import org.springframework.samples.petclinic.dto.VetDTO;
import org.springframework.samples.petclinic.model.business.Specialty;
import org.springframework.samples.petclinic.repository.SpecialtyRepository;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.samples.petclinic.model.business.Vet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Simple Service between Vet entity and VetDTO Data Transfert Object.
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Service("VetService")
public class VetService implements BaseService<Vet, VetDTO> {

	private final VetRepository vetRepository;

	private final SpecialtyService specialtyService;

	private final ModelMapper modelMapper = new ModelMapper();

	public VetService(VetRepository vetRepository, SpecialtyRepository specialtyRepository) {
		this.vetRepository = vetRepository;
		this.specialtyService = new SpecialtyService(specialtyRepository);
	}

	@Override
	public Vet dtoToEntity(VetDTO dto) {
		if (dto != null) {
			Vet vet = modelMapper.map(dto, Vet.class);
			dto.getSpecialties().forEach(specialtyDTO -> {
				Specialty specialty = specialtyService.dtoToEntity(specialtyDTO);
				vet.addSpecialty(specialty);
			});
			return vet;
		}

		return new Vet();
	}

	@Override
	public VetDTO entityToDTO(Vet entity) {
		if (entity != null) {
			VetDTO vetDTO = modelMapper.map(entity, VetDTO.class);
			entity.getSpecialties().forEach(specialty -> {
				SpecialtyDTO specialtyDTO = specialtyService.entityToDTO(specialty);
				vetDTO.addSpecialty(specialtyDTO);
			});
			return vetDTO;
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
	public VetDTO save(VetDTO dto) {
		Vet vet = dtoToEntity(dto);
		vet = vetRepository.save(vet);

		return entityToDTO(vet);
	}

}
