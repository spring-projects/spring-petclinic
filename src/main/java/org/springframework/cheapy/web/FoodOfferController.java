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
import org.springframework.cheapy.model.FoodOffer;
import org.springframework.cheapy.model.StatusOffer;
import org.springframework.cheapy.service.ClientService;
import org.springframework.cheapy.service.FoodOfferService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FoodOfferController {

	private static final String VIEWS_FOOD_OFFER_CREATE_OR_UPDATE_FORM = "foodOffers/createOrUpdateFoodOfferForm";

	private final FoodOfferService foodOfferService;
	private final ClientService clientService;


	public FoodOfferController(final FoodOfferService foodOfferService, final ClientService clientService) {
		this.foodOfferService = foodOfferService;
		this.clientService = clientService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping("/foodOffers/new")
	public String initCreationForm(Map<String, Object> model) {
		FoodOffer foodOffer = new FoodOffer();
		model.put("foodOffer", foodOffer);
		return VIEWS_FOOD_OFFER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/foodOffers/new")
	public String processCreationForm(@Valid FoodOffer foodOffer, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_FOOD_OFFER_CREATE_OR_UPDATE_FORM;
		}
		else {
			Client client = this.clientService.getCurrentclient();
			foodOffer.setClient(client);
			foodOffer.setType(StatusOffer.hidden);
			this.foodOfferService.saveFoodOffer(foodOffer);
			return "redirect:/foodOffers/" + foodOffer.getId();
		}
	}
	
	@GetMapping(value = "/foodOffers/{foodOfferId}/activate")
	public String activateFoodOffer(@PathVariable("foodOfferId") final int foodOfferId, ModelMap modelMap) {
		FoodOffer foodOffer = this.foodOfferService.findFoodOfferById(foodOfferId);
		Client client = this.clientService.getCurrentclient();
		if(foodOffer.getClient().equals(client)) {
			foodOffer.setType(StatusOffer.active);
			foodOffer.setCode("FE-"+foodOfferId);
			this.foodOfferService.saveFoodOffer(foodOffer);
		} else {
			modelMap.addAttribute("message", "You don't have access to this food offer");
		}
		return "redirect:/foodOffers/";
	}
}
