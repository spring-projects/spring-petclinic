package org.springframework.cheapy.web;

import java.time.format.DateTimeFormatter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
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

	private boolean checkIdentity(final int timeOfferId) {
		boolean res = false;
		Client client = this.clientService.getCurrentClient();
		TimeOffer timeOffer = this.timeOfferService.findTimeOfferById(timeOfferId);
		Client clientOffer = timeOffer.getClient();
		if (client.equals(clientOffer)) {
			res = true;
		}
		return res;
	}

	private boolean checkOffer(final TimeOffer session, final TimeOffer offer) {
		boolean res = false;
		if (session.getId() == offer.getId() && session.getStatus() == offer.getStatus()
				&& (session.getCode() == null ? offer.getCode() == "" : session.getCode().equals(offer.getCode())) && !(session.getStatus().equals(StatusOffer.inactive))) {
			res = true;
		}
		return res;
	}
	
	private boolean checkDates(final TimeOffer timeOffer) {
		boolean res = false;
		if(timeOffer.getEnd()==null || timeOffer.getStart()==null || timeOffer.getEnd().isAfter(timeOffer.getStart())) {
			res = true;
		}
		return res;
	}
	
	private boolean checkTimes(final TimeOffer timeOffer) {
		boolean res = false;
		if(timeOffer.getFinish()==null || timeOffer.getInit()==null || timeOffer.getFinish().isAfter(timeOffer.getInit())) {
			res = true;
		}
		return res;
	}

	@GetMapping("/offers/time/new")
	public String initCreationForm(Map<String, Object> model) {
		TimeOffer timeOffer = new TimeOffer();
		model.put("timeOffer", timeOffer);
		return VIEWS_TIME_OFFER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/offers/time/new")
	public String processCreationForm(@Valid TimeOffer timeOffer, BindingResult result) {
		
			if(!this.checkDates(timeOffer)) {
				result.rejectValue("end","" ,"La fecha de fin debe ser posterior a la fecha de inicio");
				
			}
			
			if(!this.checkTimes(timeOffer)) {
				result.rejectValue("finish","" ,"La hora de fin debe ser posterior a la de inicio");
				
			}
			
			if (result.hasErrors()) {
				return VIEWS_TIME_OFFER_CREATE_OR_UPDATE_FORM;
			} 
			
			timeOffer.setStatus(StatusOffer.hidden);

			Client client = this.clientService.getCurrentClient();

			timeOffer.setClient(client);

			this.timeOfferService.saveTimeOffer(timeOffer);
			return "redirect:/offers/time/" + timeOffer.getId();
		
	}

	@GetMapping(value = "/offers/time/{timeOfferId}/activate")
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
		if(timeOffer.getStatus().equals(StatusOffer.active)) {
			model.put("timeOffer", timeOffer);
			model.put("localDateTimeFormat", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
			return "offers/time/timeOffersShow";
			
		} else if(timeOffer.getStatus().equals(StatusOffer.hidden)&&(this.checkIdentity(timeOfferId))) {
				model.put("timeOffer", timeOffer);
				model.put("localDateTimeFormat", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
				return "offers/time/timeOffersShow";
			
		}else {
			return "error";
		}
	}

	@GetMapping(value = "/offers/time/{timeOfferId}/edit")
	public String updateTimeOffer(@PathVariable("timeOfferId") final int timeOfferId, final ModelMap model,
			HttpServletRequest request) {

		if (!this.checkIdentity(timeOfferId)) {
			return "error";
		}
		TimeOffer timeOffer = this.timeOfferService.findTimeOfferById(timeOfferId);
		if (timeOffer.getStatus().equals(StatusOffer.inactive)) {
			return "error";
		}

		model.addAttribute("timeOffer", timeOffer);
		request.getSession().setAttribute("idTime", timeOfferId);
		return TimeOfferController.VIEWS_TIME_OFFER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/offers/time/{timeOfferId}/edit")
	public String updateTimeOffer(@Valid final TimeOffer timeOfferEdit, final BindingResult result,
			final ModelMap model, HttpServletRequest request) {

		if (!this.checkIdentity(timeOfferEdit.getId())) {
			return "error";
		}
		Integer id = (Integer) request.getSession().getAttribute("idTime");
		TimeOffer timeOffer = this.timeOfferService.findTimeOfferById(id);
		if (!this.checkOffer(timeOffer, timeOfferEdit)) {
			return "error";
		}

		 
			if(!this.checkDates(timeOfferEdit)) {
				result.rejectValue("end","" ,"La fecha de fin debe ser posterior a la fecha de inicio");
				
			}
			if(!this.checkTimes(timeOfferEdit)) {
				result.rejectValue("finish","" ,"La hora de fin debe ser posterior a la de inicio");
				
			}
			if (result.hasErrors()) {
				model.addAttribute("timeOffer", timeOfferEdit);
				return TimeOfferController.VIEWS_TIME_OFFER_CREATE_OR_UPDATE_FORM;

			}
			
			BeanUtils.copyProperties(this.timeOfferService.findTimeOfferById(timeOfferEdit.getId()), timeOfferEdit,
					"start", "end", "init", "finish", "discount");
			this.timeOfferService.saveTimeOffer(timeOfferEdit);
			return "redirect:/offers/time/" + timeOfferEdit.getId();
		

	}

	@GetMapping(value = "/offers/time/{timeOfferId}/disable")
	public String disableTimeOffer(@PathVariable("timeOfferId") final int timeOfferId, final ModelMap model) {

		if (!this.checkIdentity(timeOfferId)) {
			return "error";
		}

		TimeOffer timeOffer = this.timeOfferService.findTimeOfferById(timeOfferId);
		model.put("timeOffer", timeOffer);
		return "offers/time/timeOffersDisable";
	}

	@PostMapping(value = "/offers/time/{timeOfferId}/disable")
	public String disableTimeOfferForm(@PathVariable("timeOfferId") final int timeOfferId, final ModelMap model) {

		if (!this.checkIdentity(timeOfferId)) {
			return "error";
		}

		TimeOffer timeOffer = this.timeOfferService.findTimeOfferById(timeOfferId);

		timeOffer.setStatus(StatusOffer.inactive);

		this.timeOfferService.saveTimeOffer(timeOffer);

		return "redirect:/myOffers";

	}

}
