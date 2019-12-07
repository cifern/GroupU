package groupu.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Class to represent a private message from one user to another. */
public class Message {

  private String toUser;
  private String messageBody;

  private DAO dao = new DAO();
  private Connection conn = null;
  private PreparedStatement ps = null;

  /**
   * Constructor that sets who the message is going to and the body of the message.
   *
   * @param toUser The user the message is going to.
   * @param messageBody The content of the message body.
   */
  public Message(String toUser, String messageBody) {
    this.toUser = toUser;
    this.messageBody = messageBody;
  }

  /** No argument constructor. */
  public Message() {}

  /**
   * Method to retrieve all messages that include the current user.
   *
   * @return An ArrayList of all messages found.
   */
  public ArrayList<String> getAllMessagesFromUsers() {
    ArrayList<String> users = new ArrayList<>();
    try {
      conn = dao.getConnection();
      ps = conn.prepareStatement("SELECT * FROM MESSAGES WHERE TO_USER=? OR FROM_USER=?");
      ps.setString(1, Session.getInstance("").getUserName());
      ps.setString(2, Session.getInstance("").getUserName());

      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        if (rs.getString("FROM_USER").equals(Session.getInstance("").getUserName()))
          users.add(rs.getString("TO_USER"));
        else users.add(rs.getString("FROM_USER"));
      }

      conn.close();
      ps.close();
      rs.close();

    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
    return users;
  }

  /**
   * Method to retrieve all messages that were sent by a specified user.
   *
   * @param username The username to get messages from.
   * @return An ObservableList containing all messages.
   */
  public ObservableList<String> getMessagesFromUser(String username) {
    ObservableList<String> messages = FXCollections.observableArrayList();
    try {
      conn = dao.getConnection();
      ps =
          conn.prepareStatement(
              "(SELECT t.* FROM PUBLIC.MESSAGES t WHERE FROM_USER=? AND TO_USER=? "
                  + " UNION ALL SELECT t.* FROM PUBLIC.MESSAGES t WHERE FROM_USER=? AND TO_USER=?) Order by TIMESTAMP");
      ps.setString(1, username);
      ps.setString(2, Session.getInstance("").getUserName());
      ps.setString(3, Session.getInstance("").getUserName());
      ps.setString(4, username);

      ResultSet rs = ps.executeQuery();

      // label from user and to users
      while (rs.next()) {
        messages.add(rs.getString("FROM_USER") + ": " + rs.getString("BODY"));
      }

      conn.close();
      ps.close();
      rs.close();

    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
    return messages;
  }

  /** Method to send a private message to this message object's recipient. */
  public void sendPrivateMessage() {
    try {
      Calendar calendar = Calendar.getInstance();
      java.sql.Timestamp ourJavaTimestampObject =
          new java.sql.Timestamp(calendar.getTime().getTime());

      conn = dao.getConnection();
      ps =
          conn.prepareStatement(
              "INSERT INTO MESSAGES(TO_USER, FROM_USER, BODY, TIMESTAMP) VALUES(?, ?, ?, ?)");
      ps.setString(1, this.toUser);
      ps.setString(2, Session.getInstance("").getUserName());
      ps.setString(3, this.messageBody);
      ps.setTimestamp(4, ourJavaTimestampObject);
      ps.execute();

      conn.close();
      ps.close();
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
  }

  /** Method to delete all messages that the current user has sent to this object's user */
  public void deleteAllMessages() {
    try {
      conn = dao.getConnection();
      ps = conn.prepareStatement("DELETE FROM MESSAGES WHERE TO_USER=? AND FROM_USER=?");
      ps.setString(1, Session.getInstance("").getUserName());
      ps.setString(2, this.toUser);

      ps.execute();

      conn.close();
      ps.close();
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
  }
}
