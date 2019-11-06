package groupu.model;

import groupu.controller.HomeController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

    public String toString() {
        return name;
    }
}
