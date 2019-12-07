package groupu.model;

import groupu.controller.GroupController;
import groupu.controller.HomeController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;


public final class GroupEvent {

  private String eventTitle;
  private String eventDescription;
  private String eventDate;

  private DAO dao = new DAO();
  private Connection conn = null;
  private PreparedStatement ps = null;

  public GroupEvent(String eventTitle, String eventDescription, String eventDate) {
    this.eventTitle = eventTitle;
    this.eventDescription = eventDescription;
    this.eventDate = eventDate;
  }

  /***
   * @param groupName
   * @return title
   */
  public String getEventTitle(String groupName) {
    String title = null;
    try {
      conn = dao.getConnection();
      ps = conn.prepareStatement("SELECT Name FROM EVENTS WHERE GROUP_NAME=?");
      ps.setString(1, groupName);

      ResultSet rs = ps.executeQuery();
      title = rs.toString();

      ps.close();
      conn.close();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return title;
  }

  /***
   *setter for event
   * @param eventTitle
   */
  public void setEvent(String eventTitle) {
    String group =
    this.eventTitle = eventTitle;
    try {
      conn = dao.getConnection();
      ps = conn.prepareStatement("INSERT INTO Groups(name,) VALUES(?, ?, ?)");
      ps.setString(1, eventTitle);

      ps.execute();

      ps.close();
      conn.close();
    }

    catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  /***
   *getter for event description
   * @return eventDescription
   */
  public String getEventDescription() {
    return eventDescription;
  }

  /***
   *setter for event description
   * @param eventDescription
   */
  public void setEventDescription(String eventDescription) {
    this.eventDescription = eventDescription;
  }

  /***
   *getter for event date
   * @return eventDate
   */
  public String getEventDate() {
    return eventDate;
  }

  /***
   * setter event date
   * @param eventDate
   */
  public void setEventDate(String eventDate) {
    this.eventDate = eventDate;
  }
}
