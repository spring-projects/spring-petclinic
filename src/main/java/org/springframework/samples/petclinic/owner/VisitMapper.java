package org.springframework.samples.petclinic.owner;

public class VisitMapper {
	public static VisitDto toDto(Visit visit) {
		return new VisitDto(
			visit.getId(),
			visit.getDate(),
			visit.getDescription()
		);
	}

	public static Visit toVisit(VisitDto visitDto) {
		Visit visit = new Visit();
		visit.setDate(visitDto.visitDate());
		visit.setDescription(visitDto.visitDescription());
		if (visitDto.id() != null) {
			visit.setId(visitDto.id());
		}
		return visit;
	}
}
