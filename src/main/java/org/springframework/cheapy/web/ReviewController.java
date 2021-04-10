
package org.springframework.cheapy.web;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.cheapy.model.Review;
import org.springframework.cheapy.model.User;
import org.springframework.cheapy.service.ReviewService;
import org.springframework.cheapy.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ReviewController {

	private static final String	VIEWS_REVIEWS_CREATE_OR_UPDATE_FORM	= "reviews/createOrUpdateReviewForm";
	private final ReviewService	reviewService;
	private final UserService	userService;


	public ReviewController(final ReviewService reviewService, final UserService userService) {
		this.reviewService = reviewService;
		this.userService = userService;
	}
	private boolean checkIdentity(final int reviewId) {
		boolean res = false;
		User user = this.userService.getCurrentUser();
		Review review = this.reviewService.findReviewById(reviewId);
		User reviewsAuthor = review.getEscritor();
		if (user.equals(reviewsAuthor)) {
			res = true;
		}
		return res;
	}
	@GetMapping("/reviewsList/{page}")
	public String processFindForm(@PathVariable("page") final int page, final Map<String, Object> model) {
		Pageable elements = PageRequest.of(page, 6);

		List<Review> reviewsLs = this.reviewService.findAllReviews(elements);
		model.put("reviewsLs", reviewsLs);

		return "reviews/reviewsList";

	}

	@GetMapping("/reviews/new")
	public String initCreationForm(final Map<String, Object> model) {
		Review review = new Review();
		model.put("review", review);
		return ReviewController.VIEWS_REVIEWS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/reviews/new")
	public String processCreationForm(@Valid final Review review, final BindingResult result) {
		if (result.hasErrors()) {
			return ReviewController.VIEWS_REVIEWS_CREATE_OR_UPDATE_FORM;
		} else {
			User escritor = this.userService.getCurrentUser();
			review.setEscritor(escritor);

			this.reviewService.saveReview(review);
			return "redirect:/reviews/" + review.getId();
		}
	}

	@GetMapping("/reviews/{reviewId}")
	public String processShowForm(@PathVariable("reviewId") final int reviewId, final Map<String, Object> model) {

		Review review = this.reviewService.findReviewById(reviewId);

		model.put("review", review);

		return "reviews/reviewsShow";

	}

	@GetMapping(value = "/reviews/{reviewId}/edit")
	public String updateReview(@PathVariable("reviewId") final int reviewId, final ModelMap model) {
		if (!this.checkIdentity(reviewId)) {
			return "error";
		}

		Review review = this.reviewService.findReviewById(reviewId);
		model.addAttribute("review", review);
		return ReviewController.VIEWS_REVIEWS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/reviews/{reviewId}/edit")
	public String updateReview(@Valid final Review reviewEdit, final BindingResult result, final ModelMap model) {
		if (!this.checkIdentity(reviewEdit.getId())) {
			return "error";
		}
		if (result.hasErrors()) {
			model.addAttribute("review", reviewEdit);
			return ReviewController.VIEWS_REVIEWS_CREATE_OR_UPDATE_FORM;

		} else {
			User escritor = this.userService.getCurrentUser();
			reviewEdit.setEscritor(escritor);

			this.reviewService.saveReview(reviewEdit);
			return "redirect:/reviews/" + reviewEdit.getId();
		}

	}

}
