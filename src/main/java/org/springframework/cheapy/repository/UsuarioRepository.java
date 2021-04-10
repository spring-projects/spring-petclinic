
package org.springframework.cheapy.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cheapy.model.User;
import org.springframework.cheapy.model.Usuario;

public interface UsuarioRepository extends  Repository<Usuario, Integer> {

	@Query("SELECT u FROM User u WHERE username =:username")
	@Transactional(readOnly = true)
	User findByUsername(String username);
	
	
	void save(Usuario usuario);
}
