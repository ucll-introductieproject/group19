package ucll.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ucll.be.model.User;

import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByAgeGreaterThanEqual(int ageIsGreaterThan);
    List<User> findByNameContaining(String name);
    List<User> findByAgeBetween(int ageAfter, int ageBefore);
    List<User> findUsersByProfile_InterestContainingIgnoreCase(String interest);
    void delete(User user);
    List<User> findAll();
    boolean existsByEmail(String email);
}
