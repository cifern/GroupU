package groupu.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Class that represents a user session to keep track of who is logged in through different views.
 */
public class Session {
  private static Session instance;

  private String userName;
  private Set<String> privileges;

  /**
   * Constructor that sets the name of the logged in user.
   *
   * @param userName The name of the user logged in.
   */
  private Session(String userName) {
    this.userName = userName;
  }

  /**
   * Static method that retrieves an instance of the user's session.
   *
   * @param userName The name of the user.
   * @return A Session object for the current user.
   */
  public static Session getInstance(String userName) {
    if (instance == null) {
      instance = new Session(userName);
    }
    return instance;
  }

  /**
   * Retrieves the username of the currently logged in user.
   *
   * @return The username of the logged in user.
   */
  public String getUserName() {
    return userName;
  }

  // Unused method?
  public Set<String> getPrivileges() {
    return privileges;
  }

  /** Method to erase the user session. Used for when the user is logging out. */
  public void cleanUserSession() {
    userName = ""; // or null
    instance = null;
    privileges = new HashSet<>(); // or null
  }

  /**
   * Method that overrides toString for this Session.
   *
   * @return a string of this Session's instance fields.
   */
  @Override
  public String toString() {
    return "UserSession{" + "userName='" + userName + '\'' + ", privileges=" + privileges + '}';
  }
}
