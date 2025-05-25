package ucll.be.unit.model.service;

import ucll.be.model.Profile;
import ucll.be.model.User;

public class UserTestImpl extends User {
    public UserTestImpl(String janeDoe, int i, String email, String pwd1, Profile profile) {
        super(janeDoe,email,pwd1,i,profile);
    }
}
