package ucll.be.model;

import java.time.Year;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "publication_type")
public abstract class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String title;
    private int publicationYear;
    private int availableCopies;

    public Publication(){}

    public Publication(String title, int publicationYear, int available){
        setTitle(title);
        setPublicationYear(publicationYear);
        setAvailableCopies(available);
    }

    public  Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setTitle(String title){
        if (title != null && !title.trim().isEmpty()){
            this.title = title;
        }else{throw new RuntimeException("Title is required.");}
    }

    public String getTitle(){
        return title;
    }

    public void setPublicationYear(int publicationYear){
        int currentYear = Year.now().getValue();
        if (publicationYear <= currentYear && publicationYear > 0){
            this.publicationYear = publicationYear;
        }else {throw new RuntimeException(("Publication year must be a positive integer and can't be in the future."));}
    }

    public int getPublicationYear(){
        return publicationYear;
    }

    public void setAvailableCopies(int availableCopies){
        if (availableCopies >= 0){
            this.availableCopies = availableCopies;
        }else {throw new RuntimeException("Number of available copies must be a positive integer.");}
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void LendPublication(){
        if (availableCopies <= 0){
            throw new RuntimeException("No available copies: " + title);
        }
        availableCopies--;
    }

    public void returnPublication(){
        availableCopies++;
    }

    @Override
    public String toString(){
        return "Publication{" + "title: " + title + ", " + "publication year: " + publicationYear + ", available: " + availableCopies + "}";
    }

    public abstract String getType();
}
