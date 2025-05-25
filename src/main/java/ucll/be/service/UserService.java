package ucll.be.service;

import ucll.be.model.Membership;
import ucll.be.model.Profile;
import ucll.be.model.User;
import org.springframework.stereotype.Service;
import ucll.be.repository.LoanRepository;
import ucll.be.repository.MembershipRepository;
import ucll.be.repository.UserRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;

    public UserService(UserRepository userRepository, LoanRepository loanRepository, MembershipRepository membershipRepository){
        this.userRepository = userRepository;
        this.membershipRepository = membershipRepository;
    }

    public List<User> getUsersByInterest(String interest){
        return userRepository.findUsersByProfile_InterestContainingIgnoreCase(interest);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User addUser(User user){
        Profile profile = user.getProfile();

        if (profile != null){
            if (user.getAge() < 18){
                throw new RuntimeException("User must be at least 18 years old to have a profile.");
            }
            if (isBlank(profile.getBio())){
                throw new RuntimeException("Bio is required");
            }
            if (isBlank(profile.getLocation())){
                throw new RuntimeException("Location is required.");
            }
            if (isBlank(profile.getInterest())){
                throw new RuntimeException("Interest are required.");
            }
            profile.setUser(user);
        }
        return userRepository.save(user);
    }

    public User updateUser(String email, User updatedUser){
        User existingUser = getUserByEmail(email);
        existingUser.setName(updatedUser.getName());
        existingUser.setUserName(updatedUser.getUserName());
        existingUser.setAge(updatedUser.getAge());
        return userRepository.save(existingUser);
    }

    public List<User> getAllAdultsUsers(int age){
        return  userRepository.findByAgeGreaterThanEqual(age);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found."));
    }

    public List<User> getUserByAgeRange(int min, int max){
        return userRepository.findByAgeBetween(min, max);
    }

    public List<User> getUsersByName(String name){
        if (name == null || name.isEmpty()){
            return userRepository.findAll();
        }
        return userRepository.findByNameContaining(name);
    }

    public String deleteUser(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("User not found."));
        userRepository.delete(user);
        return "User successfully deleted.";
    }

    public List<User> getUsersByInterestAndMinAgeSorted(String interest, int minAge){
        return userRepository.findAll().stream()
                .filter(u -> u.getAge() > minAge)
                .filter(u ->u.getProfile()  != null)
                .filter(u -> u.getProfile().getInterest() != null)
                .filter(u -> u.getProfile().getInterest().toLowerCase().contains(interest.toLowerCase()))
                .sorted(Comparator.comparing(u -> u.getProfile().getLocation()))
                .collect(Collectors.toList());
    }

    public User addMembershipToUser(String email, Membership newMembership){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User doesn't exist."));

        if (!List.of(Membership.MembershipType.GOLD, Membership.MembershipType.BRONZE, Membership.MembershipType.SILVER).contains(newMembership.getType())){
            throw new RuntimeException("Invalid membership type.");
        }

        LocalDate today = LocalDate.now();
        if (newMembership.getStartDate() == null){throw new RuntimeException("Start date is required.");}

        if (newMembership.getEndDate() == null){
            throw new RuntimeException("End date is required.");
        }

        if (newMembership.getEndDate().equals(newMembership.getStartDate().plusYears(1))){
            throw new RuntimeException("End date must be 1 year after the start date.");
        }

        for (Membership m : user.getMemberships()){
            boolean overlap = !(newMembership.getEndDate().isBefore(m.getStartDate()) ||
                    newMembership.getStartDate().isAfter(m.getEndDate()));
            if (!overlap) {
                throw new RuntimeException("User has already a membership on that date.");
            }
        }

        newMembership.setType(Membership.MembershipType.valueOf(newMembership.getType().name().toUpperCase()));
        newMembership.setUser(user);
        user.getMemberships().add(newMembership);

        return userRepository.save(user);
    }

    public Membership getMembershipByEmailAndDate(String email, LocalDate date){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User does not exist."));

        return membershipRepository.findMembershipByEmailAndDate(email, date).orElseThrow(() -> new RuntimeException("No membership found for user on date" + date));
    }

}

