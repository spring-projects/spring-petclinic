package org.springframework.cheapy.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.cheapy.model.SpeedOffer;
import org.springframework.cheapy.model.StatusOffer;
import org.springframework.cheapy.model.Client;
import org.springframework.cheapy.service.ClientService;
import org.springframework.cheapy.service.SpeedOfferService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SpeedOfferController {

	private static final String VIEWS_SPEED_OFFER_CREATE_OR_UPDATE_FORM = "speedOffers/createOrUpdateSpeedOfferForm";

	private final SpeedOfferService speedOfferService;
	private final ClientService clientService;

	public SpeedOfferController(final SpeedOfferService speedOfferService, final ClientService clientService) {
		this.speedOfferService = speedOfferService;
		this.clientService = clientService;
	}
	
	private boolean checkIdentity(final int speedOfferId) {
		boolean res = false;
		Client client = this.clientService.getCurrentClient();
		SpeedOffer speedOffer = this.speedOfferService.findSpeedOfferById(speedOfferId);
		Client clientOffer = speedOffer.getClient();
		if (client.equals(clientOffer)) {
			res = true;
		}
		return res;
	}

	@GetMapping("/speedOffers/new")
	public String initCreationForm(Map<String, Object> model) {
		SpeedOffer speedOffer = new SpeedOffer();
		model.put("speedOffer", speedOffer);
		return VIEWS_SPEED_OFFER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/speedOffers/new")
	public String processCreationForm(@Valid SpeedOffer speedOffer, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_SPEED_OFFER_CREATE_OR_UPDATE_FORM;
		} else {
			Client client = this.clientService.getCurrentClient();
			speedOffer.setClient(client);
			speedOffer.setStatus(StatusOffer.hidden);
			this.speedOfferService.saveSpeedOffer(speedOffer);
			return "redirect:/speedOffers/" + speedOffer.getId();
		}
	}

	@GetMapping(value = "/speedOffers/{speedOfferId}/activate")
	public String activateSpeedOffer(@PathVariable("speedOfferId") final int speedOfferId, ModelMap modelMap) {
		SpeedOffer speedOffer = this.speedOfferService.findSpeedOfferById(speedOfferId);
		Client client = this.clientService.getCurrentClient();
		if (speedOffer.getClient().equals(client)) {
			speedOffer.setStatus(StatusOffer.active);
			speedOffer.setCode("SP-" + speedOfferId);
			this.speedOfferService.saveSpeedOffer(speedOffer);
		} else {
			modelMap.addAttribute("message", "You don't have access to this speed offer");
		}
		return "redirect:/speedOffers/";
	}

	@GetMapping("/offers/speed/{speedOfferId}")
	public String processShowForm(@PathVariable("speedOfferId") int speedOfferId, Map<String, Object> model) {

		SpeedOffer speedOffer = this.speedOfferService.findSpeedOfferById(speedOfferId);
		model.put("speedOffer", speedOffer);
		
		//Se añade formateador de fecha al modelo	
		model.put("localDateTimeFormat", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
		return "speedOffers/speedOffersShow";
	}

	@GetMapping(value = "/offers/speed/{speedOfferId}/edit")
	public String updateSpeedOffer(@PathVariable("speedOfferId") final int speedOfferId, final ModelMap model) {
		
		if (!this.checkIdentity(speedOfferId)) {
			return "error";
		}

		SpeedOffer speedOffer = this.speedOfferService.findSpeedOfferById(speedOfferId);
		model.addAttribute("speedOffer", speedOffer);
		return SpeedOfferController.VIEWS_SPEED_OFFER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/offers/speed/{speedOfferId}/edit")
	public String updateSpeedOffer(@Valid final SpeedOffer speedOfferEdit, final BindingResult result, final ModelMap model) {
		
		if (!this.checkIdentity(speedOfferEdit.getId())) {
			return "error";
		}

		if (result.hasErrors()) {
			model.addAttribute("speedOffer", speedOfferEdit);
			return SpeedOfferController.VIEWS_SPEED_OFFER_CREATE_OR_UPDATE_FORM;

		} else {
			this.speedOfferService.saveSpeedOffer(speedOfferEdit);
			return "redirect:/offers/speed/" + speedOfferEdit.getId();
		}

	}

	@GetMapping(value = "/offers/speed/{speedOfferId}/disable")
	public String disableSpeedOffer(@PathVariable("speedOfferId") final int speedOfferId, final ModelMap model) {
		
		if (!this.checkIdentity(speedOfferId)) {
			return "error";
		}

		SpeedOffer speedOffer = this.speedOfferService.findSpeedOfferById(speedOfferId);
		model.put("speedOffer", speedOffer);
		return "speedOffers/speedOffersDisable";
	}

	@PostMapping(value = "/offers/speed/{speedOfferId}/disable")
	public String disableSpeedOfferForm(@PathVariable("speedOfferId") final int speedOfferId, final ModelMap model) {
		
		if (!this.checkIdentity(speedOfferId)) {
			return "error";
		}

		SpeedOffer speedOffer = this.speedOfferService.findSpeedOfferById(speedOfferId);

		speedOffer.setStatus(StatusOffer.inactive);

		this.speedOfferService.saveSpeedOffer(speedOffer);

		return "redirect:/offers";

	}
}
