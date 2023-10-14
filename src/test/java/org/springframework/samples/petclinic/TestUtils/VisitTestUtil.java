package org.springframework.samples.petclinic.TestUtils;

import lombok.NoArgsConstructor;
import org.springframework.samples.petclinic.owner.Visit;
import org.springframework.samples.petclinic.vet.Specialty;
import org.springframework.samples.petclinic.vet.Vet;

import java.time.LocalDate;

@NoArgsConstructor
public class VisitTestUtil {

	public static Visit createVisit() {
		return createVisit(LocalDate.now());
	}

	public static Visit createVisit(LocalDate localDate) {
		var visit = new Visit();
		visit.setId(1);
		visit.setDate(localDate);
		visit.setDescription("Just a visit");
		return visit;
	}

}
