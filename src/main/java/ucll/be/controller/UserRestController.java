package ucll.be.controller;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import ucll.be.model.Membership;
import ucll.be.model.User;
import ucll.be.model.RegisterLoan;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;
import ucll.be.service.LoanService;
import ucll.be.service.UserService;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/users")
public class UserRestController {
    private final UserService userService;
    private final LoanService loanService;

    public UserRestController(UserService userService, LoanService loanService){
        this.userService = userService;
        this.loanService = loanService;
    }

    @GetMapping("")
    public List<User> getAllUsers(@RequestParam(required = false) String name){
        if (name == null || name.isEmpty()){
            return userService.getAllUsers();
        }
        return userService.getUsersByName(name);
    }

    @GetMapping("/adults")
    public List<User> getAllAdultUsers(){
      List<User> adultUsers = userService.getAllAdultsUsers(18);
      if (adultUsers.isEmpty()){
          throw new RuntimeException(("No adult users found."));
      }
        return adultUsers;
    }

    @GetMapping("/oldest")
    public User getOldest(){
        List<User> user = userService.getAllAdultsUsers(18);
       if (user.isEmpty()){
           throw new RuntimeException("No adult users found.");
       }

       return user.stream().max(Comparator.comparingInt(User::getAge)).orElseThrow(() -> new RuntimeException("Could not determine oldest user."));
    }

    @GetMapping("/by-email")
    public User getUserByEmail(@RequestParam("email") String email){
        return userService.getUserByEmail(email);
    }

    @GetMapping("/adults/{age}")
    public List<User> getAllUsersByAge(@PathVariable("age") int age){
        if (age < 0){
            throw new RuntimeException("Invalid age");
        }
        return userService.getAllAdultsUsers(age);
    }

    @GetMapping("/age/{min}/{max}")
    public List<User> getUserWithinAgeRange(@PathVariable("min") int min, @PathVariable("max") int max){
        return userService.getUserByAgeRange(min, max);
    }

    @GetMapping("/{email}/loans")
    public ResponseEntity<List<RegisterLoan>> getLoansByUser(@PathVariable String email, @RequestParam(defaultValue = "false") boolean onlyActive){
        List<RegisterLoan> loans = loanService.getLoansByUser(email, onlyActive);
        return ResponseEntity.ok(loans);
    }

    @GetMapping("/{email}")
    public User getUpdatedUser(@PathVariable String email){
        return userService.getUserByEmail(email);
    }

    @GetMapping("/interest/{interest}")
    public List<User> getUsersByInterest(@PathVariable("interest") String interest){
        if (interest == null || interest.trim().isEmpty()){
            throw new RuntimeException("Interest cannot be empty.");
        }

        List<User> users = userService.getUsersByInterest(interest.trim());
        if (users.isEmpty()){
            throw new RuntimeException("No users found with interest in " + interest);
        }

        return users;
    }

    @GetMapping("/interest/{interest}/{age}")
    public ResponseEntity<?> filteredByInterestAndAge(@PathVariable("interest") String interest, @PathVariable int age){
        if (interest == null || interest.trim().isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("interest", "Interest doesn't exist"));
        }

        if (age<0 || age > 150){
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("age", "age must be above 0 preferably between 18 and 150"));
        }

        List<User> users = userService.getUsersByInterestAndMinAgeSorted(interest, age);

        if (users.isEmpty()){
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "No users found with interest in " + interest + " and older than " + age));
        }

        return ResponseEntity.ok(users);
    }

    @PostMapping("/{email}/membership")
    public ResponseEntity<?> AddMembership(@PathVariable("email") String email, @RequestBody Membership membership){
        try{
            User updatedUser = userService.addMembershipToUser(email, membership);
            return ResponseEntity.ok(updatedUser);
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{email}/membership")
    public ResponseEntity<Membership> getMembershipByDate(@PathVariable String email, @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        Membership membership = userService.getMembershipByEmailAndDate(email, date);
        return ResponseEntity.ok(membership);
    }

    @PostMapping("")
    public ResponseEntity<User> addUser(@RequestBody User user){
        User added = userService.addUser(user);
        return ResponseEntity.ok(added);
    }

    @PutMapping("/{email}/loans/return/{returnDate}")
    public ResponseEntity<RegisterLoan> returnLoan(@PathVariable String email, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate){
        RegisterLoan registerLoan = loanService.returnLoan(email, returnDate);
        return ResponseEntity.ok(registerLoan);
    }

    @PostMapping("/{email}/loans/{startDate}")
    public ResponseEntity<RegisterLoan> addLoans(@PathVariable("email") String email, @PathVariable("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestBody List<Long> publicationIds){
        RegisterLoan loan = loanService.registerLoan(email, startDate,publicationIds);
        return ResponseEntity.ok(loan);
    }

    @PutMapping("/{email}")
    public ResponseEntity<User> updateUser(@PathVariable String email, @RequestBody User updateUser){
        User result = userService.updateUser(email, updateUser);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{email}/loans")
    public ResponseEntity<String> deleteUserLoans(@PathVariable String email){
        String message = loanService.deleteUserLoansIfEligible(email);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email){
        String message = userService.deleteUser(email);
        return ResponseEntity.ok(message);
    }
}

