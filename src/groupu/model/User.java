package groupu.model;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

final public class User {

    private String username;
    private String password;

    private DAO dao = new DAO() ;
    private Connection conn = null;
    private PreparedStatement ps = null;

    public User(){
        this.username = "test";
    }

    public User(String username) {
        this.username = username;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void createUser() {
        try {
            conn = dao.getConnection();

            ps = conn.prepareStatement("INSERT INTO Users(username, password) VALUES(?, ?)");
            ps.setString(1, this.username);
            ps.setString(2, this.password);

            ps.execute();

            ps.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkUserExists(String username) {
        boolean exists = false;
        try {
            conn = dao.getConnection();
            ps = conn.prepareStatement("SELECT * FROM USERS WHERE USERNAME=?");
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            // rs.next() is false if the set is empty
            exists = rs.next();

            // close stuff
            ps.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }

    public boolean comparePassword(String username, String password) {
        boolean matchedPass = false;
        try {
            conn = dao.getConnection();
            ps = conn.prepareStatement("SELECT * FROM USERS WHERE USERNAME=? AND PASSWORD=?");
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            // rs.next() is not empty if both user and pass are in the same row
            matchedPass = rs.next();

            // close stuff
            ps.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return matchedPass;
    }

    public void joinGroup(Group group){
        try {
            conn = dao.getConnection();
            ps = conn.prepareStatement("SELECT * FROM USERS_GROUPS WHERE USER_ID=? AND GROUP_ID=?");
            ps.setString(1, Session.getInstance("").getUserName());
            ps.setString(2, group.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next())
                return;


                ps = conn.prepareStatement("INSERT INTO USERS_GROUPS(USER_ID, GROUP_ID) VALUES(?, ?)");
                ps.setString(1, Session.getInstance("").getUserName());
                ps.setString(2, group.toString());
                ps.execute();

            ps.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getJoinedGroups(){
        try {
            conn = dao.getConnection();
            String SQL = "select groups.name from USERS_GROUPS, GROUPS where users_groups.USER_ID =? AND groups.NAME = users_groups.group_ID";
            ps = conn.prepareStatement(SQL);
            ps.setString(1, Session.getInstance("").getUserName());

            ResultSet rs = ps.executeQuery();

            return rs;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    final public String getUser(){
        return username;
    }

    @Override
    public String toString() {
        return "Username: " + this.username + "\n" ;
    }

}