package groupu.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Message {

  private String toUser;
  private String messageBody;

  private DAO dao = new DAO() ;
  private Connection conn = null;
  private PreparedStatement ps = null;

  public Message(String toUser, String messageBody) {
    this.toUser = toUser;
    this.messageBody = messageBody;
  }

  public Message() {}

  public ArrayList<String> getAllMessagesFromUsers() {
    ArrayList<String> users  = new ArrayList<>();
    try {
      conn = dao.getConnection();
      ps = conn.prepareStatement("SELECT * FROM MESSAGES WHERE TO_USER=? OR FROM_USER=?");
      ps.setString(1, Session.getInstance("").getUserName());
      ps.setString(2, Session.getInstance("").getUserName());

      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        if(rs.getString("FROM_USER").equals(Session.getInstance("").getUserName()))
          users.add(rs.getString("TO_USER"));
        else
          users.add(rs.getString("FROM_USER"));
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
      ps = conn.prepareStatement("(SELECT t.* FROM PUBLIC.MESSAGES t WHERE FROM_USER=? AND TO_USER=? " +
                                      " UNION ALL SELECT t.* FROM PUBLIC.MESSAGES t WHERE FROM_USER=? AND TO_USER=?) Order by TIMESTAMP");
      ps.setString(1, username);
      ps.setString(2, Session.getInstance("").getUserName());
      ps.setString(3, Session.getInstance("").getUserName());
      ps.setString(4, username);

      ResultSet rs = ps.executeQuery();

      //label from user and to users
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

    public void sendPrivateMessage() {
      try {

        Calendar calendar = Calendar.getInstance();
        java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());


        conn = dao.getConnection();
        ps = conn.prepareStatement("INSERT INTO MESSAGES(TO_USER, FROM_USER, BODY, TIMESTAMP) VALUES(?, ?, ?, ?)");
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
