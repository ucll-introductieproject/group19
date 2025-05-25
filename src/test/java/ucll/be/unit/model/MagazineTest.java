package ucll.be.unit.model;
import ucll.be.model.RegisterMagazine;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class MagazineTest {
    @Test
    public void rightMagazine(){
        RegisterMagazine registerMagazine = new RegisterMagazine("De Tijd",1968,8, "Peter De Groote","2033-6950");

        assertEquals("De Tijd", registerMagazine.getTitle());
        assertEquals("Peter De Groote", registerMagazine.getEditor());
        assertEquals("2033-6950", registerMagazine.getISSN());
        assertEquals(1968, registerMagazine.getPublicationYear());
        assertEquals(8, registerMagazine.getAvailableCopies());
    }

    @Test
    public void wrongTitle(){
        RuntimeException exception = assertThrows(RuntimeException.class, () ->{
           new RegisterMagazine("",1968,8,"Peter De Groote","2033-6950");
        });
        assertEquals("Title is required.", exception.getMessage());
    }

    @Test
    public void wrongEditor(){
        RuntimeException exception = assertThrows(RuntimeException.class, () ->{
            new RegisterMagazine("De Tijd", 1968,8, "","2033-6950");
        });
        assertEquals("Editor is required and cannot be empty.", exception.getMessage());
    }

    @Test
    public void wrongISSN(){
        RuntimeException exception = assertThrows(RuntimeException.class, () ->{
            new RegisterMagazine("De Tijd", 1968,8, "Peter De Groote", "");
        });
        assertEquals("ISSN can't be empty and is required must be 8 numbers.", exception.getMessage());
    }

    @Test
    public void emptyISSN(){
        RuntimeException exception = assertThrows(RuntimeException.class, () ->{
            new RegisterMagazine("De Tijd", 1968,8, "Peter De Groote", "2033");
        });
        assertEquals("ISSN must be in the format xxxx-xxxx, where x is a digit.", exception.getMessage());
    }

    @Test
    public void futureYear(){
        RuntimeException exception = assertThrows(RuntimeException.class, () ->{
            new RegisterMagazine("De Tijd",2026,8, "Peter De Groote","2033-6950");
        });
        assertEquals("Publication year must be a positive integer and can't be in the future.", exception.getMessage());
    }

    @Test
    public void negativeYear(){
        RuntimeException exception = assertThrows(RuntimeException.class, () ->{
            new RegisterMagazine("De Tijd", -100,8, "Peter De Groote", "2033-6950");
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
