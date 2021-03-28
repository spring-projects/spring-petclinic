package org.springframework.cheapy.web;


import java.time.format.DateTimeFormatter;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.cheapy.model.Client;
import org.springframework.cheapy.model.StatusOffer;
import org.springframework.cheapy.model.TimeOffer;
import org.springframework.cheapy.service.ClientService;
import org.springframework.cheapy.service.TimeOfferService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TimeOfferController {


	private static final String VIEWS_TIME_OFFER_CREATE_OR_UPDATE_FORM = "offers/time/createOrUpdateTimeOfferForm";
	private final TimeOfferService timeOfferService;
	private final ClientService clientService;

	public TimeOfferController(final TimeOfferService timeOfferService, ClientService clientService) {
		this.timeOfferService = timeOfferService;
		this.clientService = clientService;
	}

	@GetMapping("/offers/time/new")
	public String initCreationForm(Map<String, Object> model) {
		TimeOffer timeOffer = new TimeOffer();
		model.put("timeOffer", timeOffer);
		return VIEWS_TIME_OFFER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/offers/time/new")
	public String processCreationForm(@Valid TimeOffer timeOffer, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_TIME_OFFER_CREATE_OR_UPDATE_FORM;
		} else {
			timeOffer.setStatus(StatusOffer.hidden);

			Client client = this.clientService.getCurrentClient();

			timeOffer.setClient(client);

			this.timeOfferService.saveTimeOffer(timeOffer);
			return "redirect:/offers/time/" + timeOffer.getId();
		}
	}

	@GetMapping(value ="/offers/time/{timeOfferId}/activate")
	public String activateTimeOffer(@PathVariable("timeOfferId") final int timeOfferId, final ModelMap modelMap) {
		Client client = this.clientService.getCurrentClient();
		TimeOffer timeOffer = this.timeOfferService.findTimeOfferById(timeOfferId);
		if (timeOffer.getClient().equals(client)) {
			timeOffer.setStatus(StatusOffer.active);
			timeOffer.setCode("TI-" + timeOfferId);
			this.timeOfferService.saveTimeOffer(timeOffer);

			
		} else {
			modelMap.addAttribute("message", "You don't have access to this time offer");
		}
		return "redirect:/offers/time/" + timeOffer.getId();


	}

	@GetMapping("/offers/time/{timeOfferId}")
	public String processShowForm(@PathVariable("timeOfferId") int timeOfferId, Map<String, Object> model) {

		TimeOffer timeOffer = this.timeOfferService.findTimeOfferById(timeOfferId);

		model.put("timeOffer", timeOffer);

		model.put("localDateTimeFormat", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
		
		return "offers/time/timeOffersShow";

	}

	@GetMapping(value = "/offers/time/{timeOfferId}/edit")
	public String updateTimeOffer(@PathVariable("timeOfferId") final int timeOfferId, final ModelMap model) {
		

		TimeOffer timeOffer = this.timeOfferService.findTimeOfferById(timeOfferId);
		model.addAttribute("timeOffer", timeOffer);
		return TimeOfferController.VIEWS_TIME_OFFER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/offers/time/{timeOfferId}/edit")
	public String updateTimeOffer(@Valid final TimeOffer timeOfferEdit, final BindingResult result, final ModelMap model) {
		

		if (result.hasErrors()) {
			model.addAttribute("timeOffer", timeOfferEdit);
			return TimeOfferController.VIEWS_TIME_OFFER_CREATE_OR_UPDATE_FORM;

		} else {
			this.timeOfferService.saveTimeOffer(timeOfferEdit);
			return "redirect:/offers/time/" + timeOfferEdit.getId();
		}

	}

	@GetMapping(value = "/offers/time/{timeOfferId}/disable")
	public String disableTimeOffer(@PathVariable("timeOfferId") final int timeOfferId, final ModelMap model) {


		TimeOffer timeOffer = this.timeOfferService.findTimeOfferById(timeOfferId);
		model.put("timeOffer", timeOffer);
		return "timeOffers/timeOffersDisable";
	}

	@PostMapping(value = "/offers/time/{timeOfferId}/disable")
	public String disableTimeOfferForm(@PathVariable("timeOfferId") final int timeOfferId, final ModelMap model) {
		

		TimeOffer timeOffer = this.timeOfferService.findTimeOfferById(timeOfferId);

		timeOffer.setStatus(StatusOffer.inactive);

		this.timeOfferService.saveTimeOffer(timeOffer);

		return "redirect:/offers";


	}

}
