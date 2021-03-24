
package org.springframework.cheapy.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.cheapy.model.Client;

public interface ClientRepository extends CrudRepository<Client, String> {

	Client findByUsername(String currentPrincipalName);

}
