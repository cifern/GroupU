package groupu.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/** Class that represents a text post in the group discussion tab. */
public final class Post {

  private int id;
  private String data;
  private String poster;
  private String group;

  private static DAO dao = new DAO();
  private static Connection conn = null;
  private static PreparedStatement ps = null;

  /** No argument constructor */
  public Post() {}

  /**
   * Constructor to create a new post object.
   *
   * @param data The text of the post.
   * @param poster The user who creates the post.
   * @param group The group the post belongs to.
   */
  public Post(String data, String poster, String group) {
    this.data = data;
    this.poster = poster;
    this.group = group;
  }

  /**
   * Method to create a new post and insert it into the database.
   *
   * @param data The text of the post.
   * @param poster The user who creates the post.
   * @param group The group the post belongs to.
   */
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

  /** Method that takes a Post object and deletes it from the database. */
  public void deletePost() {
    try {
      conn = dao.getConnection();
      ps = conn.prepareStatement("DELETE FROM POSTS WHERE DATA=? AND POSTER=? AND GROUPNAME=?");
      ps.setString(1, this.data);
      ps.setString(2, this.poster);
      ps.setString(3, this.group);

      ps.execute();

      conn.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method to retrieve all posts from a specified group.
   *
   * @param groupname The group name to get posts from.
   * @return An ArrayList containing all posts
   */
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

  /**
   * Method to delete all posts from a specified group.
   *
   * @param groupName The group to delete posts from.
   */
  public static void deleteAllPostsFromGroup(String groupName) {
    try {
      conn = dao.getConnection();
      ps = conn.prepareStatement("DELETE FROM POSTS WHERE GROUPNAME=?");
      ps.setString(1, groupName);

      ps.execute();

      conn.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}
