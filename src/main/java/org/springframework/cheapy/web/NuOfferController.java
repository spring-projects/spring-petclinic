package org.springframework.cheapy.web;

import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.cheapy.model.Client;
import org.springframework.cheapy.model.NuOffer;
import org.springframework.cheapy.model.StatusOffer;
import org.springframework.cheapy.service.ClientService;
import org.springframework.cheapy.service.NuOfferService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class NuOfferController {

	private static final String VIEWS_NU_OFFER_CREATE_OR_UPDATE_FORM = "offers/nu/createOrUpdateNuOfferForm";

	private final NuOfferService nuOfferService;
	private final ClientService clientService;

	public NuOfferController(final NuOfferService nuOfferService, final ClientService clientService) {
		this.nuOfferService = nuOfferService;
		this.clientService = clientService;
	}
	
	private boolean checkIdentity(final int nuOfferId) {
		boolean res = false;
		Client client = this.clientService.getCurrentClient();
		NuOffer nuOffer = this.nuOfferService.findNuOfferById(nuOfferId);
		Client clientOffer = nuOffer.getClient();
		if (client.equals(clientOffer)) {
			res = true;
		}
		return res;
	}

	private boolean checkOffer(final NuOffer session, final NuOffer offer) {
		boolean res = false;
		if (session.getId() == offer.getId() && session.getStatus() == offer.getStatus()
				&& (session.getCode() == null ? offer.getCode() == "" : session.getCode().equals(offer.getCode())) && !(session.getStatus().equals(StatusOffer.inactive))) {
			res = true;
		}
		return res;
	}
	
	private boolean checkDates(final NuOffer nuOffer) {
		boolean res = false;
		if(nuOffer.getEnd()==null || nuOffer.getStart()==null || nuOffer.getEnd().isAfter(nuOffer.getStart())) {
			res = true;
		}
		return res;
	}
	
	private boolean checkConditions(final NuOffer nuOffer) {
		boolean res = false;
		if(nuOffer.getGold()==null || nuOffer.getSilver()==null || nuOffer.getBronze()==null) {
			
		}else if(nuOffer.getGold() >= nuOffer.getSilver() && nuOffer.getSilver() >= nuOffer.getBronze()) {
			res = true;
		}
		return res;
	}
	
	private boolean checkDiscounts(final NuOffer NuOffer) {
		boolean res = false;
		if(NuOffer.getDiscountGold()==null || NuOffer.getDiscountSilver()==null || NuOffer.getDiscountBronze()==null) {	
		}else if(NuOffer.getDiscountGold() >= NuOffer.getDiscountSilver() && NuOffer.getDiscountSilver() >= NuOffer.getDiscountBronze()) {
			res = true;
		}
		return res;
	}
	
	@GetMapping("/offers/nuOfferList")
	public String processFindForm(Map<String, Object> model) {
		List<NuOffer> foodOfferLs=this.nuOfferService.findActiveNuOffer();
		model.put("nuOfferLs", foodOfferLs);
		model.put("localDateTimeFormat", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
		return "offers/nu/nuOffersList";

	}

	@GetMapping("/offers/nu/new")
	public String initCreationForm(Map<String, Object> model) {
		NuOffer nuOffer = new NuOffer();
		model.put("nuOffer", nuOffer);
		return VIEWS_NU_OFFER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/offers/nu/new")
	public String processCreationForm(@Valid NuOffer nuOffer, BindingResult result) {
		
			if(!this.checkDates(nuOffer)) {
				result.rejectValue("end","" ,"La fecha de fin debe ser posterior a la fecha de inicio");
				
			}
			if(!this.checkConditions(nuOffer)) {
				result.rejectValue("gold","" ,"Oro debe ser mayor o igual que plata, y plata mayor o igual que bronce");
				
			}
			if(!this.checkDiscounts(nuOffer)) {
				result.rejectValue("discountGold","" ,"El descuento de Oro debe ser mayor o igual que el de plata, y el de plata mayor o igual que el de bronce");
				
			}
			
			if (result.hasErrors()) {
				return VIEWS_NU_OFFER_CREATE_OR_UPDATE_FORM;
			} 
			nuOffer.setStatus(StatusOffer.hidden);

			Client client = this.clientService.getCurrentClient();

			nuOffer.setClient(client);

			this.nuOfferService.saveNuOffer(nuOffer);
			return "redirect:/offers/nu/" + nuOffer.getId();
		
	}

	@GetMapping(value = "/offers/nu/{nuOfferId}/activate")
	public String activateNuOffer(@PathVariable("nuOfferId") final int nuOfferId, final ModelMap modelMap) {
		Client client = this.clientService.getCurrentClient();
		NuOffer nuOffer = this.nuOfferService.findNuOfferById(nuOfferId);
		if (nuOffer.getClient().equals(client)) {
			nuOffer.setStatus(StatusOffer.active);
			nuOffer.setCode("NU-" + nuOfferId);
			this.nuOfferService.saveNuOffer(nuOffer);

		} else {
			modelMap.addAttribute("message", "You don't have access to this number offer");
		}
		return "redirect:/offers/nu/" + nuOffer.getId();

	}

	@GetMapping("/offers/nu/{nuOfferId}")
	public String processShowForm(@PathVariable("nuOfferId") int nuOfferId, Map<String, Object> model) {
		NuOffer nuOffer = this.nuOfferService.findNuOfferById(nuOfferId);
		if(nuOffer.getStatus().equals(StatusOffer.active)) {
			model.put("nuOffer", nuOffer);
			model.put("localDateTimeFormat", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
			return "offers/nu/nuOffersShow";
		}else if(nuOffer.getStatus().equals(StatusOffer.hidden)&&(this.checkIdentity(nuOfferId))) {
				model.put("nuOffer", nuOffer);
				model.put("localDateTimeFormat", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
				return "offers/nu/nuOffersShow";
			
		}else {
			return "error";
		}

	}

	@GetMapping(value = "/offers/nu/{nuOfferId}/edit")
	public String updateNuOffer(@PathVariable("nuOfferId") final int nuOfferId, final ModelMap model,
			HttpServletRequest request) {

		if (!this.checkIdentity(nuOfferId)) {
			return "error";
		}
		NuOffer nuOffer = this.nuOfferService.findNuOfferById(nuOfferId);
		if (nuOffer.getStatus().equals(StatusOffer.inactive)) {
			return "error";
		}
		model.addAttribute("nuOffer", nuOffer);
		request.getSession().setAttribute("idNu", nuOfferId);
		return NuOfferController.VIEWS_NU_OFFER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/offers/nu/{nuOfferId}/edit")
	public String updateNuOffer(@Valid final NuOffer nuOfferEdit, final BindingResult result, final ModelMap model,
			HttpServletRequest request) {

		if (!this.checkIdentity(nuOfferEdit.getId())) {
			return "error";
		}
		Integer id = (Integer) request.getSession().getAttribute("idNu");
		NuOffer nuOffer = this.nuOfferService.findNuOfferById(id);
		if (!this.checkOffer(nuOffer, nuOfferEdit)) {
			return "error";
		}

			if(!this.checkDates(nuOfferEdit)) {
				result.rejectValue("end","" ,"La fecha de fin debe ser posterior a la fecha de inicio");
				
			}
			if(!this.checkConditions(nuOfferEdit)) {
				result.rejectValue("gold","" ,"Oro debe ser mayor o igual que plata, y plata mayor o igual que bronce");
				
			}
			if(!this.checkDiscounts(nuOfferEdit)) {
				result.rejectValue("discountGold","" ,"El descuento de Oro debe ser mayor o igual que el de plata, y el de plata mayor o igual que el de bronce");
				
			}
			

			if (result.hasErrors()) {
				model.addAttribute("nuOffer", nuOfferEdit);
				return NuOfferController.VIEWS_NU_OFFER_CREATE_OR_UPDATE_FORM;

			} 
			BeanUtils.copyProperties(this.nuOfferService.findNuOfferById(nuOfferEdit.getId()), nuOfferEdit, "start",
					"end", "gold", "discount_gold", "silver", "discount_silver", "bronze", "discount_bronze");
			this.nuOfferService.saveNuOffer(nuOfferEdit);
			return "redirect:/offers/nu/" + nuOfferEdit.getId();
		
	}

	@GetMapping(value = "/offers/nu/{nuOfferId}/disable")
	public String disableNuOffer(@PathVariable("nuOfferId") final int nuOfferId, final Principal principal,
			final ModelMap model) {

		if (!this.checkIdentity(nuOfferId)) {
			return "error";
		}

		NuOffer nuOffer = this.nuOfferService.findNuOfferById(nuOfferId);
		model.put("nuOffer", nuOffer);
		return "offers/nu/nuOffersDisable";
	}

	@PostMapping(value = "/offers/nu/{nuOfferId}/disable")
	public String disableNuOfferForm(@PathVariable("nuOfferId") final int nuOfferId, final Principal principal,
			final ModelMap model) {

		if (!this.checkIdentity(nuOfferId)) {
			return "error";
		}

		NuOffer nuOffer = this.nuOfferService.findNuOfferById(nuOfferId);
		nuOffer.setStatus(StatusOffer.inactive);
		this.nuOfferService.saveNuOffer(nuOffer);
		return "redirect:/myOffers";

	}

}
