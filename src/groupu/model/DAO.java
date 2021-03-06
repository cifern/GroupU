package groupu.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Class that represents a Data Access Object. */
public class DAO {

  /**
   * Method to create a new connection to the database.
   *
   * @return A connection to the UserDB database.
   * @throws SQLException Throws exception if cannot connect to database.
   * @throws ClassNotFoundException Throws exception if H2 driver is not found.
   */
  public Connection getConnection() throws SQLException, ClassNotFoundException {
    Class.forName("org.h2.Driver");
    return DriverManager.getConnection("jdbc:h2:./res/UserDB", "", "");
  }
}
