
package org.springframework.cheapy.web;

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.cheapy.model.Client;
import org.springframework.cheapy.model.FoodOffer;
import org.springframework.cheapy.service.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ClientController {

	private static final String VIEWS_CREATE_OR_UPDATE_CLIENT = "clients/createOrUpdateClientForm";

	private final ClientService clientService;

	public ClientController(final ClientService clientService) {
		this.clientService = clientService;
	}



	private boolean checkDates(final FoodOffer foodOffer) {
		boolean res = false;
		if(foodOffer.getEnd()==null || foodOffer.getStart()==null || foodOffer.getEnd().isAfter(foodOffer.getStart())) {
			res = true;
		}
		return res;
	}




	@GetMapping("/clients/show")
	public String processShowForm(Map<String, Object> model) {

		Client client = this.clientService.getCurrentClient();
		

			model.put("client", client);
			return "clients/clientShow";
		


	}

	@GetMapping(value = "/clients/edit")
	public String updateClient( final ModelMap model, HttpServletRequest request) {

		Client client = this.clientService.getCurrentClient();

		model.addAttribute("client", client);

		return ClientController.VIEWS_CREATE_OR_UPDATE_CLIENT;
	}

	@PostMapping(value = "/clients/edit")
	public String updateClient(@Valid final Client clientEdit, final BindingResult result,
			final ModelMap model, HttpServletRequest request) {


		Client clienteSesion = this.clientService.getCurrentClient();

	

			if (result.hasErrors()) {
				model.addAttribute("client", clientEdit);
				return ClientController.VIEWS_CREATE_OR_UPDATE_CLIENT;
			}
			
			BeanUtils.copyProperties(clienteSesion, clientEdit, "name", "email", "address","init", "finish","telephone", "description","food","usuar");
			clientEdit.getUsuar().setUsername(clienteSesion.getUsuar().getUsername());
			clientEdit.getUsuar().setEnabled(true);
			this.clientService.saveClient(clientEdit);
			return "redirect:/clients/show";
		
	}

	@GetMapping(value = "/clients/disable")
	public String disableClient(final ModelMap model) {

		Client client = this.clientService.getCurrentClient();
		model.put("client", client);
		return "/clients/clientDisable";
	}

	@PostMapping(value = "/clients/disable")
	public String disableClientForm(final ModelMap model,  HttpServletRequest request) {


		Client client = this.clientService.getCurrentClient();

		client.getUsuar().setEnabled(false);

		this.clientService.saveClient(client);
		
	try {
		request.logout();
	} catch (ServletException e) {

	}
		return "redirect:/login";

	}
}
