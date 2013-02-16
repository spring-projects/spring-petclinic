/*
 * Copyright 2002-2009 the original author or authors.
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

package org.springframework.samples.petclinic.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*; 
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.web.context.WebApplicationContext;
import static org.hamcrest.Matchers.containsString;

import com.sun.syndication.feed.atom.Entry;
import com.sun.syndication.feed.atom.Feed;

/**
 * @author Arjen Poutsma 
 * @author Michael Isvy
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
// Spring configuration files that are inside WEB-INF folder can be referenced here because they've been 
// added to the classpath inside the Maven pom.xml file (inside <build> <testResources> ... </testResources> </build>)
@ContextConfiguration({"classpath*:mvc-*-config.xml", "classpath*:spring/*-config.xml"})
@ActiveProfiles("jdbc")
public class VisitsAtomViewTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;
	
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	private VisitsAtomView visitView;

	private Map<String, Object> model;

	private Feed feed;

	//@Test
	public void getVisits() throws Exception { 
		MediaType mediaType = MediaType.APPLICATION_ATOM_XML;
		ResultActions actions = this.mockMvc.perform(get("/owners/7/pets/9/visits.atom").accept(mediaType));
		actions.andExpect(status().isOk());
		actions.andExpect(content().contentType("application/atom+xml"));
		//actions.andExpect(content().xml("Pet ClinicService Visits"));
		actions.andExpect(xpath("//*").string(containsString("Pet ClinicService Visits")));
		
	}

	@Before
	public void setUp() {
		visitView = new VisitsAtomView();
		PetType dog = new PetType();
		dog.setName("dog");
		Pet bello = new Pet();
		bello.setName("Bello");
		bello.setType(dog);
		Visit belloVisit = new Visit();
		belloVisit.setPet(bello);
		belloVisit.setDate(new DateTime(2009, 1, 1, 1, 1));
		belloVisit.setDescription("Bello visit");
		Pet wodan = new Pet();
		wodan.setName("Wodan");
		wodan.setType(dog);
		Visit wodanVisit = new Visit();
		wodanVisit.setPet(wodan);
		wodanVisit.setDate(new DateTime(2009, 1, 2, 1, 1));
		wodanVisit.setDescription("Wodan visit");
		List<Visit> visits = new ArrayList<Visit>();
		visits.add(belloVisit);
		visits.add(wodanVisit);

		model = new HashMap<String, Object>();
		model.put("visits", visits);
		feed = new Feed();

	}


	@Test
	public void buildFeedMetadata() {
		visitView.buildFeedMetadata(model, feed, null);

		assertNotNull("No id set", feed.getId());
		assertNotNull("No title set", feed.getTitle());
		assertEquals("Invalid update set", new DateTime(2009, 1, 2, 1, 1).toDate(), feed.getUpdated());
	}

	@Test
	public void buildFeedEntries() throws Exception {
		List<Entry> entries = visitView.buildFeedEntries(model, null, null);
		assertEquals("Invalid amount of entries", 2, entries.size());
	}
}
