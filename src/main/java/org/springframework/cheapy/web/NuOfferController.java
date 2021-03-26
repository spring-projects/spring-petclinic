/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import org.springframework.cheapy.service.NuOfferService;
import org.springframework.cheapy.service.SpeedOfferService;
import org.springframework.cheapy.service.TimeOfferService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller 
public class NuOfferController {

	private static final String VIEWS_NU_OFFER_CREATE_OR_UPDATE_FORM = "nuOffers/createOrUpdateNuOfferForm";

	private final NuOfferService nuOfferService; 



	public NuOfferController(final NuOfferService nuOfferService) {
		this.nuOfferService = nuOfferService;

	}
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}


	@GetMapping("/offers/nu/{nuOfferId}")
	public String processShowForm(@PathVariable("nuOfferId") int nuOfferId, Map<String, Object> model) {

		NuOffer nuOffer=this.nuOfferService.findNuOfferById(nuOfferId);
		
		model.put("nuOffer", nuOffer);
		
		return "nuOffers/nuOffersShow";

	}

//	@GetMapping("/owners/{ownerId}/edit")
//	public String initUpdateOwnerForm(@PathVariable("ownerId") int ownerId, Model model) {
//		Owner owner = this.ownerService.findOwnerById(ownerId);
//		model.addAttribute(owner);
//		return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
//	}
//
//	@PostMapping("/owners/{ownerId}/edit")
//	public String processUpdateOwnerForm(@Valid Owner owner, BindingResult result,
//			@PathVariable("ownerId") int ownerId) {
//		if (result.hasErrors()) {
//			return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
//		}
//		else {
//			owner.setId(ownerId);
//			this.ownerService.saveOwner(owner);
//			return "redirect:/owners/{ownerId}";
//		}
//	}
//	@GetMapping("/owners/{ownerId}")
//	public ModelAndView showOwner(@PathVariable("ownerId") int ownerId) {
//		ModelAndView mav = new ModelAndView("owners/ownerDetails");
//		Owner owner = this.ownerService.findOwnerById(ownerId);
//		
//		mav.addObject(owner);
//		return mav;
//	}
	
	
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
