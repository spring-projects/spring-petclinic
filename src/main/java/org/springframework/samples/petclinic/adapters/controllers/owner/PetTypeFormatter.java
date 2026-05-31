package org.springframework.samples.petclinic.adapters.controllers.owner;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.samples.petclinic.adapters.dtos.owner.PetTypeDto;
import org.springframework.samples.petclinic.adapters.mappers.owner.OwnerMapper;
import org.springframework.samples.petclinic.core.usecases.owner.ports.FindPetTypePort;
import org.springframework.stereotype.Component;

@Component
class PetTypeFormatter implements Formatter<PetTypeDto> {

	private final FindPetTypePort findPetTypePort;

	PetTypeFormatter(FindPetTypePort findPetTypePort) {
		this.findPetTypePort = findPetTypePort;
	}

	@Override
	public String print(PetTypeDto petType, Locale locale) {
		String name = petType.getName();
		return name != null ? name : "<null>";
	}

	@Override
	public PetTypeDto parse(String text, Locale locale) throws ParseException {
		List<PetTypeDto> types = findPetTypePort.findAll().stream().map(OwnerMapper::petTypeToDto).toList();
		return types.stream()
			.filter(t -> t.getName().equals(text))
			.findFirst()
			.orElseThrow(() -> new ParseException("type not found: " + text, 0));
	}

}
