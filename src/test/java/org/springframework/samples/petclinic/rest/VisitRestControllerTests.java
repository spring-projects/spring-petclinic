package org.springframework.samples.petclinic.rest;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.ClinicServiceExt;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Test class for {@link VisitRestController}
 *
 * @author Vitaliy Fedoriv
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/mvc-test-config.xml", "classpath:spring/mvc-core-config.xml"})
@WebAppConfiguration
public class VisitRestControllerTests {

    @Autowired
    private VisitRestController visitRestController;

    @Autowired
    private ClinicServiceExt clinicService;

    private MockMvc mockMvc;

    private List<Visit> visits;

    @Before
    public void initVisits(){
    	this.mockMvc = MockMvcBuilders.standaloneSetup(visitRestController).build();
    	visits = new ArrayList<Visit>();
    	
    	Owner owner = new Owner();
    	owner.setId(1);
    	owner.setFirstName("Eduardo");
    	owner.setLastName("Rodriquez");
    	owner.setAddress("2693 Commerce St.");
    	owner.setCity("McFarland");
    	owner.setTelephone("6085558763");
    	
    	PetType petType = new PetType();
    	petType.setId(2);
    	petType.setName("dog");
    	
    	Pet pet = new Pet();
    	pet.setId(8);
    	pet.setName("Rosy");
    	pet.setBirthDate(new Date());
    	pet.setOwner(owner);
    	pet.setType(petType);
    	

    	Visit visit = new Visit();
    	visit.setId(2);
    	visit.setPet(pet);
    	visit.setDate(new Date());
    	visit.setDescription("rabies shot");
    	visits.add(visit);
    	
    	visit = new Visit();
    	visit.setId(3);
    	visit.setPet(pet);
    	visit.setDate(new Date());
    	visit.setDescription("neutered");
    	visits.add(visit);
    	

    }
    
    @Test
    public void testGetVisitSuccess() throws Exception {
    	given(this.clinicService.findVisitById(2)).willReturn(visits.get(0));
        this.mockMvc.perform(get("/api/visits/2")
        	.accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.id").value(2))
            .andExpect(jsonPath("$.description").value("rabies shot"));
    }
    
    @Test
    public void testGetVisitNotFound() throws Exception {
    	given(this.clinicService.findVisitById(-1)).willReturn(null);
        this.mockMvc.perform(get("/api/visits/-1")
        	.accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
    
    @Test
    public void testGetAllVisitsSuccess() throws Exception {
    	given(this.clinicService.findAllVisits()).willReturn(visits);
        this.mockMvc.perform(get("/api/visits/")
        	.accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
        	.andExpect(jsonPath("$.[0].id").value(2))
        	.andExpect(jsonPath("$.[0].description").value("rabies shot"))
        	.andExpect(jsonPath("$.[1].id").value(3))
        	.andExpect(jsonPath("$.[1].description").value("neutered"));
    }
    
    @Test
    public void testGetAllVisitsNotFound() throws Exception {
    	visits.clear();
    	given(this.clinicService.findAllVisits()).willReturn(visits);
        this.mockMvc.perform(get("/api/visits/")
        	.accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
    
    @Test
    public void testCreateVisitSuccess() throws Exception {
    	Visit newVisit = visits.get(0);
    	newVisit.setId(999);
    	ObjectMapper mapper = new ObjectMapper();
    	String newVisitAsJSON = mapper.writeValueAsString(newVisit);
    	this.mockMvc.perform(post("/api/visits/")
    		.content(newVisitAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
    		.andExpect(status().isCreated());
    }
    
    @Test
    public void testCreateVisitError() throws Exception {
    	Visit newVisit = visits.get(0);
    	newVisit.setId(null);
    	newVisit.setPet(null);
    	ObjectMapper mapper = new ObjectMapper();
    	String newVisitAsJSON = mapper.writeValueAsString(newVisit);
    	this.mockMvc.perform(post("/api/visits/")
        		.content(newVisitAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        		.andExpect(status().isBadRequest());
     }
    
    @Test
    public void testUpdateVisitSuccess() throws Exception {
    	given(this.clinicService.findVisitById(2)).willReturn(visits.get(0));
    	Visit newVisit = visits.get(0);
    	newVisit.setDescription("rabies shot test");
    	ObjectMapper mapper = new ObjectMapper();
    	String newVisitAsJSON = mapper.writeValueAsString(newVisit);
    	this.mockMvc.perform(put("/api/visits/2")
    		.content(newVisitAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        	.andExpect(content().contentType("application/json;charset=UTF-8"))
        	.andExpect(status().isNoContent());
    	
    	this.mockMvc.perform(get("/api/visits/2")
           	.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.id").value(2))
            .andExpect(jsonPath("$.description").value("rabies shot test"));	
    }
    
    @Test
    public void testUpdateVisitError() throws Exception {
    	Visit newVisit = visits.get(0);
    	newVisit.setPet(null);
    	ObjectMapper mapper = new ObjectMapper();
    	String newVisitAsJSON = mapper.writeValueAsString(newVisit);
    	this.mockMvc.perform(put("/api/visits/2")
    		.content(newVisitAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        	.andExpect(status().isBadRequest());
     }
    
    @Test
    public void testDeleteVisitSuccess() throws Exception {
    	Visit newVisit = visits.get(0);
    	ObjectMapper mapper = new ObjectMapper();
    	String newVisitAsJSON = mapper.writeValueAsString(newVisit);
    	given(this.clinicService.findVisitById(2)).willReturn(visits.get(0));
    	this.mockMvc.perform(delete("/api/visits/2")
    		.content(newVisitAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        	.andExpect(status().isNoContent());
    }
    
    @Test
    public void testDeleteVisitError() throws Exception {
    	Visit newVisit = visits.get(0);
    	ObjectMapper mapper = new ObjectMapper();
    	String newVisitAsJSON = mapper.writeValueAsString(newVisit);
    	given(this.clinicService.findVisitById(-1)).willReturn(null);
    	this.mockMvc.perform(delete("/api/visits/-1")
    		.content(newVisitAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        	.andExpect(status().isNotFound());
    }

}