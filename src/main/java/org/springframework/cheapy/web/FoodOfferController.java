
package org.springframework.cheapy.web;

import java.time.format.DateTimeFormatter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.cheapy.model.Client;
import org.springframework.cheapy.model.FoodOffer;
import org.springframework.cheapy.model.StatusOffer;
import org.springframework.cheapy.service.ClientService;
import org.springframework.cheapy.service.FoodOfferService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FoodOfferController {

	private static final String VIEWS_FOOD_OFFER_CREATE_OR_UPDATE_FORM = "offers/food/createOrUpdateFoodOfferForm";

	private final FoodOfferService foodOfferService;
	private final ClientService clientService;

	public FoodOfferController(final FoodOfferService foodOfferService, final ClientService clientService) {
		this.foodOfferService = foodOfferService;
		this.clientService = clientService;
	}

	private boolean checkIdentity(final int foodOfferId) {
		boolean res = false;
		Client client = this.clientService.getCurrentClient();
		FoodOffer foodOffer = this.foodOfferService.findFoodOfferById(foodOfferId);
		Client clientOffer = foodOffer.getClient();
		if (client.equals(clientOffer)) {
			res = true;
		}
		return res;
	}

	private boolean checkOffer(final FoodOffer session, final FoodOffer offer) {
		boolean res = false;
		if (session.getId() == offer.getId() && session.getStatus() == offer.getStatus()
				&& (session.getCode() == null ? offer.getCode() == "" : session.getCode().equals(offer.getCode())) && !(session.getStatus().equals(StatusOffer.inactive))) {
			res = true;
		}
		return res;
	}
	
	private boolean checkDates(final FoodOffer foodOffer) {
		boolean res = false;
		if(foodOffer.getEnd().isAfter(foodOffer.getStart())) {
			res = true;
		}
		return res;
	}

	@GetMapping("/offers/food/new")
	public String initCreationForm(Map<String, Object> model) {
		FoodOffer foodOffer = new FoodOffer();
		model.put("foodOffer", foodOffer);
		return VIEWS_FOOD_OFFER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/offers/food/new")
	public String processCreationForm(@Valid FoodOffer foodOffer, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_FOOD_OFFER_CREATE_OR_UPDATE_FORM;
		} else {
			if(!this.checkDates(foodOffer)) {
				result.rejectValue("end","" ,"La fecha de fin debe ser posterior a la fecha de inicio");
				return VIEWS_FOOD_OFFER_CREATE_OR_UPDATE_FORM;
			}
			Client client = this.clientService.getCurrentClient();
			foodOffer.setClient(client);
			foodOffer.setStatus(StatusOffer.hidden);
			this.foodOfferService.saveFoodOffer(foodOffer);
			return "redirect:/offers/food/" + foodOffer.getId();
		}
	}

	@GetMapping(value = "/offers/food/{foodOfferId}/activate")
	public String activateFoodOffer(@PathVariable("foodOfferId") final int foodOfferId, ModelMap modelMap) {
		FoodOffer foodOffer = this.foodOfferService.findFoodOfferById(foodOfferId);
		Client client = this.clientService.getCurrentClient();
		if (foodOffer.getClient().equals(client)) {
			foodOffer.setStatus(StatusOffer.active);
			foodOffer.setCode("FO-" + foodOfferId);
			this.foodOfferService.saveFoodOffer(foodOffer);
		} else {
			modelMap.addAttribute("message", "You don't have access to this food offer");
		}
		return "redirect:/offers/food/" + foodOfferId;

	}

	@GetMapping("/offers/food/{foodOfferId}")
	public String processShowForm(@PathVariable("foodOfferId") int foodOfferId, Map<String, Object> model) {

		FoodOffer foodOffer = this.foodOfferService.findFoodOfferById(foodOfferId);

		model.put("foodOffer", foodOffer);

		model.put("localDateTimeFormat", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

		return "offers/food/foodOffersShow";

	}

	@GetMapping(value = "/offers/food/{foodOfferId}/edit")
	public String updateFoodOffer(@PathVariable("foodOfferId") final int foodOfferId, final ModelMap model,
			HttpServletRequest request) {

		if (!this.checkIdentity(foodOfferId)) {
			return "error";
		}
		FoodOffer foodOffer = this.foodOfferService.findFoodOfferById(foodOfferId);
		if (foodOffer.getStatus().equals(StatusOffer.inactive)) {
			return "error";
		}
		model.addAttribute("foodOffer", foodOffer);
		request.getSession().setAttribute("idFood", foodOfferId);
		return FoodOfferController.VIEWS_FOOD_OFFER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/offers/food/{foodOfferId}/edit")
	public String updateFoodOffer(@Valid final FoodOffer foodOfferEdit, final BindingResult result,
			final ModelMap model, HttpServletRequest request) {

		if (!this.checkIdentity(foodOfferEdit.getId())) {
			return "error";
		}
		Integer id = (Integer) request.getSession().getAttribute("idFood");
		FoodOffer foodOffer = this.foodOfferService.findFoodOfferById(id);
		if (!this.checkOffer(foodOffer, foodOfferEdit)) {
			return "error";
		}

		if (result.hasErrors()) {
			model.addAttribute("foodOffer", foodOfferEdit);
			return FoodOfferController.VIEWS_FOOD_OFFER_CREATE_OR_UPDATE_FORM;

		} else {
			if(!this.checkDates(foodOfferEdit)) {
				result.rejectValue("end","" ,"La fecha de fin debe ser posterior a la fecha de inicio");
				return FoodOfferController.VIEWS_FOOD_OFFER_CREATE_OR_UPDATE_FORM;
			}
			BeanUtils.copyProperties(this.foodOfferService.findFoodOfferById(foodOfferEdit.getId()), foodOfferEdit,
					"start", "end", "food", "discount");
			this.foodOfferService.saveFoodOffer(foodOfferEdit);
			return "redirect:/offers/food/" + foodOfferEdit.getId();
		}
	}

	@GetMapping(value = "/offers/food/{foodOfferId}/disable")
	public String disableFoodOffer(@PathVariable("foodOfferId") final int foodOfferId, final ModelMap model) {

		if (!this.checkIdentity(foodOfferId)) {
			return "error";
		}

		FoodOffer foodOffer = this.foodOfferService.findFoodOfferById(foodOfferId);
		model.put("foodOffer", foodOffer);
		return "offers/food/foodOffersDisable";
	}

	@PostMapping(value = "/offers/food/{foodOfferId}/disable")
	public String disableFoodOfferForm(@PathVariable("foodOfferId") final int foodOfferId, final ModelMap model) {

		if (!this.checkIdentity(foodOfferId)) {
			return "error";
		}

		FoodOffer foodOffer = this.foodOfferService.findFoodOfferById(foodOfferId);

		foodOffer.setStatus(StatusOffer.inactive);

		this.foodOfferService.saveFoodOffer(foodOffer);

		return "redirect:/myOffers";

	}
}
