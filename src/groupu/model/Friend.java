package groupu.model;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Friend {

  private String username;

  private DAO dao = new DAO() ;
  private Connection conn = null;
  private PreparedStatement ps = null;

  public Friend(String username) {
    this.username = username;
  }

  public Friend() {
    this.username = Session.getInstance("").getUserName();
  }

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

  public ArrayList<String> getFriends() {
    ArrayList<String> friends = new ArrayList<>();
    try {
      conn = dao.getConnection();

      ps = conn.prepareStatement("SELECT * FROM FRIENDS WHERE USER=?");
      ps.setString(1, this.username);

      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        friends.add(rs.getString("FRIENDS"));
      }

      ps.close();
      conn.close();
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
    return friends;
  }

}
