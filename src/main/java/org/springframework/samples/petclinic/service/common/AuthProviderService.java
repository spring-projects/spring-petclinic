package org.springframework.samples.petclinic.service.common;

import org.modelmapper.ModelMapper;
import org.springframework.samples.petclinic.dto.common.AuthProviderDTO;
import org.springframework.samples.petclinic.model.common.AuthProvider;
import org.springframework.samples.petclinic.repository.AuthProviderRepository;
import org.springframework.samples.petclinic.service.business.BaseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple Service between AuthProvider entity and AuthProviderDTO Data Transfert Object.
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Service("AuthProviderService")
public class AuthProviderService implements BaseService<AuthProvider, AuthProviderDTO> {

	private final AuthProviderRepository authProviderRepository;

	private final ModelMapper modelMapper = new ModelMapper();

	public AuthProviderService(AuthProviderRepository authProviderRepository) {
		this.authProviderRepository = authProviderRepository;
	}

	@Override
	public AuthProvider dtoToEntity(AuthProviderDTO dto) {
		if (dto != null) {
			return modelMapper.map(dto, AuthProvider.class);
		}

		return new AuthProvider();
	}

	@Override
	public AuthProviderDTO entityToDTO(AuthProvider entity) {
		if (entity != null) {
			return modelMapper.map(entity, AuthProviderDTO.class);
		}

		return new AuthProviderDTO();
	}

	@Override
	public List<AuthProviderDTO> entitiesToDTOS(List<AuthProvider> entities) {
		List<AuthProviderDTO> dtos = new ArrayList<>();

		entities.forEach(entity -> dtos.add(entityToDTO(entity)));

		return dtos;
	}

	@Override
	public List<AuthProvider> dtosToEntities(List<AuthProviderDTO> dtos) {
		List<AuthProvider> entities = new ArrayList<>();

		dtos.forEach(dto -> entities.add(dtoToEntity(dto)));

		return entities;
	}

	@Override
	public AuthProviderDTO findById(int providerId) {
		return entityToDTO(authProviderRepository.findById(providerId));
	}

	@Override
	public List<AuthProviderDTO> findAll() {
		return entitiesToDTOS(authProviderRepository.findAll());
	}

	@Override
	public AuthProviderDTO save(AuthProviderDTO dto) {
		AuthProvider authProvider = dtoToEntity(dto);
		authProvider = authProviderRepository.save(authProvider);

		return entityToDTO(authProvider);
	}

	public AuthProviderDTO findByName(String providerName) {
		return entityToDTO(authProviderRepository.findByName(providerName));
	}

}
