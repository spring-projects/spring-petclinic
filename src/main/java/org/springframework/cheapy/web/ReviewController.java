package org.springframework.cheapy.web;


import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.cheapy.model.Review;
import org.springframework.cheapy.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ReviewController {


	private static final String VIEWS_REVIEWS_CREATE_OR_UPDATE_FORM = "reviews/createOrUpdateReviewForm";
	private final ReviewService reviewService;

	public ReviewController(final ReviewService reviewService) {
		this.reviewService = reviewService;
	}
	@GetMapping("/reviews")
	public String processFindForm( Map<String, Object> model) {

		List<Review> reviewsLs=this.reviewService.findAllReviews();
		model.put("reviewsLs", reviewsLs);
		
		return "reviews/reviewsList";

	}

	@GetMapping("/reviews/new")
	public String initCreationForm(Map<String, Object> model) {
		Review review = new Review();
		model.put("review", review);
		return VIEWS_REVIEWS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/reviews/new")
	public String processCreationForm(@Valid Review review, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_REVIEWS_CREATE_OR_UPDATE_FORM;
		} else {
			this.reviewService.saveReview(review);
			return "redirect:/reviews/" + review.getId();
		}
	}

	

	@GetMapping("/reviews/{reviewId}")
	public String processShowForm(@PathVariable("reviewId") int reviewId, Map<String, Object> model) {

		Review review = this.reviewService.findReviewById(reviewId);

		model.put("review", review);

		
		return "reviews/reviewsShow";

	}

	@GetMapping(value = "/reviews/{reviewId}/edit")
	public String updateReview(@PathVariable("reviewId") final int reviewId, final ModelMap model) {
		

		Review review = this.reviewService.findReviewById(reviewId);
		model.addAttribute("review", review);
		return ReviewController.VIEWS_REVIEWS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/reviews/{reviewId}/edit")
	public String updateReview(@Valid final Review reviewEdit, final BindingResult result, final ModelMap model) {
		
		if (result.hasErrors()) {
			model.addAttribute("review", reviewEdit);
			return ReviewController.VIEWS_REVIEWS_CREATE_OR_UPDATE_FORM;

		} else {
			this.reviewService.saveReview(reviewEdit);
			return "redirect:/reviews/" + reviewEdit.getId();
		}

	}

}
