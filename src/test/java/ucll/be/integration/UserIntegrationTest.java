package ucll.be.integration;

import ucll.be.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ucll.be.model.Profile;
import ucll.be.model.User;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddAndGetUser() throws Exception{
        Profile profile = new Profile("Like flowers","Ven","clay");
        User user = new User("Test User","test@email.com","Pass123!", 24,profile);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        String response = mockMvc.perform(get("/users/by-email").param("email", "test@email.com"))
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString();

        User fetched = objectMapper.readValue(response, User.class);
        assertThat(fetched.getEmail()).isEqualTo("test@email.com");
        assertThat(fetched.getName()).isEqualTo("Test User");
    }

    @Test
    public void testGetAllAdultUsers() throws Exception{
        String response = mockMvc.perform(get("/users/adults/"))
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString();

        List<User> users = objectMapper.readValue(response, objectMapper.getTypeFactory()
                .constructCollectionType(List.class, User.class));
        assertThat(users).isNotEmpty();
        assertThat(users.get(0).getAge()).isGreaterThanOrEqualTo(18);
    }

    @Test
    public void testDeleteNonExistentUser() throws Exception{
        mockMvc.perform(delete("/users/nonexistent@email.com"))
                .andExpect(status().isInternalServerError()).andExpect(content()
                .string("User does not exist."));
    }

    @Test
    public void testAddGetUpdateDeleteUser() throws Exception{
        Profile profile = new Profile("Like flowers","Ven","clay");
        User user = new User("John Doe","john@email.com","password123", 25,profile);
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(userJson).contentType(MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/users/jogn@email.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@email.com"));

        user.setName("John Updated");
        String updatedJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(put("/users/john@email.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson)).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Updated"));

        mockMvc.perform(delete("/users/john@email.com")).andExpect(status().isOk())
                .andExpect(content().string("User successfully deleted"));
    }
}

