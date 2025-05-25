package ucll.be.unit.model.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ucll.be.model.Profile;
import ucll.be.model.User;
import ucll.be.repository.UserRepository;
import ucll.be.service.UserService;
import ucll.be.repository.LoanRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private UserService userService;

    private User user1,user2,user3;

    @BeforeEach
    void setup(){
        Profile profile1 = new Profile("Siting","Home","procrastination");
        Profile profile2 = new Profile("sleeping","Bed","Skydiving in my dreams");
        Profile profile3 = new Profile("running","streets","running away from responsibility");
        user1 = new UserTestImpl("Jane Doe", 30, "jane@example.com", "password1",profile1);
        user2 = new UserTestImpl("John Doe", 28, "john@example.com", "password2",profile2);
        user3 = new UserTestImpl("Alice Smith", 22, "alice@example.com", "password3",profile3);
    }

    @Test
    void testMockUserRepositoryReturnUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user1, user2, user3));
        List<User> users = userRepository.findAll();
        assertNotNull(users);
        assertEquals(3, users.size());
    }

    @Test
    public void testGetUsersByName_PartialMatch_Success() {
       when(userRepository.findByNameContaining("Doe")).thenReturn(List.of(user1,user2));

       List<User> result = userService.getUsersByName("Doe");

        assertEquals(2, result.size());
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));
        assertFalse(result.contains(user3));

        verify(userRepository).findByNameContaining("Doe");
    }

    @Test
    public void testGetUsersByName_NoMatch() {
        when(userRepository.findByNameContaining("Smith")).thenReturn(Collections.emptyList());
        List<User> result = userService.getUsersByName("Smith");

        assertTrue(result.isEmpty());
        verify(userRepository).findByNameContaining("Smith");
    }

    @Test
    public void testGetUsersByName_EmptyName() {
        when(userRepository.findAll()).thenReturn(List.of(user1,user2));

        List<User> result = userService.getUsersByName("");

        assertEquals(2, result.size());
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));

        verify(userRepository).findAll();
    }
}

