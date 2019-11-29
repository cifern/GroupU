package groupu.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public final class Group {
    private String name = "";
    private String description;
    private String admin;
    private String tags[];

    private DAO dao = new DAO();
    private Connection conn = null;
    private PreparedStatement ps = null;

    public Group(){}

    public Group(String groupName){
        this.name = groupName;
    }

    public Group(String name, String description, String admin,String[] tags)
    {
        if(!checkGroupExists(name))
          createGroup(name, description, admin, tags);
    }

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


    public String[] getTags(){
        String[] tags = new String[10];
                return tags;
    }

    private void createGroup(String name, String description, String user_admin, String[] tags) {
        if(!checkGroupExists(name))
        try {
            conn =  dao.getConnection();
            ps = conn.prepareStatement("INSERT INTO Groups(name, DESCRIPTION, USER_ADMIN) VALUES(?, ?, ?)");
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
            /** add admin to group**/
            User user = new User();
            user.joinGroup(this);

            ps.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getGroups(){
        try {
            conn = dao.getConnection();
            String SQL = "SELECT NAME , DESCRIPTION from GROUPS";
            ps= conn.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();

            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getUsers(String groupName){
            try {
                conn = dao.getConnection();
                String SQL = "select users.USERNAME, groups.name\n" +
                        "from USERS_GROUPS, USERS, GROUPS\n" +
                        "where users.USERNAME=users_groups.USER_ID AND groups.NAME = users_groups.group_ID";
                ps= conn.prepareStatement(SQL);
                ResultSet rs = ps.executeQuery();
                return rs;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
    }
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
    public ResultSet getSearch(String search){
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

    public ResultSet getUserGroups(){
        try {
            conn  = dao.getConnection();
            String SQL = "SELECT NAME  from GROUPS where USER_ADMIN = '" + Session.getInstance("").getUserName() +"'";
            ps= conn.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();

            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    // how you're supposed to implement equals
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        Group that = (Group) other;
        return (this.name.equals(that.name)) && (this.description == that.description);
    }

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

    public ResultSet tagSearch(String[] tag){

            try {
                conn = dao.getConnection();
                ps = conn.prepareStatement("SELECT g.name, g.DESCRIPTION FROM GROUPS g, TAGS t WHERE t.TAG=? and t.GROUP_NAME = g.NAME " +
                        "UNION SELECT g.name, g.DESCRIPTION FROM GROUPS g, TAGS t WHERE t.TAG=? and t.GROUP_NAME = g.NAME " +
                        "UNION SELECT g.name, g.DESCRIPTION FROM GROUPS g, TAGS t WHERE t.TAG=? and t.GROUP_NAME = g.NAME " +
                        "UNION SELECT g.name, g.DESCRIPTION FROM GROUPS g, TAGS t WHERE t.TAG=? and t.GROUP_NAME = g.NAME " +
                        "UNION SELECT g.name, g.DESCRIPTION FROM GROUPS g, TAGS t WHERE t.TAG=? and t.GROUP_NAME = g.NAME " +
                        "UNION SELECT g.name, g.DESCRIPTION FROM GROUPS g, TAGS t WHERE t.TAG=? and t.GROUP_NAME = g.NAME " +
                        "UNION SELECT g.name, g.DESCRIPTION FROM GROUPS g, TAGS t WHERE t.TAG=? and t.GROUP_NAME = g.NAME " +
                        "UNION SELECT g.name, g.DESCRIPTION FROM GROUPS g, TAGS t WHERE t.TAG=? and t.GROUP_NAME = g.NAME " +
                        "UNION SELECT g.name, g.DESCRIPTION FROM GROUPS g, TAGS t WHERE t.TAG=? and t.GROUP_NAME = g.NAME " +
                        "UNION SELECT g.name, g.DESCRIPTION FROM GROUPS g, TAGS t WHERE t.TAG=? and t.GROUP_NAME = g.NAME " );


                for(int i = 1; i<=10; i++)
                    ps.setString(i, tag[i-1]);


                ResultSet rs = ps.executeQuery();

                return rs;


            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            return null;

        }

        
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

    @Override
    public String toString() {
        return name;
    }

}
