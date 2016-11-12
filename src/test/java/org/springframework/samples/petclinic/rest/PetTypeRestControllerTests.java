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
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.ClinicServiceExt;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Test class for {@link PetTypeRestController}
 *
 * @author Vitaliy Fedoriv
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/mvc-test-config.xml", "classpath:spring/mvc-core-config.xml"})
@WebAppConfiguration
public class PetTypeRestControllerTests {

    @Autowired
    private PetTypeRestController petTypeRestController;

    @Autowired
    private ClinicServiceExt clinicService;

    private MockMvc mockMvc;

    private List<PetType> petTypes;

    @Before
    public void initPetTypes(){
    	this.mockMvc = MockMvcBuilders.standaloneSetup(petTypeRestController).build();
    	petTypes = new ArrayList<PetType>();

    	PetType petType = new PetType();
    	petType.setId(1);
    	petType.setName("cat");
    	petTypes.add(petType);
    	
    	petType = new PetType();
    	petType.setId(2);
    	petType.setName("dog");
    	petTypes.add(petType);
    	
    	petType = new PetType();
    	petType.setId(3);
    	petType.setName("lizard");
    	petTypes.add(petType);
    	
    	petType = new PetType();
    	petType.setId(4);
    	petType.setName("snake");
    	petTypes.add(petType);
    }
    
    @Test
    public void testGetPetTypeSuccess() throws Exception {
    	given(this.clinicService.findPetTypeById(1)).willReturn(petTypes.get(0));
        this.mockMvc.perform(get("/api/pettypes/1")
        	.accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("cat"));
    }
    
    @Test
    public void testGetPetTypeNotFound() throws Exception {
    	given(this.clinicService.findPetTypeById(-1)).willReturn(null);
        this.mockMvc.perform(get("/api/pettypes/-1")
        	.accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
    
    @Test
    public void testGetAllPetTypesSuccess() throws Exception {
    	petTypes.remove(0); 
    	petTypes.remove(1);
    	given(this.clinicService.findAllPetTypes()).willReturn(petTypes);
        this.mockMvc.perform(get("/api/pettypes/")
        	.accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
        	.andExpect(jsonPath("$.[0].id").value(2))
        	.andExpect(jsonPath("$.[0].name").value("dog"))
        	.andExpect(jsonPath("$.[1].id").value(4))
        	.andExpect(jsonPath("$.[1].name").value("snake"));
    }
    
    @Test
    public void testGetAllPetTypesNotFound() throws Exception {
    	petTypes.clear();
    	given(this.clinicService.findAllPetTypes()).willReturn(petTypes);
        this.mockMvc.perform(get("/api/pettypes/")
        	.accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
    
    @Test
    public void testCreatePetTypeSuccess() throws Exception {
    	PetType newPetType = petTypes.get(0);
    	newPetType.setId(999);
    	ObjectMapper mapper = new ObjectMapper();
    	String newPetTypeAsJSON = mapper.writeValueAsString(newPetType);
    	this.mockMvc.perform(post("/api/pettypes/")
    		.content(newPetTypeAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
    		.andExpect(status().isCreated());
    }
    
    @Test
    public void testCreatePetTypeError() throws Exception {
    	PetType newPetType = petTypes.get(0);
    	newPetType.setId(null);
    	newPetType.setName(null);
    	ObjectMapper mapper = new ObjectMapper();
    	String newPetTypeAsJSON = mapper.writeValueAsString(newPetType);
    	this.mockMvc.perform(post("/api/pettypes/")
        		.content(newPetTypeAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        		.andExpect(status().isBadRequest());
     }
    
    @Test
    public void testUpdatePetTypeSuccess() throws Exception {
    	given(this.clinicService.findPetTypeById(2)).willReturn(petTypes.get(1));
    	PetType newPetType = petTypes.get(1);
    	newPetType.setName("dog I");
    	ObjectMapper mapper = new ObjectMapper();
    	String newPetTypeAsJSON = mapper.writeValueAsString(newPetType);
    	this.mockMvc.perform(put("/api/pettypes/2")
    		.content(newPetTypeAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        	.andExpect(content().contentType("application/json;charset=UTF-8"))
        	.andExpect(status().isNoContent());
    	
    	this.mockMvc.perform(get("/api/pettypes/2")
           	.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.id").value(2))
            .andExpect(jsonPath("$.name").value("dog I"));	
    }
    
    @Test
    public void testUpdatePetTypeError() throws Exception {
    	PetType newPetType = petTypes.get(0);
    	newPetType.setName("");
    	ObjectMapper mapper = new ObjectMapper();
    	String newPetTypeAsJSON = mapper.writeValueAsString(newPetType);
    	this.mockMvc.perform(put("/api/pettypes/1")
    		.content(newPetTypeAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        	.andExpect(status().isBadRequest());
     }
    
    @Test
    public void testDeletePetTypeSuccess() throws Exception {
    	PetType newPetType = petTypes.get(0);
    	ObjectMapper mapper = new ObjectMapper();
    	String newPetTypeAsJSON = mapper.writeValueAsString(newPetType);
    	given(this.clinicService.findPetTypeById(1)).willReturn(petTypes.get(0));
    	this.mockMvc.perform(delete("/api/pettypes/1")
    		.content(newPetTypeAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        	.andExpect(status().isNoContent());
    }
    
    @Test
    public void testDeletePetTypeError() throws Exception {
    	PetType newPetType = petTypes.get(0);
    	ObjectMapper mapper = new ObjectMapper();
    	String newPetTypeAsJSON = mapper.writeValueAsString(newPetType);
    	given(this.clinicService.findPetTypeById(-1)).willReturn(null);
    	this.mockMvc.perform(delete("/api/pettypes/-1")
    		.content(newPetTypeAsJSON).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        	.andExpect(status().isNotFound());
    }

}