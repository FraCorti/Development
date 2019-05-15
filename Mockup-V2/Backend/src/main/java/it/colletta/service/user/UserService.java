package it.colletta.service.user;

import com.mongodb.DuplicateKeyException;

import it.colletta.model.UserModel;
import it.colletta.repository.user.UsersRepository;
import it.colletta.security.ParseJwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {

  private UsersRepository applicationUserRepository;

  @Autowired
  public UserService(UsersRepository usersRepository) {
    this.applicationUserRepository = usersRepository;
  }

  public Optional<UserModel> findById(final String userId) {
    return applicationUserRepository.findById(userId);
  }


  /**
   * Return list of user by role.
   *
   * @return List UserModel with that role
   */
  public Page<UserModel> findByRole(final Pageable page, final String role) {
    return applicationUserRepository.findAllByRolePage(page, role);
  }

  /**
   * Return user information.
   *
   * @param id User id
   * @return User
   */
  public UserModel getUserInfo(final String id) {
    Optional<UserModel> userModelOptional = applicationUserRepository.findById(id);
    if (userModelOptional.isPresent()) {
      return userModelOptional.get();
    } else {
      throw new UsernameNotFoundException("Id not refer to a user of the system");
    }
  }

  public List<UserModel> getAllListUser(final List<String> userId) {
    return applicationUserRepository.findAllByList(userId);
  }

  /**
   * Active an user.
   *
   * @param id User id
   */
  public void activateUser(final String id) {
    applicationUserRepository.updateActivateFlagOnly(id);
  }

  /**
   * Delete user.
   *
   * @param userId User id
   * @return user deleted
   */
  public UserModel deleteUser(final String userId) {
    Optional<UserModel> userToDelete = applicationUserRepository.findById(userId);
    if (userToDelete.isPresent()) {
      applicationUserRepository.delete(userToDelete.get());
      return userToDelete.get();
    } else {
      throw new UsernameNotFoundException("Id not found");
    }
  }

  /**
   * Return user by Email.
   *
   * @param email User email
   * @return User
   */
  public UserModel findByEmail(final String email) {
    return applicationUserRepository.findByEmail(email).orElseThrow(ResourceNotFoundException::new);
  }

  /**
   * Update user info.
   *
   * @param newUserData User info
   * @param token User token
   * @return User
   * @throws DuplicateKeyException if the email is already in the system
   * @throws ResourceNotFoundException if the user is not found
   */
  public UserModel updateUser(final UserModel newUserData, final String token)
      throws DuplicateKeyException, ResourceNotFoundException {
    String id = ParseJwt.getIdFromJwt(token);
    UserModel user =
        applicationUserRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    Optional<String> email = Optional.ofNullable(newUserData.getUsername());
    email.ifPresent(user::setEmail);
    Optional<String> newFirstName = Optional.ofNullable(newUserData.getFirstName());
    newFirstName.ifPresent(user::setFirstName);
    Optional<String> newLastName = Optional.ofNullable(newUserData.getLastName());
    newLastName.ifPresent(user::setLastName);
    Optional<String> newLanguageName = Optional.ofNullable(newUserData.getLanguage());
    newLanguageName.ifPresent(user::setLanguage);
    Optional<Date> newDateOfBirth = Optional.ofNullable(newUserData.getDateOfBirth());
    newDateOfBirth.ifPresent(user::setDateOfBirth);
    return applicationUserRepository.save(user);
  }

  /**
   * Return all user.
   *
   * @return List of students
   */
  public List<UserModel> getAllUsers() {
    return applicationUserRepository.getAllUsers();
  }


  public long count() {
    return applicationUserRepository.count();
  }

  /**
   * Return all developer user.
   *
   * @param userId Id of the applicant
   * @return User List
   */
  public List<UserModel> getAllDevelopmentToEnable(final String userId) {
    Optional<UserModel> user = applicationUserRepository.findById(userId);
    List<UserModel> mydevelopment = null;
    if (user.isPresent()) {
      // if (user.get().getRole().equals(Role.ADMIN)) {}
      mydevelopment = applicationUserRepository.findAllDeveloperDisabled();
    }
    return mydevelopment;
  }

}
