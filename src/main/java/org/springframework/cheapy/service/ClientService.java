package org.springframework.cheapy.service;

import java.util.List;

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
	
	@Transactional
	public Client findByUsername(String username) throws DataAccessException {
		return this.clientRepository.findByUsername(username);
	}
	
	public void saveClient(final Client client) throws DataAccessException {
		this.clientRepository.save(client);
	}
	@Transactional
	public List<Client> findAllClient() throws DataAccessException {
		return this.clientRepository.findAllClient();
	}
}
