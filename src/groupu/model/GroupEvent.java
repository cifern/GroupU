package groupu.model;

import java.util.Date;

public final class GroupEvent {

  private String eventTitle;
  private String eventDescription;
  private Date eventDate;

  public GroupEvent(String eventTitle, String eventDescription, Date eventDate) {
    this.eventTitle = eventTitle;
    this.eventDescription = eventDescription;
    this.eventDate = eventDate;
  }

  public String getEventTitle() {
    return eventTitle;
  }

  public void setEventTitle(String eventTitle) {
    this.eventTitle = eventTitle;
  }

  public String getEventDescription() {
    return eventDescription;
  }

  public void setEventDescription(String eventDescription) {
    this.eventDescription = eventDescription;
  }

  public Date getEventDate() {
    return eventDate;
  }

  public void setEventDate(Date eventDate) {
    this.eventDate = eventDate;
  }
}
