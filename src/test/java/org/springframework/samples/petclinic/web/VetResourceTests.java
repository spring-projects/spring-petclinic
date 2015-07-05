package org.springframework.samples.petclinic.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserResource
 */
public class VetResourceTests extends AbstractWebResourceTests {

    @Autowired
    private VetResource vetResource;
    
    @Before
    public void setup() {
    	runMockSpringMVC(vetResource);
    }

    @Test
    public void shouldGetAListOfVetsInJSonFormat() throws Exception {
    	ResultActions actions = mockMvc.perform(get("/vets.json").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    	actions.andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].id").value(1)); 
    	//before when collection was nested inside parent object 'vetList', we had: $.vetList[0].id
    }
}
