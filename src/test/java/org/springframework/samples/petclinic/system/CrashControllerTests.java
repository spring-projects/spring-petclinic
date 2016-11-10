package org.springframework.samples.petclinic.system;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.PetClinicApplication;
import org.springframework.samples.petclinic.system.CrashController;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for {@link CrashController}
 *
 * @author Colin But
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PetClinicApplication.class)
@WebAppConfiguration
// Waiting https://github.com/spring-projects/spring-boot/issues/5574
@Ignore
public class CrashControllerTests {

    @Autowired
    private CrashController crashController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders
            .standaloneSetup(crashController)
            //.setHandlerExceptionResolvers(new SimpleMappingExceptionResolver())
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
