@Test
void testFindAll() throws CustomException {
    try {
        vets.findAll();
    } catch (Exception e) {
        throw new CustomException("Error finding all vets", e);
    }
    vets.findAll(); // served from cache
}