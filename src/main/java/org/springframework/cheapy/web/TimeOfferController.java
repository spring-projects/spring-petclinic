package org.springframework.cheapy.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.cheapy.model.Client;
import org.springframework.cheapy.model.TimeOffer;
import org.springframework.cheapy.model.StatusOffer;
import org.springframework.cheapy.service.ClientService;
import org.springframework.cheapy.service.TimeOfferService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class TimeOfferController {

	private static final String VIEWS_NU_OFFER_CREATE_OR_UPDATE_FORM = "timeOffers/createOrUpdateTimeOfferForm";

	private final TimeOfferService timeOfferService;
	private final ClientService clientService;



	public TimeOfferController(final TimeOfferService timeOfferService,ClientService clientService) {
		this.timeOfferService = timeOfferService;
		this.clientService = clientService;
		
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping("/timeOffers/new")
	public String initCreationForm(Map<String, Object> model) {
		TimeOffer timeOffer = new TimeOffer();
		model.put("timeOffer", timeOffer);
		return VIEWS_NU_OFFER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/timeOffers/new")
	public String processCreationForm(@Valid TimeOffer timeOffer, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_NU_OFFER_CREATE_OR_UPDATE_FORM;
		}
		else {
			timeOffer.setType(StatusOffer.hidden);
		
			Client client = this.clientService.getCurrentClient();
			
			timeOffer.setClient(client);
			
			
			this.timeOfferService.saveTimeOffer(timeOffer);
			return "redirect:/TimeOffers/" + timeOffer.getId();
		}
	}
	@GetMapping(value ="/timeOffers/{timeOfferId}/activate")
	public String activateTimeOffer(@PathVariable("timeOfferId") final int timeOfferId, final ModelMap modelMap) {
		Client client = this.clientService.getCurrentClient();
		TimeOffer timeOffer=this.timeOfferService.findTimeOfferById(timeOfferId);
		if(timeOffer.getClient().equals(client)) {
			timeOffer.setType(StatusOffer.active);
			timeOffer.setCode("TI-"+timeOfferId);
			this.timeOfferService.saveTimeOffer(timeOffer);
			
			return "redirect:/timeOffers/" + timeOffer.getId();	
		} else {
		         modelMap.addAttribute("message", "You don't have access to this time offer");
		        }
		        return "redirect:/timeOffers/";
		

	}
	
}
