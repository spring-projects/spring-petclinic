package org.springframework.samples.petclinic.web;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/business-config.xml", "classpath:spring/tools-config.xml", "classpath:spring/mvc-core-config.xml"})
@WebAppConfiguration
@ActiveProfiles("spring-data-jpa")

public abstract class AbstractWebResourceTests {

	protected MockMvc mockMvc;
	
	public void runMockSpringMVC(Object resource) {
        this.mockMvc = MockMvcBuilders.standaloneSetup(resource).build();
    }

	public AbstractWebResourceTests() {
		super();
	}

}