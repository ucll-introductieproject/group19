package ucll.be.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import ucll.be.controller.Autowired;
import ucll.be.model.User;
import ucll.be.model.Profile;
import ucll.be.repository.UserRepository;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserInterestIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup(){
        userRepository.deleteAll();

        Profile profile1 = new Profile("Loves science", "Leuven", "Science, reading, cooking");
        User user1 = new User("Alice","alice@example.com","password123",25, profile1);

        Profile profile2 = new Profile("Enjoys movies", "Brussels", "Movies, gaming");
        User user2 = new User("Bob","bob@example.com", "password456",30, profile2);

        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Test
    public void testGetUsersByInterest_success() throws Exception{
        mockMvc.perform(get("/users/interest/reading"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("alice@example.com"))
                .andExpect(jsonPath("$[0].profile.interest")
                .value("science, reading, cooking"));
    }

    @Test
    public void testGetUsersByInterest_noMatch() throws Exception{
        mockMvc.perform(get("/users/interest/knitting")).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("No users found with interest in knitting"));
    }

    @Test
    public void testGetUsersByInterest_emptyInterest()throws Exception{
        mockMvc.perform(get("/users/interest/"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Interest cannot be empty."));
    }

    @Test
    void getUsersByInterestAndMinAgeSorted_success()throws Exception{
        mockMvc.perform(get("/users/interest/reading/25"))
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].profile.location").exists());
    }

    @Test
    void getUserByInterestAndMinAgeSorted_emptyInterest()throws Exception{
        mockMvc.perform(get("/users/interest/ /25"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.interest")
                        .value("interest cannot be empty."));
    }

    @Test void getUserByInterestAndAgeSorted_invalidAge() throws Exception{
        mockMvc.perform(get("/users/interest/reading/200"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.age")
                        .value("Invalid age. Age must be between 0 and 150."));
    }

    @Test
    void getUsersByInterestAndMinAgeSorted_noUsersFound() throws Exception{
        mockMvc.perform(get("/users/interest/dancing/99"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("No users found with interest in dancing and older than 99"));
    }
}
