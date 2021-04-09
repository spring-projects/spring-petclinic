package org.springframework.cheapy.repository;

import java.util.List;

import org.springframework.cheapy.model.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

public interface ClientRepository extends Repository<Client, String> {

	@Query("SELECT client FROM Client client WHERE username =:username")
	@Transactional(readOnly = true)
	Client findByUsername(String username);
	
	@Query("SELECT client FROM Client client")
	@Transactional(readOnly = true)
	List<Client> findAllClient();
	
	
	void save(Client client);
	
}
