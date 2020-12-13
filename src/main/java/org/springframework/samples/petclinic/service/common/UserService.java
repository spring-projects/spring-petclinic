package org.springframework.samples.petclinic.service.common;

import org.modelmapper.ModelMapper;
import org.springframework.samples.petclinic.dto.common.UserDTO;
import org.springframework.samples.petclinic.model.common.User;
import org.springframework.samples.petclinic.repository.RoleRepository;
import org.springframework.samples.petclinic.repository.UserRepository;
import org.springframework.samples.petclinic.service.business.BaseService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Simple Service between User entity and UserDTO Data Transfert Object.
 *
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Service("UserService")
public class UserService implements BaseService<User, UserDTO> {

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	private final ModelMapper modelMapper = new ModelMapper();

	public UserService(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	@Override
	public User dtoToEntity(UserDTO dto) {

		if (dto == null) {
			return null;
		}

		return modelMapper.map(dto, User.class);
	}

	@Override
	public UserDTO entityToDTO(User entity) {
		if (entity == null) {
			return null;
		}

		UserDTO userDto = modelMapper.map(entity, UserDTO.class);
		userDto.setMatchingPassword(entity.getPassword());

		return userDto;
	}

	@Override
	public List<UserDTO> entitiesToDTOS(List<User> entities) {
		List<UserDTO> dtos = new ArrayList<>();

		entities.forEach(entity -> dtos.add(entityToDTO(entity)));

		return dtos;
	}

	@Override
	public List<User> dtosToEntities(List<UserDTO> dtos) {
		List<User> entities = new ArrayList<>();

		dtos.forEach(dto -> entities.add(dtoToEntity(dto)));

		return entities;
	}

	@Override
	public UserDTO findById(int id) {
		User user = userRepository.findById(id);

		return entityToDTO(user);
	}

	@Override
	public List<UserDTO> findAll() {
		Collection<User> users = userRepository.findAll();
		List<UserDTO> userDTOS = new ArrayList<>();

		users.forEach(user -> userDTOS.add(entityToDTO(user)));

		return userDTOS;
	}

	@Override
	public UserDTO save(UserDTO dto) {
		User user = dtoToEntity(dto);

		user = userRepository.save(user);

		return entityToDTO(user);
	}

	public UserDTO delete(UserDTO dto) {
		User user = dtoToEntity(dto);

		// remove user from roles
		User finalUser = user;
		user.getRoles().forEach(role -> {
			role.removeUser(finalUser);
			roleRepository.save(role);
		});
		user = userRepository.delete(user);

		return entityToDTO(user);
	}

	public UserDTO findByEmail(String email) {
		User user = userRepository.findByEmail(email);

		return entityToDTO(user);
	}

	public boolean existByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

}
