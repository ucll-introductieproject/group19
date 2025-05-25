package ucll.be.model;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "´user´")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private int age;
    private static int idCounter = 1;
    @NotBlank
    private String email;
    private String name;
    private String userName;
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    public Profile profile;

    @ElementCollection
    private List<String>borrowed = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Membership> membership;
    public User(){}

    public User(String name, String email, String password, int age, Profile profile){
        setAge(age);
        setName(name);
        setEmail(email);
        setPassword(password);
        setProfile(profile);
        this.id = idCounter++;
    }

    public int getId(){
        return id;
    }

    public void updateInfo(User update){
        if(!this.email.equals(update.getEmail())){
            throw new RuntimeException("Email cannot be changed. ");
        }
        setName(update.getName());
        setEmail(update.getEmail());
        setAge(update.getAge());
        setPassword(update.getPassword());
    }

    public void setPassword(String password){
        if (password != null && password.length() >= 8){
            this.password = password;
        }else {throw new RuntimeException("Invalid password.");}
    }

    public void setName(String name){
        if ((name != null) && (!name.trim().isEmpty())) {
            this.name = name;
        }  else {throw new RuntimeException("Name can not be null or empty.");}
    }

    public void setAge(int age){
        if (101 >= age && age >= 0) {
            this.age = age;
        }   else {throw new RuntimeException("Enter valid age between 0 and 101.");}
    }

    public void setEmail(String email){
        if (email.contains("@") && email.contains(".") && email.indexOf("@") < email.lastIndexOf(".")){
            this.email = email;
        }else{throw new RuntimeException ("Enter valid email.");}
    }

    public void setUserName(String userName){
        if (userName != null && !userName.trim().isEmpty()){
            this.userName = userName;
        }else {throw new RuntimeException("Username cannot be empty.");}
    }

    public void borrowPublication(Publication publication){
        if(publication.getAvailableCopies() > 0){
            publication.LendPublication();
            borrowed.add(String.valueOf(publication));
        }else {
            throw new RuntimeException("No available copies for: "+ publication.getTitle());
        }
    }

    public void returnPublication(Publication publication){
        if (borrowed.contains(publication)){
            publication.returnPublication();
            borrowed.remove(publication);
        }else { throw new RuntimeException("User has not borrowed this article.");
        }
    }

    public String getName(){
        return name;
    }

    public int getAge(){
        return age;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public String getUserName(){
        return userName;
    }

    public List<String> getBorrowed(){return borrowed;}

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Collection<Membership> getMemberships() {
        return membership;
    }

    public void setMembership(Collection<Membership> membership) {
        this.membership = membership;
    }
}
