package groupu.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public final class Post {

  private String data;
  private String poster;
  private String group;

  private static DAO dao = new DAO();
  private static Connection conn = null;
  private static PreparedStatement ps = null;

  public Post(String data, String poster, String group) {
    this.data = data;
    this.poster = poster;
    this.group = group;
  }

  public void createPost() {
    try {
      conn = dao.getConnection();
      ps = conn.prepareStatement("INSERT INTO POSTS(data, poster, groupname) VALUES(?, ?, ?)");
      ps.setString(1, this.data);
      ps.setString(2, this.poster);
      ps.setString(3, this.group);

      ps.execute();

      ps.close();
      conn.close();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static ArrayList<String> getPostsByGroupName(String groupname) {

    ArrayList<String> postList = new ArrayList<>();

    try {
      conn = dao.getConnection();
      ps = conn.prepareStatement("SELECT * FROM POSTS WHERE GROUPNAME=?");
      ps.setString(1, groupname);

      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        String data = rs.getString(2);
        String poster = rs.getString(3);

        String post = poster + ": " + data;
        postList.add(post);
      }

      ps.close();
      conn.close();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return postList;
  }
}
