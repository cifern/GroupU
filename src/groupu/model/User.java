package groupu.model;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

final public class User {

    private String firstname;
    private String lastname;
    private final String username;

    private DAO dao = new DAO() ;
    private Connection conn = null;
    private PreparedStatement ps = null;

    public User(){
            this.username = "test";
    }
    public User(String username) {
        this.username = username;

    }

    public User(String firstname, String lastname, String username) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;

    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getUsername() {
        return username;
    }


    public void createUser(String firstname, String lastname, String username, String password) {
        try {
            conn = dao.getConnection();

            ps = conn.prepareStatement("INSERT INTO Users(firstname, lastname, username, password) VALUES(?, ?, ?, ?)");
            ps.setString(1, firstname);
            ps.setString(2, lastname);
            ps.setString(3, username);
            ps.setString(4, password);

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

    public void joinGroup(String groupName){
        try {
            conn = dao.getConnection();

            ps = conn.prepareStatement("INSERT INTO USERS_GROUPS(USER_ID, GROUP_ID) VALUES(?, ?)");
            ps.setString(1, Session.getInstance("").getUserName());
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

    final public String getUser(){
        return username;
    }
    @Override
    public String toString() {
        return "First name: " + this.firstname + "\n" +
            "Last name: " + this.lastname + "\n" +
            "Username: " + this.username + "\n" ;
    }
}