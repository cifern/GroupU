package groupu.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
      ps = conn.prepareStatement("SELECT * FROM MESSAGES WHERE TO_USER=?");
      ps.setString(1, Session.getInstance("").getUserName());

      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
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
        ps = conn.prepareStatement("INSERT INTO MESSAGES(TO_USER, FROM_USER, BODY) VALUES(?, ?, ?)");
        ps.setString(1, this.toUser);
        ps.setString(2, Session.getInstance("").getUserName());
        ps.setString(3, this.messageBody);

        ps.execute();

        conn.close();
        ps.close();
      } catch (ClassNotFoundException | SQLException e) {
        e.printStackTrace();
      }
    }
}
