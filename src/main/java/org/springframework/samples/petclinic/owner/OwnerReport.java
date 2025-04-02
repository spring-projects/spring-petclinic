package org.springframework.samples.petclinic.owner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.springframework.samples.petclinic.owner.Visit;

@Component
public class OwnerReport {

	@Autowired
	private OwnerRepository ownerRepository;

	public void printPetAppointments(int ownerId) {
		Owner owner = ownerRepository.findById(ownerId)
			.orElseThrow(() -> new IllegalArgumentException("Owner not found with ID: " + ownerId));

		System.out.println("=== Appointments Report for " + owner.getFirstName() + " " + owner.getLastName() + " ===");
		System.out.println();

		Map<String, Integer> appointmentCount = new HashMap<>();

		owner.getPets().forEach(pet -> {
			Collection<Visit> visits = pet.getVisits();

			if (visits.isEmpty()) {
				System.out.printf("Pet %s (Owner: %s %s) has no appointments scheduled%n", pet.getName(),
						owner.getFirstName(), owner.getLastName());
			}
			else {
				visits.forEach(visit -> {
					String dayOfWeek = visit.getDate()
						.getDayOfWeek()
						.getDisplayName(TextStyle.FULL, Locale.getDefault());

					String surchargeInfo = "";
					if (visit.getDate().getDayOfWeek() == DayOfWeek.SATURDAY) {
						int month = visit.getDate().getMonth().getValue();
						if (month == 12) {
							surchargeInfo = " (December Saturday surcharge: 80% extra)";
						}
						else if (month == 1) {
							surchargeInfo = " (January Saturday surcharge: 70% extra)";
						}
						else if (month == 8) {
							surchargeInfo = " (August Saturday: No extra charge)";
						}
						else {
							surchargeInfo = " (Weekend surcharge: 50% extra)";
						}
					}
					else if (visit.getDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
						surchargeInfo = " (Weekend surcharge: 75% extra)";
					}

					System.out.printf("Pet: %s | Owner: %s %s | Date: %s (%s)%s | Description: %s%n", pet.getName(),
							owner.getFirstName(), owner.getLastName(), visit.getDate(), dayOfWeek, surchargeInfo,
							visit.getDescription());

					String visitDate = visit.getDate().toString();
					appointmentCount.merge(visitDate, 1, Integer::sum);
				});
			}
			System.out.println();
		});

		System.out.println("=== Summary of Appointments per Date ===");
		appointmentCount.forEach((date, count) -> {
			LocalDate visitDate = LocalDate.parse(date);
			String dayOfWeek = visitDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());

			System.out.printf("%s (%s): %d appointment(s)", date, dayOfWeek, count);
			if (count >= 3) {
				System.out.println(" - WARNING: High number of visits for this date!");
			}
			else {
				System.out.println(" - There are still available slots for this date");
			}
		});
	}

}
