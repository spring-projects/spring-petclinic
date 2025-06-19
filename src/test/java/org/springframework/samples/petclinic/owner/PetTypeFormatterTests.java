@Test
void shouldThrowParseException() {
    given(this.pets.findPetTypes()).willReturn(makePetTypes());
    Assertions.assertThrows(ParseException.class, () -> petTypeFormatter.parse("Fish", Locale.ENGLISH));
}

private List<PetType> makePetTypes() {
    List<PetType> petTypes = new ArrayList<>();
    petTypes.add(new PetType("Dog"));
    petTypes.add(new PetType("Bird"));
    return petTypes;
}