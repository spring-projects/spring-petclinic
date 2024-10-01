package org.springframework.samples.petclinic.domain;

import io.opentelemetry.instrumentation.annotations.WithSpan;

public class DomainValidationService {


	public void testFunc(){

	}
	// Main method for domain validation logic
	@WithSpan
	public boolean validateDomainLogic(String usr) {
		String domain = normalizeDomainFormat("example.com");

		if (isValidDomain(domain)) {

			if (!containsRestrictedWords(domain) && isValidDomainSuffix(domain)) {
				if (isDomainAvailable(domain)) {
					saveValidationResults(domain, true, true);
				}
				else {
					saveValidationResults(domain, true, false);
				}
			}
			else {
				flagDomainForReview(domain);
				saveValidationResults(domain, false, false);
			}
		}
		else {
			saveValidationResults(domain, false, false);
		}

		asyncNotifyAdmin(domain);
		return true;
	}

	// Fake method to check if a domain is valid
	private boolean isValidDomain(String domain) {
		return domain.matches("^[a-zA-Z0-9.-]+$") && domain.length() >= 3 && !domain.startsWith("-")
				&& !domain.endsWith("-");
	}

	// Fake method to check if a domain is available
	private boolean isDomainAvailable(String domain) {
		return !domain.equalsIgnoreCase("taken.com") && !isReservedDomain(domain) && !isPremiumDomain(domain);
	}

	// Fake method to check if domain contains restricted words
	private boolean containsRestrictedWords(String domain) {
		String[] restrictedWords = { "badword", "restricted" };
		for (String word : restrictedWords) {
			if (domain.contains(word)) {
				return true;
			}
		}
		return false;
	}

	// Fake method to simulate saving domain validation results
	private void saveValidationResults(String domain, boolean isValid, boolean isAvailable) {
		// Simulated save operation: Creating a fake result object and printing its
		// contents
		ValidationResult result = new ValidationResult(domain, isValid, isAvailable);
		System.out.println("Saving results: " + result);
	}

	// Fake method to flag domain for manual review
	private void flagDomainForReview(String domain) {
		// Simulated flagging operation: Creating a fake review request and printing its
		// contents
		ReviewRequest request = new ReviewRequest(domain, "Contains restricted words or invalid suffix");
		System.out.println("Flagging for review: " + request);
	}

	// Fake method to notify admin about the validation results
	private void notifyAdmin(String domain) {
		// Simulated notification: Creating a fake notification object and printing its
		// contents
		Notification notification = new Notification("admin@example.com", "Domain Validation Completed",
				"Validation completed for domain: " + domain);
		try {
			Thread.sleep(2000);
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		System.out.println("Notifying admin: " + notification);
	}

	private void asyncNotifyAdmin(String domain) {
		// Simulated notification: Creating a fake notification object and printing its
		// contents
		Notification notification = new Notification("admin@example.com", "Domain Validation Completed",
			"Validation completed for domain: " + domain);
		try {
			Thread.sleep(2000);
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		System.out.println("Notifying admin: " + notification);
	}


	// Fake method to check if the domain is reserved
	private boolean isReservedDomain(String domain) {
		String[] reservedDomains = { "example.com", "localhost" };
		for (String reserved : reservedDomains) {
			if (domain.equalsIgnoreCase(reserved)) {
				return true;
			}
		}
		return false;
	}

	// Fake method to check if the domain is a premium domain
	private boolean isPremiumDomain(String domain) {
		String[] premiumDomains = { "premium.com", "exclusive.com" };
		for (String premium : premiumDomains) {
			if (domain.equalsIgnoreCase(premium)) {
				return true;
			}
		}
		return false;
	}

	// Fake method to simulate domain format normalization
	private String normalizeDomainFormat(String domain) {
		return domain.toLowerCase().trim();
	}

	// Fake method to simulate domain suffix validation
	private boolean isValidDomainSuffix(String domain) {
		String[] validSuffixes = { ".com", ".net", ".org" };
		for (String suffix : validSuffixes) {
			if (domain.endsWith(suffix)) {
				return true;
			}
		}
		return false;
	}

	// Fake class to represent validation results
	private class ValidationResult {

		String domain;

		boolean isValid;

		boolean isAvailable;

		ValidationResult(String domain, boolean isValid, boolean isAvailable) {
			this.domain = domain;
			this.isValid = isValid;
			this.isAvailable = isAvailable;
		}

		@Override
		public String toString() {
			return "ValidationResult{domain='" + domain + "', isValid=" + isValid + ", isAvailable=" + isAvailable
					+ "}";
		}

	}

	// Fake class to represent a review request
	private class ReviewRequest {

		String domain;

		String reason;

		ReviewRequest(String domain, String reason) {
			this.domain = domain;
			this.reason = reason;
		}

		@Override
		public String toString() {
			return "ReviewRequest{domain='" + domain + "', reason='" + reason + "'}";
		}

	}

	// Fake class to represent a notification
	private class Notification {

		String recipient;

		String subject;

		String message;

		Notification(String recipient, String subject, String message) {
			this.recipient = recipient;
			this.subject = subject;
			this.message = message;
		}

		@Override
		public String toString() {
			return "Notification{recipient='" + recipient + "', subject='" + subject + "', message='" + message + "'}";
		}

	}

}
