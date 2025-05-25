package ucll.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ucll.be.model.RegisterLoan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface LoanRepository extends JpaRepository<RegisterLoan, Long> {
    List<RegisterLoan> findByMembershipId(Long membershipId);
    List<RegisterLoan> findByUserEmailIgnoreCase(String email);
    @Query("SELECT l FROM RegisterLoan l WHERE LOWER(l.user.email)=LOWER(:email) AND (:onlyActive= false OR l.endDate > CURRENT_DATE)")
    List<RegisterLoan> findLoansByEmail(String email, boolean onlyActive);
}
