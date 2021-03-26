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

import org.springframework.cheapy.model.SpeedOffer;
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
public class SpeedOfferController {

	private static final String		VIEWS_SPEED_OFFER_CREATE_OR_UPDATE_FORM	= "speedOffers/createOrUpdateSpeedOfferForm";

	private final FoodOfferService	foodOfferService;
	private final NuOfferService	nuOfferService;
	private final SpeedOfferService	speedOfferService;
	private final TimeOfferService	timeOfferService;


	public SpeedOfferController(final FoodOfferService foodOfferService, final NuOfferService nuOfferService, final SpeedOfferService speedOfferService, final TimeOfferService timeOfferService) {
		this.foodOfferService = foodOfferService;
		this.nuOfferService = nuOfferService;
		this.speedOfferService = speedOfferService;
		this.timeOfferService = timeOfferService;

	}

	@GetMapping("/offers/speed/{speedOfferId}")
	public String processShowForm(@PathVariable("speedOfferId") final int speedOfferId, final Map<String, Object> model) {

		SpeedOffer speedOffer = this.speedOfferService.findSpeedOfferById(speedOfferId);

		model.put("speedOffer", speedOffer);

		return "speedOffers/speedOffersShow";

	}

	@GetMapping(value = "/offers/speed/{speedOfferId}/edit")
	public String updateNuOffer(@PathVariable("speedOfferId") final int speedOfferId, final Principal principal, final ModelMap model) {

		SpeedOffer speedOffer = this.speedOfferService.findSpeedOfferById(speedOfferId);
		model.addAttribute("speedOffer", speedOffer);
		return SpeedOfferController.VIEWS_SPEED_OFFER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/offers/speed/{speedOfferId}/edit")
	public String updateNuOffer(@Valid final SpeedOffer speedOfferEdit, final BindingResult result, final Principal principal, final ModelMap model) {

		if (result.hasErrors()) {
			model.addAttribute("speedOffer", speedOfferEdit);
			return SpeedOfferController.VIEWS_SPEED_OFFER_CREATE_OR_UPDATE_FORM;

		} else {
			this.speedOfferService.saveSpeedOffer(speedOfferEdit);
			return "redirect:/offers/speed/" + speedOfferEdit.getId();
		}

	}

	@GetMapping(value = "/offers/speed/{speedOfferId}/disable")
	public String disableSpeedOffer(@PathVariable("speedOfferId") final int speedOfferId, final Principal principal, final ModelMap model) {

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

		SpeedOffer speedOffer = this.speedOfferService.findSpeedOfferById(speedOfferId);
		model.put("speedOffer", speedOffer);
		return "speedOffers/speedOffersDisable";
	}

	@PostMapping(value = "/offers/speed/{speedOfferId}/disable")
	public String disableNuOfferForm(@PathVariable("speedOfferId") final int speedOfferId, final Principal principal, final ModelMap model) {

		//		if (!this.comprobarIdentidad(principal, vehiculoId)) {
		//			return "exception";
		//		}
		//
		//		if (this.tieneCitasAceptadasYPendientes(vehiculoId)) {
		//			return "redirect:/cliente/vehiculos/{vehiculoId}/disable";
		//
		//		} else {
		SpeedOffer speedOffer = this.speedOfferService.findSpeedOfferById(speedOfferId);

		speedOffer.setType(StatusOffer.inactive);

		this.speedOfferService.saveSpeedOffer(speedOffer);

		return "redirect:";

	}

}
