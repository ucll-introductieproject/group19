package ucll.be.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

import static org.hibernate.engine.transaction.internal.jta.JtaStatusHelper.isActive;

@Entity
public class Membership {

    public enum MembershipType{
        BRONZE,SILVER,GOLD
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Start date is required.")
    private LocalDate startDate;

    @NotNull(message = "End date is required.")
    private LocalDate endDate;


    @ManyToOne
    private User user;

    @NotNull(message = "Membership type is required.")
    @Enumerated(EnumType.STRING)
    private MembershipType type;

    @Min(0)
    private int freeLoans;

    public Membership(){
        setStartDate(startDate);
        setEndDate(endDate);
        setType(type);
        setId(id);
    }

    private void setId(Long id) {
        this.id = id;
    }
    public Long getId(){
        return id;
    }

    public int getFreeLoans(){
        if (isActive()) {
            return freeLoans;
        }else {return 0;}
    }

    public boolean isActive(){
        LocalDate today = LocalDate.now();
        return (startDate == null || !today.isBefore(startDate))
                && (endDate == null || !today.isAfter(endDate));
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public MembershipType getType(){
        return type;
    }

    public User getUser(){
        return user;
    }

    public void setStartDate(LocalDate startDate){
        this.startDate = startDate;
    }

    public void setType(MembershipType type){
        this.type = type;
    }

    public void setEndDate(LocalDate endDate){
        this.endDate = endDate;
    }

    public void setUser(User user){
        this.user = user;
    }

    @PostLoad
    @PostPersist
    @PostUpdate
    private void validateFreeLoans(){
        switch (type) {
            case GOLD:
                if (freeLoans < 11 || freeLoans > 15)
                    throw new IllegalArgumentException("Invalid number of free loans for membership.");
                break;
            case SILVER:
                if (freeLoans < 6 || freeLoans > 10)
                    throw new IllegalArgumentException("Invalid number of free loans for membership type.");
                break;
            case BRONZE:
                if (freeLoans < 0 || freeLoans > 5)
                    throw new IllegalArgumentException("Invalid number of free loans for membership type.");
                break;
        }
    }

    public void redeemFreeLoan(){
        if (freeLoans <= 0){
            throw new IllegalArgumentException("No more free loans.");
        }
        freeLoans--;
    }

    public int getRemainingFreeLoans(){
        return freeLoans;
    }
}
