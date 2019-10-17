package groupu.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserStorage {

  private static final String JdbcDriver = "org.h2.Driver";
  private static final String DatabaseUrl = "jdbc:h2:./res/UserDB";

  private final String user = "";
  private final String pass = "";

  private Connection conn = null;
  private Statement statement = null;
  private PreparedStatement ps = null;

  public void createUser(String firstname, String lastname, String username, String password) {
    try {
      Class.forName(JdbcDriver);
      conn = DriverManager.getConnection(DatabaseUrl, user, pass);
      ps = conn.prepareStatement("INSERT INTO Users(firstname, lastname, username, password) VALUES(?, ?, ?, ?)");
      ps.setString(1, firstname);
      ps.setString(2, lastname);
      ps.setString(3, username);
      ps.setString(4, password);

      ps.execute();

      ps.close();
      conn.close();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public boolean checkUserExists(String username) throws SQLException {
    boolean exists = false;
    try {
      Class.forName(JdbcDriver);
      conn = DriverManager.getConnection(DatabaseUrl, user, pass);
      ps = conn.prepareStatement("SELECT * FROM USERS WHERE USERNAME=?");
      ps.setString(1, username);

      ResultSet rs = ps.executeQuery();

      // rs.next() is false if the set is empty
      exists = rs.next();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return exists;
  }

}
