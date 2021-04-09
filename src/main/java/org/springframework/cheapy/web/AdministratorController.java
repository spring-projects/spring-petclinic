package org.springframework.cheapy.web;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.cheapy.model.FoodOffer;
import org.springframework.cheapy.model.NuOffer;
import org.springframework.cheapy.model.SpeedOffer;
import org.springframework.cheapy.model.StatusOffer;
import org.springframework.cheapy.model.TimeOffer;
import org.springframework.cheapy.model.Usuario;
import org.springframework.cheapy.service.ClientService;
import org.springframework.cheapy.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdministratorController {

	private static final String VIEWS_USUARIO_CREATE_OR_UPDATE_FORM = "usuarios/createOrUpdateUsuarioForm";

	private final UsuarioService usuarioService;

	public AdministratorController(final UsuarioService usuarioService, ClientService clientService) {
		this.usuarioService = usuarioService;
	}
	
	@GetMapping("/administrators/usuarios")
	public String processFindUsuariosForm(Map<String, Object> model) {
		List<Usuario> usuarioLs = this.usuarioService.findAllUsuario();
		model.put("usuarioLs", usuarioLs);
		return "usuarios/usuariosList";
	}

	@GetMapping("/administrators/usuarios/{username}")
	public String processShowForm(@PathVariable("username") String username, Map<String, Object> model) {
		Usuario usuario = this.usuarioService.findByUsername(username);
		model.put("usuario", usuario);
		return "usuarios/usuariosShow"; 
	}
	
	@GetMapping(value = "/administrators/usuarios/{username}/disable")
	public String disableUsuario(@PathVariable("username") final String username, final ModelMap model) {

		Usuario usuario = this.usuarioService.findByUsername(username);
		model.put("usuario", usuario);
		return "usuarios/usuariosDisable";
	}

	@PostMapping(value = "/administrators/usuarios/{username}/disable")
	public String disableUsuarioForm(@PathVariable("username") final String username, final ModelMap model, final HttpServletRequest request) {

		Usuario usuario = this.usuarioService.findByUsername(username);
		usuario.getUsuar().setEnabled(false);
		this.usuarioService.saveUsuario(usuario);
		return "redirect:/administrators/usuarios";
	}
}
