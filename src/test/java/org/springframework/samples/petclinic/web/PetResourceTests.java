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
public class PetResourceTests extends AbstractWebResourceTests {

    @Autowired
    private PetResource petResource;
    
    @Before
    public void setup() {
    	runMockSpringMVC(petResource);
    }

    /**
     * Expected JSon result:
     * {  
		   "id":2,
		   "name":"Basil",
		   "birthDate":1344211200000,
		   "type":{  
		      "id":6,
		      "name":"hamster",
		      "new":false
		   },
		   "visits":[],
		   "new":false
		}
     */
    @Test
    public void shouldGetAPetInJSonFormat() throws Exception {
    	ResultActions actions = mockMvc.perform(get("/owner/2/pet/2.json").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    	actions.andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Basil"))
                .andExpect(jsonPath("$.type.id").value(6)); 
    }
}
