package ucll.be.unit.model;
import ucll.be.model.Books;
import org.junit.jupiter.api.Test;
import ucll.be.model.RegisterMagazine;

import static org.junit.jupiter.api.Assertions.*;

public class BooksTest {
    @Test
    public void rightTest(){
        Books books = new Books("Lord of the rings",1954, 8,"John Ronald Reuel Tolkien","978-0-545-01022-1");

        assertEquals("Lord of the rings", books.getTitle());
        assertEquals("John Ronald Reuel Tolkien", books.getAuthor());
        assertEquals("978-0-545-01022-1", books.getISBN());
        assertEquals(1954, books.getPublicationYear());
        assertEquals(8, books.getAvailableCopies());
    }

    @Test
    public void wrongTitle(){
        RuntimeException exception = assertThrows(RuntimeException.class,() ->{
            new Books("",1954, 8,"John Ronald Reuel Tolkien","978-0-545-01022-1");
        });
        assertEquals("Title is required.", exception.getMessage());
    }

    @Test
    public void wrongAuthor(){
        RuntimeException exception = assertThrows(RuntimeException.class, () ->{
            new Books("Lord of the rings",1954,8,"","978-0-545-01022-1");
        });
        assertEquals("Author can't be empty.", exception.getMessage());
    }

    @Test
    public void wrongIsbn(){
        RuntimeException exception = assertThrows(RuntimeException.class, () ->{
            new Books("Lord of the rings",1954,8,"John Ronald Reuele Tolkien","978-545-01022");
        });
        assertEquals("ISBN can't be empty and must contain exactly 13 digits.", exception.getMessage());
    }

    @Test
    public void emptyIsbn(){
        RuntimeException exception = assertThrows(RuntimeException.class, () ->{
            new Books("Lord of the rings",1954,8,"John Ronald Reuele Tolkien", "");
        });
        assertEquals("ISBN can't be empty and must contain exactly 13 digits.", exception.getMessage());
    }

    @Test
    public void wrongPublicationYear(){
        RuntimeException exception = assertThrows(RuntimeException.class, () ->{
            new Books("Lord of the rings", 2026,8,"John Roland Reuele Tolkien","978-0-545-01022-1");
        });
        assertEquals("Publication year must be a positive integer and can't be in the future.", exception.getMessage());
    }

    @Test
    public void negativeYear(){
        RuntimeException exception = assertThrows(RuntimeException.class, () ->{
            new Books("Lord of the rings", -1,8, "John Roland Reuele Tolkien", "978-0-545-01022-1");
        });
        assertEquals("Publication year must be a positive integer and can't be in the future.", exception.getMessage());
    }

    @Test
    public void invalidCopies(){
        RuntimeException exception = assertThrows(RuntimeException.class, () ->{
            new RegisterMagazine("De Tijd",1968,-5,"Peter De Groote","2033-6950");
        });
        assertEquals("Number of available copies must be a positive integer.", exception.getMessage());
    }
}
