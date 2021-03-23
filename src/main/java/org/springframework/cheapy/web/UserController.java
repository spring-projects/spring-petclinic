/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cheapy.web;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cheapy.model.Authorities;
import org.springframework.cheapy.model.User;
import org.springframework.cheapy.service.AuthoritiesService;
import org.springframework.cheapy.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
public class UserController {

	private UserService userService;
	
	private AuthoritiesService authoritiesService;
	
//	@Autowired
//	public UserController (UserService userService, AuthoritiesService authoritiesService, 
//			ClienteService clienteService, FarmaceuticoService farmaceuticoService, ProveedorService proveedorService) {
//		this.userService = userService;
//		this.authoritiesService = authoritiesService;
//		this.clienteService = clienteService;
//		this.farmaceuticoService = farmaceuticoService;
//		this.proveedorService = proveedorService;
//	}
//	
//	@InitBinder
//	public void setAllowedFields(final WebDataBinder dataBinder) {
//		dataBinder.setDisallowedFields("id");
//	}
//	
//	@GetMapping("users")
//	private String showUserDetails(ModelMap model) {
//		User user = this.userService.getCurrentUser();
//		Authorities authority = this.authoritiesService.findAuthoritiyByUser(user);
//		
//		if(authority.getAuthority().equals("cliente")) {
//			Cliente cliente = this.clienteService.findClienteUser(user);
//			model.addAttribute("cliente", cliente);
//		}else if(authority.getAuthority().equals("proveedor")) {
//			Proveedor proveedor = this.proveedorService.findProveedorUser(user);
//			model.addAttribute("proveedor", proveedor);
//		}else if(authority.getAuthority().equals("farmaceutico")) {
//			Farmaceutico farmaceutico = this.farmaceuticoService.findFarmaceuticoByUser(user);
//			model.addAttribute("farmaceutico", farmaceutico);
//		}
//
//		log.info("El usuario '" + user.getUsername() + "' ha mostrado su informacion personal");
//		return "users/userDetails";
//	}
//	
//	@GetMapping("/users/new")
//	public String newUser(ModelMap model) {
//		Cliente cliente = new Cliente();
//		model.addAttribute("cliente", cliente);
//		model.addAttribute("dni", new String());
//		return "users/userRegister";
//	}
//	
//	@PostMapping("/users/new")
//	public String creationUser(@ModelAttribute("cliente") Cliente cliente, final BindingResult result, ModelMap model) {
//		if (result.hasErrors()) {
//			return "users/userRegister";
//		} else if(cliente.getUser() == null) {
//			try {
//				cliente = this.clienteService.clienteDni(cliente.getDni());
//			}catch(EntityNotFoundException ex) {
//				result.rejectValue("dni", "clienteNotFound");
//				return "users/userRegister";
//			}
//			cliente.setUser(new User());
//			model.addAttribute("cliente", cliente);
//			return "users/userRegister";
//		}else {
//			this.userService.saveUser(cliente.getUser());
//			this.authoritiesService.saveAuthorities(cliente.getUser().getUsername(), "cliente");
//			this.clienteService.saveCliente(cliente);
//			log.info("El cliente con dni '" + cliente.getDni() + "' se ha registrado como usuario");
//			return "redirect:../";
//		}
//	}
//	
//	@GetMapping("/users/password")
//	public String initChangePassword(ModelMap model) {
//		User currentUser = this.userService.getCurrentUser();
//		UserValidate user = new UserValidate(currentUser.getUsername(), "");
//		model.addAttribute("user", user);
//		return "users/passwordEdit";
//	}
//	
//	@PostMapping("/users/password")
//	public String changePassword(@ModelAttribute("user") UserValidate user, final BindingResult result, ModelMap model) {
//		if(result.hasErrors()) {
//			return "users/passwordEdit";
//		}else {
//			User CurrentUser = this.userService.getCurrentUser();
//			if(CurrentUser.getPassword().equals(user.getPassword()) && user.getNewPassword().equals(user.getValidPassword())) {
//				if(!user.getNewPassword().isEmpty()) {
//					CurrentUser.setPassword(user.getNewPassword());
//					this.userService.saveUser(CurrentUser);
//					log.info("El usuario '" + CurrentUser.getUsername() + "' ha cambiado satisfactoriamente su contraseña");
//					return "redirect:../";
//				}else {
//					FieldError err = new FieldError("PassException", "newPassword", "Introduce una nueva contraseña");
//					result.addError(err);
//					log.warn("El usuario '" + CurrentUser.getUsername() + "' ha tenido un error 'PassException'");
//					return "users/passwordEdit";
//				}
//			}else if(!CurrentUser.getPassword().equals(user.getPassword())){
//				FieldError err = new FieldError("PassException", "password", "Contraseña incorrecta");
//				result.addError(err);
//				log.warn("El usuario '" + CurrentUser.getUsername() + "' ha tenido un error 'PassException'");
//				return "users/passwordEdit";
//			}else {
//				FieldError err = new FieldError("PassException", "newPassword", "Las contraseñas no coinciden");
//				result.addError(err);
//				log.warn("El usuario '" + CurrentUser.getUsername() + "' ha tenido un error 'PassException'");
//				return "users/passwordEdit";
//			}
//		}
//	}
}
