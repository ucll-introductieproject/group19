package ucll.be.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.*;

@Entity
@DiscriminatorValue("Loan")
public class RegisterLoan{
    private double price;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "´user_id´")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "´membership_id´")
    private Membership membership;

    @ManyToMany
    private List<Publication> publication;

    @NotNull
    private LocalDate startDate;
    private LocalDate endDate;

    public RegisterLoan(LocalDate startDate, List<Publication> publication, User user) {
        validateInputs(startDate,publication,user);
        this.user = user;
        this.publication = publication;
        this.startDate = startDate;
        this.endDate = startDate.plusDays(30);
        lendPublication();
    }

    public RegisterLoan(){}

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    private void validateInputs(LocalDate startDate, List<Publication> publication, User user){
        if (user == null){
            throw new RuntimeException("User is required.");
        }

        if (publication == null || publication.isEmpty()){
            throw new RuntimeException("List of publication is required.");
        }

        if (startDate == null || startDate.isAfter(LocalDate.now())){
            throw new RuntimeException("Start date is required and can't be in the future.");
        }
    }

    public void lendPublication() {
        List<Publication> successful = new ArrayList<>();
        for(Publication publication1 : publication){
            try {
                user.borrowPublication(publication1);
                successful.add(publication1);
            }catch (RuntimeException e){
                for (Publication p : successful){
                    user.returnPublication(p);
                }
                throw new RuntimeException("Unable to lend publication. No copies for " + publication1.getTitle());
            }
        }
    }

    public LocalDate getStartDate(){
        return startDate;
    }

    public LocalDate getEndDate(){
        return endDate;
    }

    public User getUser(){
        return user;
    }

    public List<Publication> getPublication(){
        return publication;
    }

    public void returnPublication(){
        for(Publication p : publication){
            p.returnPublication();
        }
    }

    public void setPrice(double price){
        this.price = price;
    }

    public double getPrice(){
        return price;
    }

    @Override
    public String toString(){
        return "RegisterLoan{" + "User: " + user.getUserName() + ", startDate: " + startDate + ", endDate: " + endDate + ", publication: " + publication + "}";
    }
}
