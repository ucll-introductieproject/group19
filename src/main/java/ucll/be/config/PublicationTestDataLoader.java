package ucll.be.config;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import ucll.be.model.Books;
import ucll.be.model.RegisterMagazine;
import ucll.be.repository.PublicationRepository;

@Component
public class PublicationTestDataLoader {
    public final PublicationRepository publicationRepository;

    public PublicationTestDataLoader(PublicationRepository publicationRepository) {
        this.publicationRepository = publicationRepository;
    }

    @PostConstruct
    public void loadData() {
        if (publicationRepository.findAll().isEmpty()) {
            try {
                publicationRepository.save(new Books(
                        "Effective Java", 2018, 4,"Joshua Bloch", "978-0-13-468599-1"));
                publicationRepository.save(new Books(
                        "Clean Code", 2008, 2, "Robert C. Martin", "978-0-13-235088-4"));
                publicationRepository.save(new RegisterMagazine(
                        "National Geographic", 2022, 5, "Susan Goldberg", "0027-9358"));
                publicationRepository.save(new RegisterMagazine(
                        "Scientific American", 2021, 3, "Laura Helmuth", "0036-8733"));
            } catch (Exception e) {
                System.err.println(" Error in publication loader: " + e.getMessage());
            }
        }
    }
}
