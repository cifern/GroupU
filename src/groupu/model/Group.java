package groupu.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        this.name = name;
    }

    public Group(String name, String description, String admin,String[] tags)
    {
        if(!checkGroupExists(name))
        createGroup(name, description,admin,tags);
    }

    public String[] getTags(){
        String[] tags = new String[10];
                return tags;
    }

    private void createGroup(String name, String description, String user_admin, String[] tags) {
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
            ResultSet rs = conn.createStatement().executeQuery(SQL);
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
                ResultSet rs = conn.createStatement().executeQuery(SQL);
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
            ps = conn.prepareStatement("SELECT * FROM GROU WHERE NAME=?");
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
            ResultSet rs = conn .createStatement().executeQuery(SQL);
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

    public String toString() {
        return name + " " + description;
    }
}
