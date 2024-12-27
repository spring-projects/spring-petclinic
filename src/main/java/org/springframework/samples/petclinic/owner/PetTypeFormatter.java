package org.springframework.samples.petclinic.owner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

/**
 * Formatter to parse and print 'PetType' elements.
 */
@Component
public class PetTypeFormatter implements Formatter<PetType> {

	private final OwnerRepository owners;

	@Autowired
	public PetTypeFormatter(OwnerRepository owners) {
		this.owners = owners;
	}

	@Override
	public String print(PetType petType, Locale locale) {
		return petType.getName();
	}

	@Override
	public PetType parse(String text, Locale locale) throws ParseException {
		Optional<PetType> petType = findPetTypeByName(text);
		return petType.orElseThrow(() -> new ParseException("PetType not found: " + text, 0));
	}

	/**
	 * Helper method to find a PetType by its name.
	 * @param name the name of the pet type to search for
	 * @return an Optional containing the PetType if found, or an empty Optional
	 */
	private Optional<PetType> findPetTypeByName(String name) {
		return this.owners.findPetTypes()
			.stream()
			.filter(petType -> petType.getName().equalsIgnoreCase(name))
			.findFirst();
	}
}

