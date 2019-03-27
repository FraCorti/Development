package it.colletta.service.user;

import it.colletta.model.SignupRequestModel;
import it.colletta.model.UserModel;
import it.colletta.repository.user.UsersRepository;
import it.colletta.service.signup.SignupRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class UserService {

    @Autowired
    private UsersRepository applicationUserRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserModel addUser(UserModel user) {
        SignupRequestService signupRequestService = new SignupRequestService();
        final String encode = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        user.setActivated(false);
        user = applicationUserRepository.save(user);
        SignupRequestModel signupRequestModel = SignupRequestModel.builder()
                .userReference(user.getId())
                .requestDate(Calendar.getInstance().getTime())
                .build();
        user.setPassword(null);
        return user;
    }
    public UserModel getUserInfo(UserModel user) {
        return applicationUserRepository.findByEmail(user.getUsername());
    }

    public void activateUser(String id) {
        applicationUserRepository.updateActivateFlagOnly(id);
    }
}
