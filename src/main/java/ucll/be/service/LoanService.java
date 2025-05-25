package ucll.be.service;

import org.springframework.stereotype.Service;
import ucll.be.model.Membership;
import ucll.be.model.Publication;
import ucll.be.model.RegisterLoan;
import ucll.be.model.User;
import ucll.be.repository.LoanRepository;
import ucll.be.repository.PublicationRepository;
import ucll.be.repository.UserRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanService {
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final PublicationRepository publicationRepository;

    public LoanService(UserRepository userRepository, LoanRepository loanRepository, PublicationRepository publicationRepository){
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
        this.publicationRepository = publicationRepository;
    }

    public List<RegisterLoan> getLoansByUser(String email, boolean onlyActive){
        if (!userRepository.existsByEmail(email)){
            throw new RuntimeException("User does not exist: " + email);
        }
        return loanRepository.findLoansByEmail(email, onlyActive);
    }

    public String deleteUserLoansIfEligible(String email){
        List<RegisterLoan> userLoans = getLoansByUser(email, false);

        if (userLoans.isEmpty()){
            throw new RuntimeException("User has no loans.");
        }

        boolean hasActiveLoans = userLoans.stream().anyMatch(loan -> loan.getEndDate().isAfter(LocalDate.now()));

        if (hasActiveLoans){
            throw new RuntimeException("User has active loans.");
        }

        loanRepository.deleteAll(userLoans);
        return "Loans of the user successfully deleted";
    }

    public RegisterLoan registerLoan(String email, LocalDate startDate, List<Long> publicationIds){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found."));
        boolean hasActiveLoan = loanRepository.findLoansByEmail(email, true)
                .stream().anyMatch(loan -> loan.getEndDate().isAfter(LocalDate.now()));

        if (hasActiveLoan){
            throw new RuntimeException("User already has an active loan.");
        }

        List<Publication> publications = new ArrayList<>();
        for (Long id : publicationIds){
            Publication publication = publicationRepository.findById(id).orElseThrow(() -> new RuntimeException("Publication with id " + id + "not found."));
            publications.add(publication);
        }

        RegisterLoan loan = new RegisterLoan(startDate, publications,user);
        loan.setEndDate(startDate.plusDays(30));
        return loanRepository.save(loan);
    }

    public RegisterLoan returnLoan(String email, LocalDate endDate) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));

        RegisterLoan activeLoans = loanRepository.findLoansByEmail(email, true).stream().filter(loan -> loan.getStartDate().isBefore(endDate))
                .findFirst().orElseThrow(() -> new RuntimeException("User has nothing in inventory."));

        activeLoans.returnPublication();

        Membership membership = user.getMemberships()
                .stream().filter(Membership::isActive).findFirst()
                .orElse(null);

        int freeLoans = (membership != null) ? membership.getFreeLoans() : 0;
        double price;

        if (membership != null && freeLoans > 0){
            membership.redeemFreeLoan();
            price =0.0;
        }else {
            long days = ChronoUnit.DAYS.between(activeLoans.getStartDate(), endDate);
            double pricePerDay;

            pricePerDay = 1.0;
            if (membership != null){
                switch (membership.getType()){
                    case BRONZE:pricePerDay=0.75;break;
                    case SILVER:pricePerDay=0.50;break;
                    case GOLD:pricePerDay=0.25;break;
                }
            }

            price = activeLoans.getPublication().size()*days*pricePerDay;

            if (activeLoans.getEndDate() != null && endDate.isAfter(activeLoans.getEndDate())){
                long daysLate = ChronoUnit.DAYS.between(activeLoans.getEndDate(), endDate);
                double lateFee = 0.50 * daysLate * activeLoans.getPublication().size();
                price += lateFee;
            }
        }
        activeLoans.setEndDate(endDate);
        activeLoans.setPrice(price);
        loanRepository.save(activeLoans);
        return activeLoans;
    }
}
