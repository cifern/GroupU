package groupu.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** Group class that represents a user created group. */
public final class Group {
    private String name = "";
    private String description;
    private String admin;
    private String tags[];

    private DAO dao = new DAO();
    private Connection conn = null;
    private PreparedStatement ps = null;

  /** No argument constructor. */
  public Group() {}

  /**
   * Constructor that sets this group's name.
   *
   * @param groupName this group's name.
   */
  public Group(String groupName) {
    this.name = groupName;
  }

  /**
   * Constructor that creates a new group object and database row.
   *
   * @param name The group name.
   * @param description The group description.
   * @param admin The group admin.
   * @param tags The group tags.
   */
  public Group(String name, String description, String admin, String[] tags) {
    if (!checkGroupExists(name)) createGroup(name, description, admin, tags);
  }

  /**
   * Method that sets the description of the group.
   *
   * @param description The group description.
   * @param groupName The group name.
   */
  public void setDescription(String description, String groupName) {
    try {
      conn = dao.getConnection();
      ps = conn.prepareStatement("UPDATE GROUPS SET DESCRIPTION=? WHERE NAME=?");
      ps.setString(1, description);
      ps.setString(2, groupName);

        ps.execute();

        ps.close();
        conn.close();
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

  /**
   * Method to retrieve all tags from a group.
   *
   * @return a String array of the group tags.
   */
  public String[] getTags() {
    String[] tags = new String[10];
    return tags;
  }

  /**
   * Creates a new group and inserts it into the database.
   *
   * @param name The group name.
   * @param description The group description.
   * @param user_admin The group admin.
   * @param tags The group tags.
   */
  private void createGroup(String name, String description, String user_admin, String[] tags) {
    if (!checkGroupExists(name))
      try {
        conn = dao.getConnection();
        ps =
            conn.prepareStatement(
                "INSERT INTO Groups(name, DESCRIPTION, USER_ADMIN) VALUES(?, ?, ?)");
        ps.setString(1, name);
        ps.setString(2, description);
        ps.setString(3, user_admin);

            ps.execute();

            for(int i = 0; i < tags.length; i++){
                ps = conn.prepareStatement("INSERT INTO TAGS(GROUP_NAME, TAG) VALUES(?, ?)");
                ps.setString(1, name);
                ps.setString(2, tags[i]);
                ps.execute();
            }
            /* add admin to group**/
            User user = new User();
            user.joinGroup(this);

            ps = conn.prepareStatement("INSERT INTO EVENTS(NAME, DESCRIPTION, GROUP_NAME, DATE) VALUES(?,?,?,?)");
            ps.setString(1, "");
            ps.setString(2, "");
            ps.setString(3, name);
            ps.setString(4, "");
            ps.execute();

            ps.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

  /**
   * Method to retrieve all groups from the database.
   *
   * @return a ResultSet containing all groups.
   */
  public ResultSet getGroups() {
    try {
      conn = dao.getConnection();
      String SQL = "SELECT NAME , DESCRIPTION from GROUPS";
      ps = conn.prepareStatement(SQL);
      ResultSet rs = ps.executeQuery();

      return rs;
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Method to delete a specific group from the database. Includes all relative information like
   * deleting users from this group, posts, reports, etc.
   *
   * @param groupName The group name to delete.
   */
  public void deleteGroup(String groupName) {
    if (checkGroupExists(groupName)) {
      try {
        conn = dao.getConnection();
        ps = conn.prepareStatement("DELETE FROM GROUPS WHERE NAME=?");
        ps.setString(1, groupName);
        ps.execute();

            ps = conn.prepareStatement("DELETE FROM TAGS WHERE GROUP_NAME=?");
            ps.setString(1, groupName);
            ps.execute();

            conn.close();
            ps.close();
          } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
          }
          // delete posts, events (not yet), tags, reports
            Post p = new Post();
            p.deleteAllPostsFromGroup(groupName);

            Report.removeAllReportsFromGroup(groupName);

            User u = new User();
            u.deleteAllUsersFromGroup(groupName);
        }
    }

  /**
   * Method to search the database for the user's search terms.
   *
   * @param search The tag to search for.
   * @return a ResultSet of groups that contain the tag.
   */
  public ResultSet getSearch(String search) {
    try {
      conn = dao.getConnection();
      ps = conn.prepareStatement("SELECT NAME, DESCRIPTION FROM GROUPS WHERE NAME LIKE ?");
      ps.setString(1, search.concat("%"));

            ResultSet rs = ps.executeQuery();

            return rs;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

  /**
   * Method to check the database to see if a group exists with the same name.
   *
   * @param group The group name to check against.
   * @return A boolean if the group exists or not.
   */
  private boolean checkGroupExists(String group) {
    boolean exists = false;
    try {
      conn = dao.getConnection();
      ps = conn.prepareStatement("SELECT * FROM GROUPS WHERE NAME=?");
      ps.setString(1, group);

            ResultSet rs = ps.executeQuery();
            exists = rs.next();

            ps.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

  /**
   * Retrieves the group names where this user is an admin.
   *
   * @return A ResultSet of all groups where the user is an admin.
   */
  public ResultSet getUserGroups() {
    try {
      conn = dao.getConnection();
      String SQL =
          "SELECT NAME from GROUPS where USER_ADMIN = '"
              + Session.getInstance("").getUserName()
              + "'";
      ps = conn.prepareStatement(SQL);
      ResultSet rs = ps.executeQuery();

            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

  /**
   * Retrieves the name of the user that is the admin of a specified group.
   *
   * @param name The name of the group to check.
   * @return A string of the group admin's name.
   */
  public String getGroupAdmin(String name) {
    String admin = "";
    ResultSet rs = null;
    try {
      conn = dao.getConnection();
      ps = conn.prepareStatement("SELECT USER_ADMIN FROM GROUPS WHERE NAME=?");
      ps.setString(1, name);

            rs = ps.executeQuery();

            while (rs.next()) {
                admin = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return admin;
    }

  /**
   * Retrieves the description of a specified group.
   *
   * @param groupName The group name to get the description of.
   * @return A string of the group's description.
   */
  public String getGroupDescription(String groupName) {
    String desc = "";
    ResultSet rs = null;
    try {
      conn = dao.getConnection();
      ps = conn.prepareStatement("SELECT DESCRIPTION FROM GROUPS WHERE NAME=?");
      ps.setString(1, groupName);

            rs = ps.executeQuery();

            while (rs.next()) {
                desc = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return desc;
    }

  /**
   * Retrieves all users from a specified group.
   *
   * @param groupName The group name to get uses from.
   * @return An observable list with all group members.
   */
  public ObservableList<String> getAllUsers(String groupName) {
    ObservableList<String> userList = FXCollections.observableArrayList();
    ResultSet rs = null;
    int count = 1;
    try {
      conn = dao.getConnection();
      ps = conn.prepareStatement("SELECT USER_ID FROM USERS_GROUPS WHERE GROUP_ID=?");
      ps.setString(1, groupName);

            rs = ps.executeQuery();

            while (rs.next()) {
                String username = rs.getString(1);
                userList.add(username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return userList;
    }

  /**
   * Method to search the database for groups that contain any of the specified tags.
   *
   * @param tag The array of tags to search for.
   * @return A ResultSet of groups that have matching tags.
   */
  public ResultSet tagSearch(String[] tag) {
    try {
      conn = dao.getConnection();
      String SQL =
          "SELECT g.name, g.DESCRIPTION FROM GROUPS g, TAGS t WHERE t.TAG= '"
              + tag[0]
              + "' and t.GROUP_NAME = g.NAME ";
      // concat not working for some reason
      for (int j = 1; j < tag.length; j++)
        SQL +=
            ("UNION SELECT g.name, g.DESCRIPTION FROM GROUPS g, TAGS t WHERE t.TAG= '"
                + tag[j]
                + "' and t.GROUP_NAME = g.NAME ");

                ps = conn.prepareStatement(SQL);
                ResultSet rs = ps.executeQuery();

                return rs;
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
    }

    public ObservableList<String> wildCardTagSearch(String tag){
            if(tag.equals(""))
                return null;
        try {
            conn = dao.getConnection();
            String SQL = "SELECT TAG FROM  TAGS  WHERE TAG LIKE ?";
            ps = conn.prepareStatement(SQL);
            ps.setString(1, tag.concat("%"));


            ResultSet rs = ps.executeQuery();
            ObservableList<String> tags = FXCollections.observableArrayList();

            while(rs.next())
            tags.add(rs.getString(1));

            return tags;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

  /**
   * Method to remove a specified member from a specified group.
   *
   * @param username The name of the user to remove.
   * @param groupName The name of the group to remove the user from.
   */
  public void removeMember(String username, String groupName) {
    try {
      conn = dao.getConnection();
      ps = conn.prepareStatement("DELETE FROM USERS_GROUPS WHERE USER_ID=? AND GROUP_ID=?");
      ps.setString(1, username);
      ps.setString(2, groupName);

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
   * Method to check if a specified user is in a specified group.
   *
   * @param username The username of the user to check.
   * @param groupName The group name of the group to check.
   * @return A boolean if the user is in the group or not.
   */
  public boolean isUserInGroup(String username, String groupName) {
    try {
      conn = dao.getConnection();
      ps = conn.prepareStatement("SELECT * FROM USERS_GROUPS WHERE USER_ID=? AND GROUP_ID=?");
      ps.setString(1, username);
      ps.setString(2, groupName);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                return true;
            }

            conn.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

  /**
   * Method to update the group tags of the specified group.
   *
   * @param groupName The group name to set the tags.
   * @param tags The tags to set.
   */
  public void updateGroupTags(String groupName, List<String> tags) {
    // remove all tags before adding new ones
    this.removeAllTags(groupName);

      for (String s : tags) {
          try {
              conn = dao.getConnection();
              ps = conn.prepareStatement("INSERT INTO TAGS(GROUP_NAME, TAG) VALUES(?, ?)");
              ps.setString(1, groupName);
              ps.setString(2, s);

              ps.execute();

              conn.close();
              ps.close();
          } catch (SQLException | ClassNotFoundException e) {
              e.printStackTrace();
          }
      }
  }

  /**
   * Method to remove all tags from a specified group.
   *
   * @param groupName The group name to remove tags from.
   */
  public void removeAllTags(String groupName) {
    try {
      conn = dao.getConnection();
      ps = conn.prepareStatement("DELETE FROM TAGS WHERE GROUP_NAME=?");
      ps.setString(1, groupName);

            ps.execute();

            conn.close();
            ps.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

  /**
   * Method to return the name of the group.
   *
   * @return A string of the group name.
   */

    public void setEventDescription(String description, String groupName) {
        try {
            conn = dao.getConnection();
            ps = conn.prepareStatement("UPDATE EVENTS SET DESCRIPTION=? WHERE GROUP_NAME=?");
            ps.setString(1, description);
            ps.setString(2, groupName);

            ps.execute();

            ps.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setEventTitle(String title, String groupName) {
        try {
            conn = dao.getConnection();
            ps = conn.prepareStatement("UPDATE EVENTS SET NAME=? WHERE GROUP_NAME=?");
            ps.setString(1, title);
            ps.setString(2, groupName);

            ps.execute();

            ps.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setEventDate(String date, String groupName) {
        try {
            conn = dao.getConnection();
            ps = conn.prepareStatement("UPDATE EVENTS SET DATE=? WHERE GROUP_NAME=?");
            ps.setString(1, date);
            ps.setString(2, groupName);

            ps.execute();

            ps.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getEventTitle(String groupName){
       String title = "";
        try {
            conn = dao.getConnection();
            String SQL = "SELECT NAME FROM EVENTS  WHERE GROUP_NAME=?";
            ps = conn.prepareStatement(SQL);
            ps.setString(1, groupName);
            ResultSet rs = ps.executeQuery();


            while (rs.next()) {
            System.out.println(rs.getString(1));
            title = rs.getString(1);
                }
            return title;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return title;
    }

    public String getEventDescription(String groupName){
        String desc = "";
        try {
            conn = dao.getConnection();
            String SQL = "SELECT DESCRIPTION FROM EVENTS  WHERE GROUP_NAME=?";
            ps = conn.prepareStatement(SQL);
            ps.setString(1, groupName);
            ResultSet rs = ps.executeQuery();

            while(rs.next())
            desc = rs.getString(1);
            return desc;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return desc;
    }

    public String getEventDate(String groupName){
        String date = "";
        try {
            conn = dao.getConnection();
            String SQL = "SELECT DATE FROM EVENTS  WHERE GROUP_NAME=?";
            ps = conn.prepareStatement(SQL);
            ps.setString(1, groupName);
            ResultSet rs = ps.executeQuery();

            while(rs.next())
            date = rs.getString(1);
            return date;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return date;
    }
    @Override
    public String toString() {
        return name;
    }

}
