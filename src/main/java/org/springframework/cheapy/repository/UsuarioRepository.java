
package org.springframework.cheapy.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.cheapy.model.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, String> {

	//Usuario findByUsername(String currentPrincipalName);

}
