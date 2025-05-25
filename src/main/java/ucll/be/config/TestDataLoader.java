package ucll.be.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import ucll.be.model.*;
import ucll.be.repository.LoanRepository;
import ucll.be.repository.PublicationRepository;
import ucll.be.service.UserService;

import java.time.LocalDate;
import java.util.List;

@Component
    public class TestDataLoader {
        private final UserService userService;
        private final LoanRepository loanRepository;
    private final PublicationRepository publicationRepository;

    public TestDataLoader(UserService userService, LoanRepository loanRepository, PublicationRepository publicationRepository) {
        this.loanRepository = loanRepository;
        this.userService = userService;
        this.publicationRepository = publicationRepository;
    }

        @PostConstruct
        public void loadTestData() {
            Books book1 = new Books("Jungle Book", 2022, 5, "Author A", "123-4567890123");
            Books book2 = new Books("Moby Dick", 2021, 5, "Author B", "123-4567890124");
            Books book3 = new Books("The Hobbit", 2020, 5, "Author C", "123-4567890125");

            publicationRepository.saveAll(List.of(book1,book2,book3));

            Profile primaryProfile = new Profile("Software engineer who loves backend development.", "Brussels, Belgium", "Java, Spring Boot, Chess");
            Profile linkedProfile = new Profile("Student and avid reader of science fiction.", "Ghent, Belgium", "Books, Sci-Fi, Programming");
            Profile unrelatedProfile = new Profile("Digital nomad exploring the world with a laptop.", "Lisbon; Portugal", "Traveling, Photography, Python");


            User primaryUser = new User("Primary User", "primary@email.com", "Pass123!", 25, primaryProfile);
            User linkedUser = new User("Linked User", "linked@email.com", "Linked456!", 22, linkedProfile);
            User unrelatedUser = new User("Unrelated User", "unrelated@email.com", "Unrel789!", 30, unrelatedProfile);
            User teenUser = new User("Teen User", "teen@email.com", "Teen1234!", 16,null);

            primaryUser.borrowPublication(book1);
            primaryUser.borrowPublication(book2);

            linkedUser.borrowPublication(book1);

            unrelatedUser.borrowPublication(book3);

            teenUser.borrowPublication(book2);

            userService.addUser(primaryUser);
            userService.addUser(linkedUser);
            userService.addUser(unrelatedUser);
            userService.addUser(teenUser);

            RegisterLoan loan1 = new RegisterLoan(LocalDate.now(), List.of(book1, book2), primaryUser);
            RegisterLoan loan2 = new RegisterLoan(LocalDate.now(), List.of(book1), linkedUser);
            RegisterLoan loan3 = new RegisterLoan(LocalDate.now(), List.of(book3), unrelatedUser);
            RegisterLoan loan4 = new RegisterLoan(LocalDate.now(), List.of(book2), teenUser);

            loanRepository.saveAll(List.of(loan1, loan2, loan3, loan4));
        }
    }
