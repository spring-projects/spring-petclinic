package org.springframework.cheapy.system;

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cheapy.model.Authorities;
import org.springframework.cheapy.model.Municipio;
import org.springframework.cheapy.model.Owner;
import org.springframework.cheapy.model.User;
import org.springframework.cheapy.model.Usuario;
import org.springframework.cheapy.service.AuthoritiesService;
import org.springframework.cheapy.service.ClientService;
import org.springframework.cheapy.service.OwnerService;
import org.springframework.cheapy.service.UserService;
import org.springframework.cheapy.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

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

	@GetMapping("/users/new")
	public String initCreationForm(Map<String, Object> model) {
		Usuario usuario = new Usuario();
		
		User user=new User();
		
		usuario.setUsuar(user);
		model.put("municipio", Municipio.values());
		model.put("usuario", usuario);
		//model.put("user", user);
		return "singup/singUpUser";
	}

	@PostMapping("/users/new")
	public String processCreationForm(/*@Valid User user,*/ @Valid Usuario usuario, BindingResult result) {
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

//	@GetMapping("/owners/find")
//	public String initFindForm(Map<String, Object> model) {
//		model.put("owner", new Owner());
//		return "owners/findOwners";
//	}
//
//	@GetMapping("/owners")
//	public String processFindForm(Owner owner, BindingResult result, Map<String, Object> model) {
//
//		// allow parameterless GET request for /owners to return all records
//		if (owner.getLastName() == null) {
//			owner.setLastName(""); // empty string signifies broadest possible search
//		}
//
//		// find owners by last name
//		Collection<Owner> results = this.ownerService.findByLastName(owner.getLastName());
//		if (results.isEmpty()) {
//			// no owners found
//			result.rejectValue("lastName", "notFound", "not found");
//			return "owners/findOwners";
//		}
//		else if (results.size() == 1) {
//			// 1 owner found
//			owner = results.iterator().next();
//			return "redirect:/owners/" + owner.getId();
//		}
//		else {
//			// multiple owners found
//			model.put("selections", results);
//			return "owners/ownersList";
//		}
//	}
//
//	@GetMapping("/owners/{ownerId}/edit")
//	public String initUpdateOwnerForm(@PathVariable("ownerId") int ownerId, Model model) {
//		Owner owner = this.ownerService.findOwnerById(ownerId);
//		model.addAttribute(owner);
//		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
//	}
//
//	@PostMapping("/owners/{ownerId}/edit")
//	public String processUpdateOwnerForm(@Valid Owner owner, BindingResult result,
//			@PathVariable("ownerId") int ownerId) {
//		if (result.hasErrors()) {
//			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
//		}
//		else {
//			owner.setId(ownerId);
//			this.ownerService.saveOwner(owner);
//			return "redirect:/owners/{ownerId}";
//		}
//	}
//	@GetMapping("/owners/{ownerId}")
//	public ModelAndView showOwner(@PathVariable("ownerId") int ownerId) {
//		ModelAndView mav = new ModelAndView("owners/ownerDetails");
//		Owner owner = this.ownerService.findOwnerById(ownerId);
//		
//		mav.addObject(owner);
//		return mav;
//	}
//	
}
