import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(GreetingController.class)
class GreetingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnDefaultGreetingWhenNameIsNotProvided() throws Exception {
        mockMvc.perform(get("/api/greetings"))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome to the PetClinic!"));
    }

    @Test
    void shouldReturnPersonalizedGreetingWhenNameIsProvided() throws Exception {
        mockMvc.perform(get("/api/greetings").param("name", "John"))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome to the PetClinic, John!"));
    }
}