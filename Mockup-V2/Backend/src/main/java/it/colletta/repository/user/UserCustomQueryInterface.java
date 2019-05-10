package it.colletta.repository.user;

import it.colletta.model.UserModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Interface for custom query on users collections.
 *
 */
public interface UserCustomQueryInterface {

  /**
   * @param id user to set enabled = true.
   */
  void updateActivateFlagOnly(final String id);

  /**
   * @return all the user who are successfully register and activated in the system.
   */
  List<UserModel> getAllUsers();

  /**
   * @return all the user who are in the list.
   */
  List<UserModel> findAllByList(final List<String> userId);

  /**
   * @return all the user with a specific role in a page format.
   */
  Page<UserModel> findAllByRolePage(Pageable page, String role);
}
