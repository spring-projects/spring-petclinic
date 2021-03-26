/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cheapy.web;

import java.security.Principal;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.cheapy.model.NuOffer;
import org.springframework.cheapy.model.StatusOffer;
import org.springframework.cheapy.service.FoodOfferService;
import org.springframework.cheapy.service.NuOfferService;
import org.springframework.cheapy.service.SpeedOfferService;
import org.springframework.cheapy.service.TimeOfferService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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

	private static final String		VIEWS_NU_OFFER_CREATE_OR_UPDATE_FORM	= "nuOffers/createOrUpdateNuOfferForm";

	private final FoodOfferService	foodOfferService;
	private final NuOfferService	nuOfferService;
	private final SpeedOfferService	speedOfferService;
	private final TimeOfferService	timeOfferService;


	public NuOfferController(final FoodOfferService foodOfferService, final NuOfferService nuOfferService, final SpeedOfferService speedOfferService, final TimeOfferService timeOfferService) {
		this.foodOfferService = foodOfferService;
		this.nuOfferService = nuOfferService;
		this.speedOfferService = speedOfferService;
		this.timeOfferService = timeOfferService;

	}

	@GetMapping("/offers/nu/{nuOfferId}")
	public String processShowForm(@PathVariable("nuOfferId") final int nuOfferId, final Map<String, Object> model) {

		NuOffer nuOffer = this.nuOfferService.findNuOfferById(nuOfferId);

		model.put("nuOffer", nuOffer);

		return "nuOffers/nuOffersShow";

	}

	@GetMapping(value = "/offers/nu/{nuOfferId}/edit")
	public String updateNuOffer(@PathVariable("nuOfferId") final int nuOfferId, final Principal principal, final ModelMap model) {

		NuOffer nuOffer = this.nuOfferService.findNuOfferById(nuOfferId);
		model.addAttribute("nuOffer", nuOffer);
		return NuOfferController.VIEWS_NU_OFFER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/offers/nu/{nuOfferId}/edit")
	public String updateNuOffer(@Valid final NuOffer nuOfferEdit, final BindingResult result, final Principal principal, final ModelMap model) {

		if (result.hasErrors()) {
			model.addAttribute("nuOffer", nuOfferEdit);
			return NuOfferController.VIEWS_NU_OFFER_CREATE_OR_UPDATE_FORM;

		} else {
			this.nuOfferService.saveNuOffer(nuOfferEdit);
			return "redirect:/offers/nu/" + nuOfferEdit.getId();
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

		NuOffer nuOffer = this.nuOfferService.findNuOfferById(nuOfferId);
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
		NuOffer nuOffer = this.nuOfferService.findNuOfferById(nuOfferId);

		nuOffer.setType(StatusOffer.inactive);

		this.nuOfferService.saveNuOffer(nuOffer);

		return "redirect:";

	}

}
