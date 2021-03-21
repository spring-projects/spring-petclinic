
package org.springframework.cheapy.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.cheapy.model.User;

public interface UserRepository extends CrudRepository<User, String> {

	User findByUsername(String currentPrincipalName);

}
