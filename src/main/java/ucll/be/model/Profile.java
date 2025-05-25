package ucll.be.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Bio is required.")
    private String bio;

    @NotBlank(message = "Location is required.")
    private String location;

    @NotBlank(message = "Interest are required.")
    private String interest;

    @OneToOne(mappedBy = "profile")
    private User user1;

    public Profile(){}

    public Profile(String bio, String location, String interests) {
        this.bio = bio;
        this.location = location;
        this.interest = interests;
    }

    public void setUser(User user1) {
        this.user1 = user1;
    }

    public String getInterest() {
        return interest;
    }

    public String getLocation() {
        return location;
    }

    public String getBio() {
        return bio;
    }

    public User getUser(){
        return user1;
    }
}
