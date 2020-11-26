package org.springframework.samples.petclinic.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.common.*;
import org.springframework.samples.petclinic.controller.common.WebSocketSender;
import org.springframework.samples.petclinic.dto.common.CredentialDTO;
import org.springframework.samples.petclinic.dto.common.MessageDTO;
import org.springframework.samples.petclinic.dto.common.UserDTO;
import org.springframework.samples.petclinic.service.common.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Slf4j
@Controller
public class UserController extends WebSocketSender {

	private final UserService userService;

	private final CredentialService credentialService;

	private final RoleService roleService;

	private final SecurityServiceImpl securityService;

	private final EmailService emailService;

	public UserController(UserService userService, CredentialService credentialService, RoleService roleService,
			SecurityServiceImpl securityService, EmailService emailService) {
		this.userService = userService;
		this.credentialService = credentialService;
		this.roleService = roleService;
		this.securityService = securityService;
		this.emailService = emailService;
	}

	@InitBinder("user")
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields(CommonAttribute.USER_ID,"roles");
	}

	Map<String, String> oauth2AuthenticationUrls = new HashMap<>();

	@Autowired
	private ClientRegistrationRepository clientRegistrationRepository;

	@Autowired
	private OAuth2AuthorizedClientService authorizedClientService;

	@GetMapping(CommonEndPoint.REGISTER)
	public String initCreationForm(Map<String, Object> model) {
		UserDTO user = new UserDTO();
		model.put(CommonAttribute.USER, user);
		return CommonView.USER_REGISTRATION;
	}

	@PostMapping(CommonEndPoint.REGISTER)
	public String processCreationForm(@ModelAttribute(CommonAttribute.USER) @Valid UserDTO user, BindingResult result) {
		if (result.hasErrors()) {
			sendErrorMessage(CommonWebSocket.USER_CREATION_ERROR);
			return CommonView.USER_REGISTRATION;
		}

		if (userService.existByEmail(user.getEmail())) {
			result.rejectValue("email", "5", "Email already exist !");
			sendErrorMessage(CommonWebSocket.USER_CREATION_ERROR);
			return CommonView.USER_REGISTRATION;
		}

		// set default role
		user.addRole("ROLE_USER");

		// encode password because we get clear password
		user.encode(user.getPassword());
		user = this.userService.save(user);

		CredentialDTO credential = new CredentialDTO(user);
		credential = credentialService.save(credential);

		sendSuccessMessage(CommonWebSocket.USER_CREATED);

		// send confirmation mail
		MessageDTO message = new MessageDTO(user.getFirstName(), user.getLastName(), "admin@petclinic.com",
				user.getEmail(), "New connexion",
				"Your attempt to create new account. To confirm your account, please click here : ",
				"http://localhost:8080/confirm-account?token=" + credential.getToken());

		// TODO
		// emailService.sendMailAsynch(message, Locale.getDefault());

		log.info(message.toString());

		return CommonView.HOME + user.getId();
	}

	@GetMapping(CommonEndPoint.LOGIN)
	public String initLoginForm(Map<String, Object> model) {

		if (model.containsKey(CommonAttribute.USER)) {
			return CommonView.HOME;
		}
		else {
			UserDTO user = new UserDTO();
			model.put(CommonAttribute.USER, user);
		}

		Iterable<ClientRegistration> clientRegistrations = null;
		ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository).as(Iterable.class);

		if (type != ResolvableType.NONE && ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
			clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
		}

		clientRegistrations.forEach(registration -> oauth2AuthenticationUrls.put(registration.getClientName(),
				"oauth2/authorization/" + registration.getRegistrationId()));
		model.put("urls", oauth2AuthenticationUrls);

		return CommonView.USER_LOGIN;
	}

	@GetMapping(CommonEndPoint.LOGIN_SUCCESS)
	public String postLogin(Model model, Authentication authentication) {
		UserDTO user = (UserDTO) authentication.getPrincipal();

		model.addAttribute(CommonAttribute.USER, user);
		String message = String.format(CommonWebSocket.USER_LOGGED_IN, user.getFirstName(), user.getLastName());
		sendSuccessMessage(message);

		return CommonView.HOME;
	}

	@GetMapping(CommonEndPoint.OAUTH2_SUCCESS)
	public String postLogin(Model model, OAuth2AuthenticationToken authentication) {
		String firstName = authentication.getPrincipal().getAttribute("given_name");
		String lastName = authentication.getPrincipal().getAttribute("family_name");

		CredentialDTO credential = credentialService.findByAuthentication(authentication);

		if (credential.isNew()) {

			// first time authentification with this provider
			credential = credentialService.saveNew(authentication);
			String email = credential.getEmail();

			UserDTO user = userService.findByEmail(email);

			if (user == null) {
				user = new UserDTO();
				user.setEmail(email);
				user.encode(credential.getPassword());
				user.setFirstName(firstName);
				user.setLastName(lastName);
				user.setEnabled(true);
				user.addRole("ROLE_USER");
				user = userService.save(user);
			}

			// send confirmation mail
			MessageDTO message = new MessageDTO(firstName, lastName, "admin@petclinic.com", credential.getEmail(),
					"New connexion from " + credential.getProvider(),
					"Your attempt to connect from " + credential.getProvider()
							+ " To confirm this connection, please click the link below : ",
					"http://localhost:8080/confirm-account?token=" + credential.getToken());

			log.info(message.toString());
			emailService.sendMailAsynch(message, Locale.getDefault());

			// disconnect
			authentication.eraseCredentials();
			SecurityContextHolder.clearContext();

		}
		else if (credential.isVerified()) {
			securityService.autoLogin(credential.getEmail(), credential.getPassword());
			String message = String.format(CommonWebSocket.USER_LOGGED_IN, firstName, lastName);
			sendSuccessMessage(message);
		}

		return CommonView.HOME;
	}

	@RequestMapping(value = CommonEndPoint.CONFIRM_ACCOUNT, method = { RequestMethod.GET, RequestMethod.POST })
	public String confirmUserAccount(@RequestParam(CommonAttribute.TOKEN) String token, Model model) {
		CredentialDTO credential = credentialService.findByToken(token);

		if (!credential.isNew() && credential.isNotExpired()) {
			credential.setVerified(true);
			credential.setToken("");
			credential.setExpiration(null);
			credential = credentialService.save(credential);

			// find corresponding user
			UserDTO user = userService.findByEmail(credential.getEmail());

			securityService.autoLogin(credential.getEmail(), credential.getPassword());
			model.addAttribute(CommonAttribute.USER, user);
			return CommonView.USER_UPDATE;
		}

		return CommonView.HOME;
	}

	@GetMapping(CommonEndPoint.LOGOUT)
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			new SecurityContextLogoutHandler().logout(request, response, authentication);
		}

		sendSuccessMessage(CommonWebSocket.USER_LOGGED_OUT);
		return CommonView.USER_LOGIN_R;
	}

	@GetMapping(CommonEndPoint.LOGOUT_SUCCESS)
	public String postLogout(Model model) {
		sendSuccessMessage(CommonWebSocket.USER_LOGGED_OUT);
		return CommonView.HOME;
	}

	@GetMapping(CommonEndPoint.USERS_EDIT)
	public String initUpdateOwnerForm(Model model) {
		try {
			UserDTO user = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			user = userService.findByEmail(user.getEmail());
			model.addAttribute(CommonAttribute.USER, user);
			model.addAttribute(CommonAttribute.USER_ID, user.getId());
			return CommonView.USER_UPDATE;
		}
		catch (Exception exception) {
			// user don't have profile
		}

		return CommonView.HOME;
	}

	@PostMapping(CommonEndPoint.USERS_EDIT)
	public String processUpdateOwnerForm(@ModelAttribute(CommonAttribute.USER) @Valid UserDTO user,
			BindingResult result, Model model) {
		if (result.hasErrors()) {
			sendErrorMessage(CommonWebSocket.USER_UPDATED_ERROR);
			return CommonView.USER_UPDATE;
		}

		if (!user.getPassword().equals(user.getMatchingPassword())) {
			sendErrorMessage(CommonWebSocket.USER_UPDATED_ERROR);
			return CommonView.USER_UPDATE;
		}

		else {
			user = userService.save(user);

			model.addAttribute(CommonAttribute.USER, user);
			sendSuccessMessage(CommonWebSocket.USER_UPDATED);
			return CommonView.HOME;
		}
	}

	@GetMapping(CommonEndPoint.USERS_ID)
	public ModelAndView showOwner(@PathVariable("userId") Integer userId) {
		ModelAndView modelAndView = new ModelAndView(CommonView.USER_DETAILS);
		UserDTO user = this.userService.findById(userId);

		modelAndView.addObject(CommonAttribute.USER, user);
		return modelAndView;
	}

	@GetMapping("/user/{userId}/edit/password")
	public String editPassword(@PathVariable("userId") Integer userId, Model model) {
		try {
			UserDTO operator = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			UserDTO user = userService.findById(userId);

			if (user.equals(operator) || operator.getRoles().contains("ROLE_ADMIN")) {
				model.addAttribute(CommonAttribute.USER, user);
				model.addAttribute(CommonAttribute.USER_ID, user.getId());
				return CommonView.USER_CHANGE_PASSWORD;
			}
		}
		catch (Exception exception) {
			// user don't have profile
		}

		return CommonView.HOME;
	}

	@PostMapping("/user/{userId}/edit/password")
	public String updatePassword(@ModelAttribute(CommonAttribute.USER) @Valid UserDTO user, BindingResult bindingResult,
			@PathVariable(CommonAttribute.USER_ID) Integer userId, @Param("oldPassword") String oldPassword,
			@Param("newPassword") String newPassword, @Param("newMatchingPassword") String newMatchingPassword,
			Model model) {

		// verify the matching with old password
		if (!user.matches(oldPassword)) {
			bindingResult.rejectValue("password", "6", "Bad password !");
			model.addAttribute(CommonAttribute.USER, user);
			return CommonView.USER_CHANGE_PASSWORD;
		}

		// verify matching between two password
		if (!newPassword.equals(newMatchingPassword)) {
			bindingResult.rejectValue("password", "7", "Bad matching password !");
			model.addAttribute(CommonAttribute.USER, user);
			return CommonView.USER_CHANGE_PASSWORD;
		}

		try {
			UserDTO operator = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			if (user.equals(operator) || operator.getRoles().contains("ROLE_ADMIN")) {
				// encode password
				user.encode(newPassword);
				user = userService.save(user);

				model.addAttribute(CommonAttribute.USER, user);
				return CommonView.USER_UPDATE_R;
			}
		}
		catch (NullPointerException exception) {
			log.error(exception.getMessage());
		}

		return CommonView.HOME;
	}

}
