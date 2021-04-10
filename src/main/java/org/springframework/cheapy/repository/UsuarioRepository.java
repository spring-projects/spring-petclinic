package org.springframework.cheapy.repository;

import java.util.List;

import org.springframework.cheapy.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

public interface UsuarioRepository extends Repository<Usuario, String> {

	@Query("SELECT usuario FROM Usuario usuario WHERE username =:username")
	@Transactional(readOnly = true)
	Usuario findByUsername(String username);
	
	@Query("SELECT usuario FROM Usuario usuario")
	@Transactional(readOnly = true)
	List<Usuario> findAllUsuario();
	
	void save(Usuario usuario);
	
}
