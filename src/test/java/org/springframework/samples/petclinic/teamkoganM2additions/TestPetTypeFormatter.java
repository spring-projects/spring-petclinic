package org.springframework.samples.petclinic.teamkoganM2additions;

import org.junit.Test;
import org.springframework.samples.petclinic.owner.PetRepository;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.owner.PetTypeFormatter;

import static org.mockito.Mockito.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class TestPetTypeFormatter {

    @Test
    public void testPetTypeFormatter() {

        //create the mock PetRepository
        PetRepository mockPetRepository = mock(PetRepository.class);

        //create the mock PetType
        PetType mockPetType = mock(PetType.class);

        //call the class under test
        PetTypeFormatter PTF = new PetTypeFormatter(mockPetRepository);

        //stub the getName() method
        when(mockPetType.getName()).thenReturn("Testy");

        //test the print method
        String name = PTF.print(mockPetType, Locale.ENGLISH);

        //check the result is what we expected
        assertEquals("Testy", name);

        //verify the getName method was called
        verify(mockPetType).getName();

        //stub the findPetTypes method to return the PetTypes defined below
        when(mockPetRepository.findPetTypes()).thenReturn(PetTypes());

        //try catch block needed for ParseException that can be thrown when testing the parse method
        try {
            //test the parse method
            PetType testPetType = PTF.parse("Pupper", Locale.ENGLISH);

            //verify the getName method was called
            verify(mockPetType).getName();

            //check that the result is what is expected
            assertEquals("Pupper", testPetType.getName());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //define some pet types
    private List<PetType> PetTypes() {
        List<PetType> petTypes = new ArrayList<>();
        petTypes.add(new PetType() {
            { setName("Doggo"); }
        });
        petTypes.add(new PetType() {
            { setName("Pupper"); }
        });
        return petTypes;
    }

}
