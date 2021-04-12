
package org.springframework.cheapy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cheapy.model.Usuario;

package org.springframework.cheapy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cheapy.model.FoodOffer;
import org.springframework.cheapy.model.User;
import org.springframework.cheapy.model.Usuario;
import org.springframework.cheapy.repository.UserRepository;

import org.springframework.cheapy.repository.UsuarioRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

	private UsuarioRepository usuarioRepository;

	@Autowired
	public UsuarioService(final UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	
	@Transactional
	public Usuario getCurrentUsuario() throws DataAccessException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		return this.usuarioRepository.findByUsername(username);
	}
	
	@Transactional
	public Usuario findByUsername(String username) throws DataAccessException {
		return this.usuarioRepository.findByUsername(username);
	}
	
	@Transactional
	public List<Usuario> findAllUsuario() throws DataAccessException {
		return this.usuarioRepository.findAllUsuario();
	}
	
	@Transactional
	public List<Usuario> findUsuarioEnabled() throws DataAccessException {
		return this.usuarioRepository.findUsuarioEnabled();
	}

	@Transactional
	public void saveUsuario(final Usuario usuario) throws DataAccessException {
		this.usuarioRepository.save(usuario);
	}
}
