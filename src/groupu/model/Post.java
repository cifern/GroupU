package groupu.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public final class Post {

  private int id;
  private String data;
  private String poster;
  private String group;

  private DAO dao = new DAO();
  private Connection conn = null;
  private PreparedStatement ps = null;

  public Post() {

  }

  public void createPost(String data, String poster, String group) {
    try {
      conn = dao.getConnection();
      ps = conn.prepareStatement("INSERT INTO POSTS(data, poster, groupname) VALUES(?, ?, ?)");
      ps.setString(1, data);
      ps.setString(2, poster);
      ps.setString(3, group);

      ps.execute();

      ps.close();
      conn.close();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public ArrayList<String> getPostsByGroupName(String groupname) {

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
