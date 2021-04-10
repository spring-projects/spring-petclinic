package org.springframework.cheapy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cheapy.model.FoodOffer;
import org.springframework.cheapy.model.User;
import org.springframework.cheapy.repository.UserRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

	private UserRepository userRepository;
	
	@Autowired
	public UserService(final UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Transactional
	public User getCurrentUser() throws DataAccessException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		return this.userRepository.findByUsername(username);
	}

	public void saveUser(final User user) throws DataAccessException {
		this.userRepository.save(user);
	}
}
