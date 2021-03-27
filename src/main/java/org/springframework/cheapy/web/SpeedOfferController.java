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


import java.util.Map;

import javax.validation.Valid;

import org.springframework.cheapy.model.Client;
import org.springframework.cheapy.model.SpeedOffer;
import org.springframework.cheapy.model.StatusOffer;
import org.springframework.cheapy.service.ClientService;
import org.springframework.cheapy.service.SpeedOfferService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SpeedOfferController {

	private static final String VIEWS_SPEED_OFFER_CREATE_OR_UPDATE_FORM = "offers/speed/createOrUpdateSpeedOfferForm";

	private final SpeedOfferService speedOfferService;
	private final ClientService clientService;

	public SpeedOfferController(final SpeedOfferService speedOfferService, final ClientService clientService) {
		this.speedOfferService = speedOfferService;
		this.clientService = clientService;
	}

	@GetMapping("/offers/speed/new")
	public String initCreationForm(Map<String, Object> model) {
		SpeedOffer speedOffer = new SpeedOffer();
		model.put("speedOffer", speedOffer);
		return VIEWS_SPEED_OFFER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/offers/speed/new")
	public String processCreationForm(@Valid SpeedOffer speedOffer, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_SPEED_OFFER_CREATE_OR_UPDATE_FORM;
		}
		else {
			Client client = this.clientService.getCurrentClient();
			speedOffer.setClient(client);
			speedOffer.setType(StatusOffer.hidden);
			this.speedOfferService.saveSpeedOffer(speedOffer);
			return "redirect:/offers/speed/" + speedOffer.getId();
		}
	}
	
	@GetMapping(value = "/offers/speed/{speedOfferId}/activate")
	public String activateSpeedOffer(@PathVariable("speedOfferId") final int speedOfferId, ModelMap modelMap) {
		SpeedOffer speedOffer = this.speedOfferService.findSpeedOfferById(speedOfferId);
		Client client = this.clientService.getCurrentClient();
		if(speedOffer.getClient().equals(client)) {
			speedOffer.setType(StatusOffer.active);
			speedOffer.setCode("SP-"+speedOfferId);
			this.speedOfferService.saveSpeedOffer(speedOffer);
		} else {
			modelMap.addAttribute("message", "You don't have access to this speed offer");
		}
		return "redirect:/offers/speed/" + speedOffer.getId();
	}
  
  	@GetMapping("/offers/speed/{speedOfferId}")
	public String processShowForm(@PathVariable("speedOfferId") int speedOfferId, Map<String, Object> model) {

		SpeedOffer speedOffer=this.speedOfferService.findSpeedOfferById(speedOfferId);
		model.put("speedOffer", speedOffer);
		return "offers/speed/speedOffersShow";
	}
}
