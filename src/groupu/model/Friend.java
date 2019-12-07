package groupu.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/** Friend class to represent another user on a user's friend list. */
public class Friend {

  private String username;

  private DAO dao = new DAO();
  private Connection conn = null;
  private PreparedStatement ps = null;

  /**
   * Friend constructor that sets this friend's username.
   *
   * @param username This Friend's username.
   */
  public Friend(String username) {
    this.username = username;
  }

  /** Friend constructor that sets this Friend's username to the currently signed in user. */
  public Friend() {
    this.username = Session.getInstance("").getUserName();
  }

  /** Method that adds another user to this user's friends list. */
  public void addFriend() {
    try {
      conn = dao.getConnection();

      ps = conn.prepareStatement("INSERT INTO Friends(USER, FRIEND) VALUES(?, ?)");

      ps.setString(1, Session.getInstance("").getUserName());
      ps.setObject(2, this.username);

      ps.execute();

      ps.close();
      conn.close();
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method to retrieve all of the friends this user has.
   *
   * @return an ArrayList containing all friends.
   */
  public ArrayList<String> getFriends() {
    ArrayList<String> friends = new ArrayList<>();
    try {
      conn = dao.getConnection();

      ps = conn.prepareStatement("SELECT * FROM FRIENDS WHERE USER=?");
      ps.setString(1, this.username);

      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        friends.add(rs.getString("FRIEND"));
      }

      ps.close();
      conn.close();
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
    return friends;
  }

  /** Method to remove a friend from this users friends list. */
  public void removeFriend() {
    try {
      conn = dao.getConnection();
      ps = conn.prepareStatement("DELETE FROM FRIENDS WHERE USER=? AND FRIEND=?");
      ps.setString(1, Session.getInstance("").getUserName());
      ps.setString(2, this.username);

      ps.execute();

      conn.close();
      ps.close();
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
  }
}
