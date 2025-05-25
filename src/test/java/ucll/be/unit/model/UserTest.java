package ucll.be.unit.model;

import ucll.be.model.Profile;
import ucll.be.model.User;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class UserTest{
    @Test
    public void giveValidValues_whenCreatingUser_thenUserIsCreatedWhitThoseValues(){
        Profile profile = new Profile("Arsonist","Brussels, Belgium","Fire");
        User user = new User("John Doe", "john.doe@ucll.be", "john1234", 56,profile);

        assertEquals("John Doe", user.getName());
        assertEquals(56, user.getAge());
        assertEquals("john.doe@ucll.be", user.getEmail());
        assertEquals("john1234", user.getPassword());
    }

    @Test
    public void testInvalidName(){
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            Profile profile = new Profile("Arsonist","Brussels, Belgium","Fire");
            new User("", "john.doe@ucll.be","john1234" , 56,profile);
        });
        assertEquals("Name can not be null or empty.", exception.getMessage());
    }

    @Test
    public void testInvalidEmail(){
        RuntimeException exception = assertThrows(RuntimeException.class, () ->{
            Profile profile = new Profile("Arsonist","Brussels, Belgium","Fire");
            new User("John Doe","john.doeucll.be","john1234",56 ,profile);
        });
        assertEquals("Enter valid email.", exception.getMessage());
    }

    @Test
    public void testInvalidAge(){
        RuntimeException exception = assertThrows(RuntimeException.class, () ->{
            Profile profile = new Profile("Arsonist","Brussels, Belgium","Fire");
            new User("John Doe", "john.doe@example.com","password123" ,-1,profile );
        });
        assertEquals("Enter valid age between 0 and 101.", exception.getMessage());
    }

    @Test
    public void testInvalidHigherAge(){
        RuntimeException exception = assertThrows(RuntimeException.class, () ->{
            Profile profile = new Profile("Arsonist","Brussels, Belgium","Fire");
            new User("John Doe", "john.doe@example.com","password123",102,profile);
        });
        assertEquals("Enter valid age between 0 and 101.", exception.getMessage());
    }

    @Test
    public void testInvalidPassword(){
        RuntimeException exception = assertThrows(RuntimeException.class, () ->{
            Profile profile = new Profile("Arsonist","Brussels, Belgium","Fire");
            new User("John Doe","john.doe@example.com", "",56,profile);
        });
        assertEquals("Invalid password.", exception.getMessage());
    }
}
