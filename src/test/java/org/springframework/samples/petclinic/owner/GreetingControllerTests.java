@SpringBootTest
@AutoConfigureMockMvc
public class GreetingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGreetWithName() throws Exception {
        mockMvc.perform(get("/api/greetings?name=Anu"))
            .andExpect(status().isOk())
            .andExpect(content().string("Welcome to the PetClinic, Anu!"));
    }

    @Test
    public void testGreetWithoutName() throws Exception {
        mockMvc.perform(get("/api/greetings"))
            .andExpect(status().isOk())
            .andExpect(content().string("Welcome to the PetClinic!"));
    }
}
