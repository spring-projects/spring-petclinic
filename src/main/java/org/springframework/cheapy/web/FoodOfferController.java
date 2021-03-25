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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.cheapy.model.FoodOffer;
import org.springframework.cheapy.model.NuOffer;
import org.springframework.cheapy.model.SpeedOffer;
import org.springframework.cheapy.model.TimeOffer;
import org.springframework.cheapy.service.FoodOfferService;
import org.springframework.cheapy.service.NuOfferService;
import org.springframework.cheapy.service.SpeedOfferService;
import org.springframework.cheapy.service.TimeOfferService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller 
public class FoodOfferController {

	//private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";

	private final FoodOfferService foodOfferService;



	public FoodOfferController(final FoodOfferService foodOfferService) {
		this.foodOfferService = foodOfferService;

	}

	@GetMapping("/offers/food/{foodOfferId}")
	public String processShowForm(@PathVariable("foodOfferId") int foodOfferId, Map<String, Object> model) {

		FoodOffer foodOffer=this.foodOfferService.findFoodOfferById(foodOfferId);
		
		model.put("foodOffer", foodOffer);
		
		return "foodOffers/foodOffersShow";

	}

}
