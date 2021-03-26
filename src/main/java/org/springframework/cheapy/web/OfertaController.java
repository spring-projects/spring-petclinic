package org.springframework.cheapy.web;

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
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OfertaController {

	private final FoodOfferService foodOfferService;
	private final NuOfferService nuOfferService; 
	private final SpeedOfferService speedOfferService;
	private final TimeOfferService timeOfferService;

	public OfertaController(final FoodOfferService foodOfferService, final NuOfferService nuOfferService,
			final SpeedOfferService speedOfferService, final TimeOfferService timeOfferService) {
		this.foodOfferService = foodOfferService;
		this.nuOfferService = nuOfferService;
		this.speedOfferService = speedOfferService;
		this.timeOfferService = timeOfferService;
	}

	@GetMapping("/offers")
	public String processFindForm( Map<String, Object> model) {

		List<FoodOffer> foodOfferLs=this.foodOfferService.findAllFoodOffer();
		List<NuOffer> nuOfferLs=this.nuOfferService.findAllNuOffer();
		List<SpeedOffer> speedOfferLs=this.speedOfferService.findAllSpeedOffer();
		List<TimeOffer> timeOfferLs=this.timeOfferService.findAllTimeOffer();
		
		model.put("foodOfferLs", foodOfferLs);
		model.put("nuOfferLs", nuOfferLs);
		model.put("speedOfferLs", speedOfferLs);
		model.put("timeOfferLs", timeOfferLs);
		
		return "offers/offersList";

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
	
	
}
