package org.springframework.samples.petclinic.rest;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Test class for {@link OwnerRestController}
 *
 * @author Vitaliy Fedoriv
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/mvc-test-config.xml", "classpath:spring/mvc-core-config.xml"})
@WebAppConfiguration
public class OwnerRestControllerTests {

    @Autowired
    private OwnerRestController ownerRestController;

    @Autowired
    private ClinicService clinicService;

    private MockMvc mockMvc;

    private List<Owner> owners;

    @Before
    public void initOwners(){
    	this.mockMvc = MockMvcBuilders.standaloneSetup(ownerRestController).build();
    	owners = new ArrayList<Owner>();

    	Owner owner = new Owner();
    	owner.setId(1);
    	owner.setFirstName("George");
    	owner.setLastName("Franklin");
    	owner.setAddress("110 W. Liberty St.");
    	owner.setCity("Madison");
    	owner.setTelephone("6085551023");
    	owners.add(owner);
    	
    	owner = new Owner();
    	owner.setId(2);
    	owner.setFirstName("Betty");
    	owner.setLastName("Davis");
    	owner.setAddress("638 Cardinal Ave.");
    	owner.setCity("Sun Prairie");
    	owner.setTelephone("6085551749");
    	owners.add(owner);
    	
    	owner = new Owner();
    	owner.setId(3);
    	owner.setFirstName("Eduardo");
    	owner.setLastName("Rodriquez");
    	owner.setAddress("2693 Commerce St.");
    	owner.setCity("McFarland");
    	owner.setTelephone("6085558763");
    	owners.add(owner);
    	
    	owner = new Owner();
    	owner.setId(4);
    	owner.setFirstName("Harold");
    	owner.setLastName("Davis");
    	owner.setAddress("563 Friendly St.");
    	owner.setCity("Windsor");
    	owner.setTelephone("6085553198");
    	owners.add(owner);
    	
    	
    }
    
    @Test
    public void testGetOwnerSuccess() throws Exception {
    	given(this.clinicService.findOwnerById(1)).willReturn(owners.get(0));
        this.mockMvc.perform(get("/api/owners/1")
        	.accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.firstName").value("George"));
    }
    
    @Test
    public void testGetOwnerNotFound() throws Exception {
    	given(this.clinicService.findOwnerById(-1)).willReturn(null);
        this.mockMvc.perform(get("/api/owners/-1")
        	.accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
    
    @Test
    public void testGetOwnersListSuccess() throws Exception {
    	owners.remove(0); 
    	owners.remove(1);
    	given(this.clinicService.findOwnerByLastName("Davis")).willReturn(owners);
        this.mockMvc.perform(get("/api/owners/?lastName=Davis")
        	.accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.[0].id").value(2))
            .andExpect(jsonPath("$.[0].firstName").value("Betty"))
            .andExpect(jsonPath("$.[1].id").value(4))
            .andExpect(jsonPath("$.[1].firstName").value("Harold"));
    }
    
    @Test
    public void testGetOwnersListNotFound() throws Exception {
    	owners.clear();
    	given(this.clinicService.findOwnerByLastName("0")).willReturn(owners);
        this.mockMvc.perform(get("/api/owners/?lastName=0")
        	.accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
    
    @Ignore
    @Test
    public void testCreateOwnerSuccess() throws Exception {
    	doAnswer(new Answer<Void>() {
    	     public Void answer(InvocationOnMock invocation) {
    	         Owner resultOwner = (Owner) invocation.getArguments()[0];
    	         resultOwner.setId(999);
    	         System.out.println("resultOwner after call " + resultOwner.toString());
    	         return null;
    	     }
    	}).when(clinicService).saveOwner((Owner) anyObject());
    	
    	Owner newOwner = owners.get(0);
    	newOwner.setId(null);
    	
    	ObjectMapper mapper = new ObjectMapper();
    	String newOwnerAsJSON = mapper.writeValueAsString(newOwner);
    	System.out.println("newOwnerAsJSON" + newOwnerAsJSON);
    	newOwner.setId(999);
    	//newOwner.as;
    	String resultOwnerAsJSON = mapper.writeValueAsString(newOwner);
    	System.out.println("resultOwnerAsJSON" + resultOwnerAsJSON);
// TODO  Uncompleted test
    	this.mockMvc.perform(post("/api/owners/")
    		.content(newOwnerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
    		.andExpect(status().isCreated())
    		.andExpect(content().json(resultOwnerAsJSON));
    }
    
    @Test
    public void testCreateOwnerError() throws Exception {
    	Owner newOwner = owners.get(0);
    	newOwner.setId(null);
    	newOwner.setFirstName(null);
    	ObjectMapper mapper = new ObjectMapper();
    	String newOwnerAsJSON = mapper.writeValueAsString(newOwner);
    	this.mockMvc.perform(post("/api/owners/")
        		.content(newOwnerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        		.andExpect(status().isBadRequest());
     }

}
