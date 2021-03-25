package org.springframework.cheapy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cheapy.model.Client;
import org.springframework.cheapy.repository.ClientRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {

	private ClientRepository clientRepository;
	
	@Autowired
	public ClientService(final ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	@Transactional
	public Client getCurrentClient() throws DataAccessException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		return this.clientRepository.findByUsername(username);
	}
	
}
