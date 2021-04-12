package org.springframework.cheapy.repository;


import java.util.Optional;
import java.util.List;


import org.springframework.cheapy.model.Client;
import org.springframework.cheapy.model.Code;
import org.springframework.cheapy.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

public interface ClientRepository extends Repository<Client, Integer> {

	@Query("SELECT client FROM Client client WHERE username =:username")
	@Transactional(readOnly = true)
	Client findByUsername(String username);

	Optional<Client> findById(Integer id);
	

//	void save(Client client);

	@Query("SELECT client FROM Client client")
	@Transactional(readOnly = true)
	List<Client> findAllClient();
	
	void save(Client client);

}

