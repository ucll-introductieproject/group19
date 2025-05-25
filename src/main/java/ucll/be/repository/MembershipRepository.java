package ucll.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ucll.be.model.Membership;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    List<Membership> findByUserEmail(String email);
    @Query("SELECT m FROM Membership m WHERE m.user.email = :email AND :date BETWEEN m.startDate AND m.endDate")
    Optional<Membership> findMembershipByEmailAndDate(@Param("email") String email, @Param("date")LocalDate date);
}
