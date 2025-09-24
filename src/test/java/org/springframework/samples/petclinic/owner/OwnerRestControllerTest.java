package org.springframework.samples.petclinic.owner;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OwnerRestController.class)
public class OwnerRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OwnerService ownerService;

    @Test
    void testGetOwnerCountEndpoint() throws Exception {
        when(ownerService.getOwnerCount()).thenReturn(10L);

        mockMvc.perform(get("/api/owners/count"))
               .andExpect(status().isOk())
               .andExpect(content().string("10"));

        verify(ownerService, times(1)).getOwnerCount();
    }
}
