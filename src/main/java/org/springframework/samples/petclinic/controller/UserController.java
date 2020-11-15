package org.springframework.samples.petclinic.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.common.CommonAttribute;
import org.springframework.samples.petclinic.common.CommonEndPoint;
import org.springframework.samples.petclinic.common.CommonView;
import org.springframework.samples.petclinic.common.CommonWebSocket;
import org.springframework.samples.petclinic.controller.common.WebSocketSender;
import org.springframework.samples.petclinic.dto.UserDTO;
import org.springframework.samples.petclinic.service.RoleService;
import org.springframework.samples.petclinic.service.SecurityServiceImpl;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Paul-Emmanuel DOS SANTOS FACAO
 */
@Slf4j
@Controller
public class UserController extends WebSocketSender {

	private static final String ROLE_ADMIN = "ADMIN";

	private static final String ROLE_STAFF = "STAFF";

	private static final String ROLE_USER = "USER";

	private static final String authorizationRequestBaseUri = "oauth2/authorization";

	private final UserService userService;

	private final RoleService roleService;

	private final SecurityServiceImpl securityService;

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public UserController(UserService userService, RoleService roleService, SecurityServiceImpl securityService,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userService = userService;
		this.roleService = roleService;
		this.securityService = securityService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@InitBinder("user")
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields(CommonAttribute.USER_ID);
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
		return CommonView.USER_CREATE_OR_UPDATE;
	}

	@PostMapping(CommonEndPoint.REGISTER)
	public String processCreationForm(@ModelAttribute(CommonAttribute.USER) @Valid UserDTO user, BindingResult result) {
		if (result.hasErrors()) {
			sendErrorMessage(CommonWebSocket.USER_CREATION_ERROR);
			return CommonView.USER_CREATE_OR_UPDATE;
		}

		try {
			userService.findByEmail(user.getEmail());
			result.rejectValue("email", "5", "Email already exist !");
			sendErrorMessage(CommonWebSocket.USER_CREATION_ERROR);
			return CommonView.USER_CREATE_OR_UPDATE;
		}
		catch (Exception ex) {
		}

		// set default role
		user.addRole(roleService.findByName(ROLE_USER));

		// encode password because we get clear password
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setMatchingPassword(user.getPassword());

		user = this.userService.save(user);
		sendSuccessMessage(CommonWebSocket.USER_CREATED);
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
				authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
		model.put("urls", oauth2AuthenticationUrls);

		return CommonView.USER_LOGIN;
	}

	@GetMapping(CommonEndPoint.LOGIN_SUCCESS)
	public String postLogin(Model model, Authentication authentication) {
		UserDTO user = userService.findByEmail(authentication.getName());

		model.addAttribute(CommonAttribute.USER, user);
		String message = String.format(CommonWebSocket.USER_LOGGED_IN, user.getFirstName(), user.getLastName());
		sendSuccessMessage(message );

		return CommonView.HOME;
	}

	@GetMapping(CommonEndPoint.OAUTH2_SUCCESS)
	public String postLogin(Model model, OAuth2AuthenticationToken authentication) {

		OAuth2AuthorizedClient client = authorizedClientService	.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());

		String userInfoEndpointUri = client.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();

		UserDTO user = userService.findByEmail(authentication.getName());

		if( user!=null) {
			model.addAttribute(CommonAttribute.USER, user);

			String message = String.format(CommonWebSocket.USER_LOGGED_IN, user.getFirstName(), user.getLastName());
			sendSuccessMessage(message);
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
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (!authentication.getName().equals("anonymousUser")) {

			UserDTO user = userService.findByEmail(authentication.getName());
			model.addAttribute(CommonAttribute.USER, user);
			return CommonView.USER_CREATE_OR_UPDATE;
		}

		return CommonView.HOME;
	}

	@PostMapping(CommonEndPoint.USERS_ID_EDIT)
	public String processUpdateOwnerForm(@ModelAttribute(CommonAttribute.USER) @Valid UserDTO user,
			BindingResult result, @PathVariable("userId") int userId) {
		if (result.hasErrors()) {
			sendErrorMessage(CommonWebSocket.USER_UPDATED_ERROR);
			return CommonView.USER_CREATE_OR_UPDATE;
		}
		else {
			user.setId(userId);
			this.userService.save(user);

			sendSuccessMessage(CommonWebSocket.USER_UPDATED);
			return CommonView.USER_USERS_ID_R;
		}
	}

	@GetMapping(CommonEndPoint.USERS_ID)
	public ModelAndView showOwner(@PathVariable("userId") int userId) {
		ModelAndView modelAndView = new ModelAndView(CommonView.USER_DETAILS);
		UserDTO user = this.userService.findById(userId);

		modelAndView.addObject(CommonAttribute.USER, user);
		return modelAndView;
	}

}
