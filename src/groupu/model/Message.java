package groupu.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Message implements Comparable<Message> {

  private String toUser;
  private String messageBody;
  private String timeSent;

  private DAO dao = new DAO() ;
  private Connection conn = null;
  private PreparedStatement ps = null;

  public Message() {}

  public Message(String toUser, String messageBody) {
    this.toUser = toUser;
    this.messageBody = messageBody;
  }

  public Message(String toUser, String messageBody, String timeSent) {
    this.toUser = toUser;
    this.messageBody = messageBody;
    this.timeSent = timeSent;
  }

  public String getTimeSent() {
    return this.timeSent;
  }

  public String getMessageBody() {
    return this.messageBody;
  }

  public ObservableList<Message> getAllMessagesBetweenUsers(String toUser, String fromUser) {
    ObservableList<Message> messages = FXCollections.observableArrayList();

    try {
      conn = dao.getConnection();
      ps = conn.prepareStatement("SELECT * FROM MESSAGES WHERE TO_USER=? AND FROM_USER=?");
      ps.setString(1, toUser);
      ps.setString(2, fromUser);

      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        String to = rs.getString(1);
        String from = rs.getString(2);
        String body = rs.getString(3);
        String time = rs.getString(4);

        Message m = new Message(to, "You: " + body, time);

        messages.add(m);
      }

      ps = conn.prepareStatement("SELECT * FROM MESSAGES WHERE FROM_USER=? AND TO_USER=?");
      ps.setString(1, toUser);
      ps.setString(2, fromUser);

      rs = ps.executeQuery();

      while (rs.next()) {
        String to = rs.getString(1);
        String from = rs.getString(2);
        String body = rs.getString(3);
        String time = rs.getString(4);

        Message m = new Message(to, body, time);

        messages.add(m);
      }

      // sort messages by time before returning
      Collections.sort(messages);
      System.out.println(messages.toString());

      conn.close();
      ps.close();
      rs.close();

    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
    return messages;
  }

  public ArrayList<String> getAllMessagesFromUsers() {
    ArrayList<String> users  = new ArrayList<>();
    try {
      conn = dao.getConnection();
      ps = conn.prepareStatement("SELECT * FROM MESSAGES WHERE TO_USER=?");
      ps.setString(1, Session.getInstance("").getUserName());

      ResultSet rs = ps.executeQuery();

      // get all users you have a message from
      while (rs.next()) {
        users.add(rs.getString("FROM_USER"));
      }

      ps = conn.prepareStatement("SELECT * FROM MESSAGES WHERE FROM_USER=?");
      ps.setString(1, Session.getInstance("").getUserName());

      rs = ps.executeQuery();

      // get all users you have sent a message to
      while (rs.next()) {
        users.add(rs.getString("TO_USER"));
      }

      conn.close();
      ps.close();
      rs.close();

      } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
      }
    return users;
  }

  public ObservableList<String> getMessagesFromUser(String username) {
    ObservableList<String> messages  = FXCollections.observableArrayList();
    try {
      conn = dao.getConnection();
      ps = conn.prepareStatement("SELECT * FROM MESSAGES WHERE FROM_USER=?");
      ps.setString(1, username);

      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        messages.add(rs.getString("BODY"));
      }

      conn.close();
      ps.close();
      rs.close();

    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
    return messages;
  }

    public void sendPrivateMessage() {
      try {
        conn = dao.getConnection();
        ps = conn.prepareStatement("INSERT INTO MESSAGES(TO_USER, FROM_USER, BODY, TIMESENT) VALUES(?, ?, ?, ?)");
        ps.setString(1, this.toUser);
        ps.setString(2, Session.getInstance("").getUserName());
        ps.setString(3, this.messageBody);
        ps.setString(4, this.timeSent);

        ps.execute();

        conn.close();
        ps.close();
      } catch (ClassNotFoundException | SQLException e) {
        e.printStackTrace();
      }
    }

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

    @Override
    public String toString() {
      return "Message:"
          + "TO_USER: " + this.toUser + "\n"
          + "BODY: " + this.messageBody + "\n"
          + "TIME: " + this.timeSent + "\n";
    }

    @Override
    public int compareTo(Message o) {
      return this.getTimeSent().compareTo(o.getTimeSent());
    }
}
