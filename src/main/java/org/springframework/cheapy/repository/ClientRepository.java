
package org.springframework.cheapy.repository;


import org.springframework.cheapy.model.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, String> {


	Client findByUsername(String username);

}
