package org.springframework.cheapy.system;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cheapy.model.Authorities;
import org.springframework.cheapy.model.Client;
import org.springframework.cheapy.model.Code;
import org.springframework.cheapy.model.Municipio;
import org.springframework.cheapy.model.User;
import org.springframework.cheapy.model.Usuario;
import org.springframework.cheapy.service.AuthoritiesService;
import org.springframework.cheapy.service.ClientService;
import org.springframework.cheapy.service.UserService;
import org.springframework.cheapy.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SingUpController {

	//private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";

	@Autowired
	private final ClientService clientService;
	@Autowired
	private final UserService userService;
	@Autowired
	private final UsuarioService usuarioService;
	@Autowired
	private final AuthoritiesService authoritiesService;


	public SingUpController(final ClientService clientService, UserService userService, AuthoritiesService authoritiesService,
			UsuarioService usuarioService) {
		this.clientService = clientService;
		this.userService = userService;
		this.authoritiesService = authoritiesService;
		this.usuarioService = usuarioService;

	}
	
	

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	private boolean checkTimes(final Client client) {
		boolean res = false;
		if(client.getFinish()==null || client.getInit()==null || client.getFinish().isAfter(client.getInit())) {
			res = true;
		}
		return res;
	}

	@GetMapping("/users/new")
	public String singUpUserForm(Map<String, Object> model) {
		Usuario usuario = new Usuario();
		
		User user=new User();
		
		usuario.setUsuar(user);
		model.put("municipio", Municipio.values());
		model.put("usuario", usuario);
		//model.put("user", user);
		return "singup/singUpUser";
	}

	@PostMapping("/users/new")
	public String singUpUserForm(/*@Valid User user,*/ @Valid Usuario usuario, BindingResult result) {
		Authorities auth=new Authorities();
		User user= usuario.getUsuar();
		user.setEnabled(true);
		usuario.setUsuar(user);
		auth.setUsername(user.getUsername());
		auth.setAuthority("usuario");
		if (result.hasErrors()) {
			return "singup/singUpUser";
		}
		else {
			//auth.setId(1);
			//this.authoritiesService.saveAuthorities(auth);
			this.usuarioService.saveUsuario(usuario);
			this.userService.saveUser(user);
			this.authoritiesService.saveAuthorities(usuario.getUsuar().getUsername(), "usuario");
			
			return "redirect:/";
		}
	}

	@GetMapping("/clients/new")
	public String singUpClientForm(Map<String, Object> model) {
		Client cliente = new Client();
		
		User user=new User();
		
		cliente.setUsuar(user);
		model.put("municipio", Municipio.values());
		model.put("cliente", cliente);
		//model.put("user", user);
		return "singup/singUpClient";
	}

	@PostMapping("/clients/new")
	public String singUpClientForm(/*@Valid User user,*/ @Valid Client cliente, final BindingResult result, final ModelMap model,
			HttpServletRequest request) {
		Authorities auth=new Authorities();
		String cod=cliente.getCode().getCode();
		Code code=this.clientService.findCodeByCode(cod);
		User user= cliente.getUsuar();
		user.setEnabled(true);
		cliente.setUsuar(user);
		auth.setUsername(user.getUsername());
		auth.setAuthority("client");
		if(!this.checkTimes(cliente)) {
			result.rejectValue("finish","" ,"La hora de cierre debe ser posterior a la hora de apertura");
			
		}
		if (result.hasErrors()) {
			model.put("municipio", Municipio.values());
			model.put("cliente", cliente);
			return "singup/singUpClient";
		}else if(code.getActivo().equals(false)) {
			return "error";
		}else {
			code.setActivo(false);
			this.clientService.saveCode(code);
			cliente.setCode(code);
			this.clientService.saveClient(cliente);
			this.userService.saveUser(user);
			this.authoritiesService.saveAuthorities(cliente.getUsuar().getUsername(), "client");
			
			
			return "redirect:/";
		}
	}
}
