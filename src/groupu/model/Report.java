package groupu.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Report {

  private String groupName;
  private String description;

  private static DAO dao = new DAO();
  private static Connection conn = null;
  private static PreparedStatement ps = null;

  public Report() {}

  public Report(String groupName, String description) {
    this.groupName = groupName;
    this.description = description;
  }

  public void reportGroup() {
    try {
      conn = dao.getConnection();
      ps = conn.prepareStatement("INSERT INTO Reports(GROUPNAME, DESCRIPTION) Values(?, ?)");
      ps.setString(1, this.groupName);
      ps.setString(2, this.description);

      ps.execute();

      ps.close();
      conn.close();
    }  catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static ObservableList<String> getAllGroupReports(String groupName) {
    ObservableList<String> reportsList = FXCollections.observableArrayList();
    ResultSet rs = null;
    try {
      conn = dao.getConnection();
      ps = conn.prepareStatement("SELECT DESCRIPTION FROM REPORTS WHERE GROUPNAME=?");
      ps.setString(1, groupName);

      rs = ps.executeQuery();

      while (rs.next()) {
        reportsList.add(rs.getString(1));
      }
      conn.close();
      ps.close();
      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return reportsList;
  }

  public static void removeReport(String groupName, String description) {
    try {
      conn = dao.getConnection();
      ps = conn.prepareStatement("DELETE FROM REPORTS WHERE GROUPNAME=? AND DESCRIPTION=?");
      ps.setString(1, groupName);
      ps.setString(2, description);

      ps.execute();

      conn.close();
      ps.close();

    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static void removeAllReportsFromGroup(String groupName) {
    try {
      conn = dao.getConnection();
      ps = conn.prepareStatement("DELETE FROM REPORTS WHERE GROUPNAME=?");
      ps.setString(1, groupName);

      ps.execute();

      conn.close();
      ps.close();

    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
  }
}
