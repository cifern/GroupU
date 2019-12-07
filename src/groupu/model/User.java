package groupu.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Class that represents a user. */
public final class User {

  private String username;
  private String password;

  private DAO dao = new DAO();
  private Connection conn = null;
  private PreparedStatement ps = null;

  /** Constructor that sets the username. */
  public User() {
    this.username = "test";
  }

  /**
   * Constructor that sets the username and password of this user.
   *
   * @param username The username of this user.
   * @param password The password of this user.
   */
  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  /** Method to create a new user and insert it into the database. */
  public void createUser() {
    try {
      conn = dao.getConnection();

      ps = conn.prepareStatement("INSERT INTO Users(username, password) VALUES(?, ?)");
      ps.setString(1, this.username);
      ps.setString(2, this.password);

      ps.execute();

      ps.close();
      conn.close();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method to check the database to see if a specified user already exists.
   *
   * @param username The username to check.
   * @return A boolean if the user exists already or not.
   */
  public boolean checkUserExists(String username) {
    boolean exists = false;
    try {
      conn = dao.getConnection();
      ps = conn.prepareStatement("SELECT * FROM USERS WHERE USERNAME=?");
      ps.setString(1, username);

      ResultSet rs = ps.executeQuery();

      // rs.next() is false if the set is empty
      exists = rs.next();

      // close stuff
      ps.close();
      conn.close();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return exists;
  }

  /**
   * Method to compare the entered password on the login screen with the password in the database.
   *
   * @param username The username to check.
   * @param password The password to compare.
   * @return A boolean if the password matches or not.
   */
  public boolean comparePassword(String username, String password) {
    boolean matchedPass = false;
    try {
      conn = dao.getConnection();
      ps = conn.prepareStatement("SELECT * FROM USERS WHERE USERNAME=? AND PASSWORD=?");
      ps.setString(1, username);
      ps.setString(2, password);

      ResultSet rs = ps.executeQuery();

      // rs.next() is not empty if both user and pass are in the same row
      matchedPass = rs.next();

      // close stuff
      ps.close();
      conn.close();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return matchedPass;
  }

  /**
   * Method to add the currently logged in user to the specified group.
   *
   * @param group The group to add the user to.
   */
  public void joinGroup(Group group) {
    try {
      conn = dao.getConnection();
      ps = conn.prepareStatement("SELECT * FROM USERS_GROUPS WHERE USER_ID=? AND GROUP_ID=?");
      ps.setString(1, Session.getInstance("").getUserName());
      ps.setString(2, group.toString());
      ResultSet rs = ps.executeQuery();
      if (rs.next()) return;

      ps = conn.prepareStatement("INSERT INTO USERS_GROUPS(USER_ID, GROUP_ID) VALUES(?, ?)");
      ps.setString(1, Session.getInstance("").getUserName());
      ps.setString(2, group.toString());
      ps.execute();

      ps.close();
      conn.close();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method to retrieve the groups that the currently signed in user has joined.
   *
   * @return A ResultSet containing all the joined groups.
   */
  public ResultSet getJoinedGroups() {
    try {
      conn = dao.getConnection();
      String SQL =
          "select groups.name from USERS_GROUPS, GROUPS where users_groups.USER_ID =? AND groups.NAME = users_groups.group_ID";
      ps = conn.prepareStatement(SQL);
      ps.setString(1, Session.getInstance("").getUserName());

      ResultSet rs = ps.executeQuery();

      return rs;
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Method to delete all users from the specified group.
   *
   * @param groupName The name of the group to remove users from.
   */
  public void deleteAllUsersFromGroup(String groupName) {
    try {
      conn = dao.getConnection();
      ps = conn.prepareStatement("DELETE FROM USERS_GROUPS WHERE GROUP_ID=?");
      ps.setString(1, groupName);

      ps.execute();

      ps.close();
      conn.close();
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method to get the user's username.
   *
   * @return This user's username.
   */
  public final String getUser() {
    return username;
  }

  /**
   * Method that overrides toString to get this user's username.
   *
   * @return A string displaying this user's username.
   */
  @Override
  public String toString() {
    return "Username: " + this.username + "\n";
  }
}
