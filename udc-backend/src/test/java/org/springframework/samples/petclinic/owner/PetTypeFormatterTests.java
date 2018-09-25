package org.springframework.samples.petclinic.owner;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link PetTypeFormatter}
 *
 * @author Colin But
 */
@RunWith(MockitoJUnitRunner.class)
public class PetTypeFormatterTests {

    @Mock
    private PetRepository pets;

    private PetTypeFormatter petTypeFormatter;

    @Before
    public void setup() {
        this.petTypeFormatter = new PetTypeFormatter(pets);
    }

    @Test
    public void testPrint() {
        PetType petType = new PetType();
        petType.setName("Hamster");
        String petTypeName = this.petTypeFormatter.print(petType, Locale.ENGLISH);
        assertEquals("Hamster", petTypeName);
    }

    @Test
    public void shouldParse() throws ParseException {
        Mockito.when(this.pets.findPetTypes()).thenReturn(makePetTypes());
        PetType petType = petTypeFormatter.parse("Bird", Locale.ENGLISH);
        assertEquals("Bird", petType.getName());
    }

    @Test(expected = ParseException.class)
    public void shouldThrowParseException() throws ParseException {
        Mockito.when(this.pets.findPetTypes()).thenReturn(makePetTypes());
        petTypeFormatter.parse("Fish", Locale.ENGLISH);
    }

    /**
     * Helper method to produce some sample pet types just for test purpose
     *
     * @return {@link Collection} of {@link PetType}
     */
    private List<PetType> makePetTypes() {
        List<PetType> petTypes = new ArrayList<>();
        petTypes.add(new PetType() {
            {
                setName("Dog");
            }
        });
        petTypes.add(new PetType() {
            {
                setName("Bird");
            }
        });
        return petTypes;
    }

}
