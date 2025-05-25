package ucll.be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ucll.be.model.Publication;
import java.util.List;
import java.util.logging.SimpleFormatter;

public interface PublicationRepository extends JpaRepository <Publication, Long> {
    List<Publication> findByTitleContainingIgnoreCase(String title);
    List<Publication> findByAvailableCopiesGreaterThan(int number);
    @Query("SELECT p FROM Publication p WHERE TYPE (p) = :clazz")
    List<Publication> findByClass(@Param("clazz") Class<? extends Publication> clazz);
}

