package org.springframework.cheapy.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;


import org.springframework.beans.BeanUtils;
import org.springframework.cheapy.model.FoodOffer;
import org.springframework.cheapy.model.NuOffer;
import org.springframework.cheapy.model.Owner;
import org.springframework.cheapy.model.SpeedOffer;
import org.springframework.cheapy.model.StatusOffer;
import org.springframework.cheapy.model.TimeOffer;
import org.springframework.cheapy.service.FoodOfferService;
import org.springframework.cheapy.model.Client;
import org.springframework.cheapy.model.NuOffer;
import org.springframework.cheapy.model.StatusOffer;
import org.springframework.cheapy.service.ClientService;

import org.springframework.cheapy.service.NuOfferService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class NuOfferController {

	private static final String VIEWS_NU_OFFER_CREATE_OR_UPDATE_FORM = "nuOffers/createOrUpdateNuOfferForm";

	private final NuOfferService nuOfferService;
	private final ClientService clientService;

	public NuOfferController(final NuOfferService nuOfferService,ClientService clientService) {
		this.nuOfferService = nuOfferService;
		this.clientService = clientService;
		
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping("/nuOffers/new")
	public String initCreationForm(Map<String, Object> model) {
		NuOffer nuOffer = new NuOffer();
		model.put("nuOffer", nuOffer);
		return VIEWS_NU_OFFER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/nuOffers/new")
	public String processCreationForm(@Valid NuOffer nuOffer, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_NU_OFFER_CREATE_OR_UPDATE_FORM;
		}
		else {
			nuOffer.setType(StatusOffer.hidden);
		
			Client client = this.clientService.getCurrentClient();
			
			nuOffer.setClient(client);
			
			
			this.nuOfferService.saveNuOffer(nuOffer);
			return "redirect:/nuOffers/" + nuOffer.getId();
		}
	}
	@GetMapping(value ="/nuOffers/{nuOfferId}/activate")
	public String activateNuOffer(@PathVariable("nuOfferId") final int nuOfferId, final ModelMap modelMap) {
		Client client = this.clientService.getCurrentClient();
		NuOffer nuOffer=this.nuOfferService.findNuOfferById(nuOfferId);
		if(nuOffer.getClient().equals(client)) {
			nuOffer.setType(StatusOffer.active);
			nuOffer.setCode("NU-"+nuOfferId);
			this.nuOfferService.saveNuOffer(nuOffer);
			
			return "redirect:/nuOffers/" + nuOffer.getId();	
		} else {
		         modelMap.addAttribute("message", "You don't have access to this number offer");
		        }
		        return "redirect:/nuOffers/";
		

	}
  
  	@GetMapping("/offers/nu/{nuOfferId}")
	public String processShowForm(@PathVariable("nuOfferId") int nuOfferId, Map<String, Object> model) {

		NuOffer nuOffer=this.nuOfferService.findNuOfferById(nuOfferId);
		
		model.put("nuOffer", nuOffer);
		
		return "nuOffers/nuOffersShow";

	}


	
	@GetMapping(value = "/offers/nu/{nuOfferId}/edit")
	public String updateNuOffer(@PathVariable("nuOfferId") final int nuOfferId, final Principal principal, final ModelMap model) {

//		if (!this.comprobarIdentidad(principal, vehiculoId)) {
//			return "exception";
//		}
		
		NuOffer nuOffer=this.nuOfferService.findNuOfferById(nuOfferId);
		model.put("nuOffer", nuOffer);
		return VIEWS_NU_OFFER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/offers/nu/{nuOfferId}/edit")
	public String updateNuOffer(@Valid final NuOffer nuOfferEdit, final BindingResult result, @PathVariable("nuOfferId") final int nuOfferId, final Principal principal, final ModelMap model) {

//		if (!this.comprobarIdentidad(principal, vehiculoId)) {
//			return "exception";
//		}

		if (result.hasErrors()) {
			model.put("nuOffer", nuOfferEdit);
			return VIEWS_NU_OFFER_CREATE_OR_UPDATE_FORM;

		} else {

			NuOffer nuOfferOld=this.nuOfferService.findNuOfferById(nuOfferId);

			BeanUtils.copyProperties(nuOfferEdit, nuOfferOld, "id", "client_id");
			
			this.nuOfferService.saveNuOffer(nuOfferOld);

			return "offers/offersList";
		}

	}
	
	@GetMapping(value ="/offers/nu/{nuOfferId}/edit")
	public String initUpdateNuOfferForm(@PathVariable("nuOfferId") int nuOfferId, Model model) {
		NuOffer nuOffer=this.nuOfferService.findNuOfferById(nuOfferId);
		model.addAttribute(nuOffer);
		return VIEWS_NU_OFFER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/offers/nu/{nuOfferId}/edit")
	public String processUpdateOwnerForm(@Valid NuOffer nuOffer, BindingResult result,
			@PathVariable("nuOfferId") int nuOfferId) {
		if (result.hasErrors()) {
			return VIEWS_NU_OFFER_CREATE_OR_UPDATE_FORM;
		}
		else {
			nuOffer.setId(nuOfferId);
			this.nuOfferService.saveNuOffer(nuOffer);
			return "redirect:/offers/nu/{nuOfferId}";
		}
	}
	
	@GetMapping(value = "/offers/nu/{nuOfferId}/disable")
	public String disableNuOffer(@PathVariable("nuOfferId") final int nuOfferId, final Principal principal, final ModelMap model) {

//		if (!this.comprobarIdentidad(principal, vehiculoId)) {
//			return "exception";
//		}
//
//		if (this.tieneCitasAceptadasYPendientes(vehiculoId)) {
//			model.addAttribute("x", true);
//
//		} else {
//			model.addAttribute("x", false);
//		}

		NuOffer nuOffer=this.nuOfferService.findNuOfferById(nuOfferId);
		model.put("nuOffer", nuOffer);
		return "nuOffers/nuOffersDisable";
	}

	@PostMapping(value = "/offers/nu/{nuOfferId}/disable")	
	public String disableNuOfferForm(@PathVariable("nuOfferId") final int nuOfferId, final Principal principal, final ModelMap model) {

//		if (!this.comprobarIdentidad(principal, vehiculoId)) {
//			return "exception";
//		}
//
//		if (this.tieneCitasAceptadasYPendientes(vehiculoId)) {
//			return "redirect:/cliente/vehiculos/{vehiculoId}/disable";
//
//		} else {
		NuOffer nuOffer=this.nuOfferService.findNuOfferById(nuOfferId);
		
		nuOffer.setType(StatusOffer.inactive);
		
		this.nuOfferService.saveNuOffer(nuOffer);
		
		return "redirect:";
			
	}
	
}
