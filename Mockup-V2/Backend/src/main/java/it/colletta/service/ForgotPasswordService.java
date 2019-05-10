package it.colletta.service;

import it.colletta.model.ForgotPasswordModel;
import it.colletta.model.UserModel;
import it.colletta.model.helper.ChangePasswordHelper;
import it.colletta.repository.administration.ForgotPasswordRepository;
import it.colletta.repository.user.UsersRepository;
import it.colletta.service.signup.EmailServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

@Service
public class ForgotPasswordService {

  private UsersRepository usersRepository;
  private ForgotPasswordRepository forgotPasswordRepository;
  private EmailServiceImpl emailService;
  private BCryptPasswordEncoder passwordEncoder;

  /**
   * Constructor.
   */
  @Autowired
  public ForgotPasswordService(UsersRepository usersRepository,
      ForgotPasswordRepository forgotPasswordRepository, EmailServiceImpl emailService,
      BCryptPasswordEncoder passwordEncoder) {
    this.usersRepository = usersRepository;
    this.forgotPasswordRepository = forgotPasswordRepository;
    this.emailService = emailService;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * generateNewPasswordRequest.
   */
  @Transactional
  public void generateNewPasswordRequest(String username, ControllerLinkBuilder link)
      throws Exception {
    UserModel userInstance = usersRepository.findByEmail(username.toLowerCase())
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    ForgotPasswordModel forgotPasswordModel = new ForgotPasswordModel();
    forgotPasswordModel.setRequestDate(Calendar.getInstance().getTime());
    forgotPasswordModel.setUserId(userInstance.getId());
    final ForgotPasswordModel requestPassword = forgotPasswordRepository.save(forgotPasswordModel);
    emailService.forgotPasswordMail(userInstance,
        link.slash(requestPassword.getId()).withSelfRel().getHref());
  }

  /**
   * setNewPassword.
   */
  @Transactional
  public void setNewPassword(ChangePasswordHelper passwordHelper) throws Exception {
    ForgotPasswordModel passwordModel =
        forgotPasswordRepository.findById(passwordHelper.getRequestId())
            .orElseThrow(() -> new ResourceNotFoundException("Request not found"));
    String password = passwordHelper.getPassword();
    assert password.equals(passwordHelper.getPasswordConfirm());
    String passwordEncoded = passwordEncoder.encode(password);
    UserModel userModel = usersRepository.findById(passwordModel.getUserId())
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    userModel.setPassword(passwordEncoded);
    usersRepository.save(userModel);
  }
}
