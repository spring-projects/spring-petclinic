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

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author Arjen Poutsma 
 * @author Michael Isvy
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
// Spring configuration files that are inside WEB-INF folder can be referenced here because they've been 
// added to the classpath inside the Maven pom.xml file (inside <build> <testResources> ... </testResources> </build>)
@ContextConfiguration("VisitsAtomViewTestWithContainer-config.xml")
@ActiveProfiles("jdbc")
public class VisitsAtomViewWithContainerTest {

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;
	
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}

	@Test
	public void getVisits() throws Exception { 
		MediaType mediaType = MediaType.APPLICATION_ATOM_XML;
		ResultActions actions = this.mockMvc.perform(get("/vets.atom"));
		actions.andExpect(status().isOk());
		actions.andExpect(xpath("//*").string(containsString("Pet ClinicService Visits")));
		actions.andExpect(content().contentType("application/atom+xml"));
		
	}
}
