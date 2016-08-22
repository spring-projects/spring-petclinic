package org.springframework.samples.petclinic.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.config.MvcCoreConfig;
import org.springframework.samples.petclinic.config.MvcTestConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.HandlerExceptionResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for {@link CrashController}
 *
 * @author Colin But
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { MvcCoreConfig.class, MvcTestConfig.class })
public class CrashControllerTests {

    @Autowired
    private CrashController crashController;

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders
            .standaloneSetup(crashController)
            .setHandlerExceptionResolvers(handlerExceptionResolver)
            .build();
    }

    @Test
    public void testTriggerException() throws Exception {
        mockMvc.perform(get("/oups"))
            .andExpect(view().name("exception"))
            .andExpect(model().attributeExists("exception"))
            .andExpect(forwardedUrl("exception"))
            .andExpect(status().isOk());
    }
}
