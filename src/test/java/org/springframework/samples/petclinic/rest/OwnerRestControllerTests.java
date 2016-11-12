/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicServiceExt;
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
    private ClinicServiceExt clinicService;

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
        	.accept(MediaType.APPLICATION_JSON_VALUE))
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
        this.mockMvc.perform(get("/api/owners/*/lastname/Davis")
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
    
    @Test
    public void testGetAllOwnersSuccess() throws Exception {
    	owners.remove(0); 
    	owners.remove(1);
    	given(this.clinicService.findAllOwners()).willReturn(owners);
        this.mockMvc.perform(get("/api/owners/")
        	.accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.[0].id").value(2))
            .andExpect(jsonPath("$.[0].firstName").value("Betty"))
            .andExpect(jsonPath("$.[1].id").value(4))
            .andExpect(jsonPath("$.[1].firstName").value("Harold"));
    }
    
    @Test
    public void testGetAllOwnersNotFound() throws Exception {
    	owners.clear();
    	given(this.clinicService.findAllOwners()).willReturn(owners);
        this.mockMvc.perform(get("/api/owners/")
        	.accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
    
    @Test
    public void testCreateOwnerSuccess() throws Exception {
    	Owner newOwner = owners.get(0);
    	newOwner.setId(999);
    	ObjectMapper mapper = new ObjectMapper();
    	String newOwnerAsJSON = mapper.writeValueAsString(newOwner);
    	this.mockMvc.perform(post("/api/owners/")
    		.content(newOwnerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
    		.andExpect(status().isCreated());
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
    
    @Test
    public void testUpdateOwnerSuccess() throws Exception {
    	Owner newOwner = owners.get(0);
    	newOwner.setFirstName("George I");
    	ObjectMapper mapper = new ObjectMapper();
    	String newOwnerAsJSON = mapper.writeValueAsString(newOwner);
    	this.mockMvc.perform(put("/api/owners/1")
    		.content(newOwnerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        	.andExpect(content().contentType("application/json;charset=UTF-8"))
        	.andExpect(status().isNoContent());
    	
    	this.mockMvc.perform(get("/api/owners/1")
           	.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.firstName").value("George I"));
        	
    }
    
    @Test
    public void testUpdateOwnerError() throws Exception {
    	Owner newOwner = owners.get(0);
    	newOwner.setFirstName("");
    	ObjectMapper mapper = new ObjectMapper();
    	String newOwnerAsJSON = mapper.writeValueAsString(newOwner);
    	this.mockMvc.perform(put("/api/owners/1")
    		.content(newOwnerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        	.andExpect(status().isBadRequest());
     }
    
    @Test
    public void testDeleteOwnerSuccess() throws Exception {
    	Owner newOwner = owners.get(0);
    	ObjectMapper mapper = new ObjectMapper();
    	String newOwnerAsJSON = mapper.writeValueAsString(newOwner);
    	given(this.clinicService.findOwnerById(1)).willReturn(owners.get(0));
    	this.mockMvc.perform(delete("/api/owners/1")
    		.content(newOwnerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        	.andExpect(status().isNoContent());
    }
    
    @Test
    public void testDeleteOwnerError() throws Exception {
    	Owner newOwner = owners.get(0);
    	ObjectMapper mapper = new ObjectMapper();
    	String newOwnerAsJSON = mapper.writeValueAsString(newOwner);
    	given(this.clinicService.findOwnerById(-1)).willReturn(null);
    	this.mockMvc.perform(delete("/api/owners/-1")
    		.content(newOwnerAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        	.andExpect(status().isNotFound());
    }

}
