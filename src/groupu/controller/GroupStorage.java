package groupu.controller;

import groupu.model.Session;

import java.sql.*;

public class GroupStorage {

    private static final String JdbcDriver = "org.h2.Driver";
    private static final String DatabaseUrl = "jdbc:h2:./res/UserDB";

    private final String user = "";
    private final String pass = "";

    private Connection conn = null;
    private PreparedStatement ps = null;

    public void createGroup(String name, String description, String user_admin, String[] tags) {
        try {
            Class.forName(JdbcDriver);
            conn = DriverManager.getConnection(DatabaseUrl, user, pass);
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
            conn = DriverManager.getConnection(DatabaseUrl, user, pass);
            String SQL = "SELECT NAME , DESCRIPTION from GROUPS";
            ResultSet rs = conn .createStatement().executeQuery(SQL);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getUserGroups(){
        try {
            conn = DriverManager.getConnection(DatabaseUrl, user, pass);
            String SQL = "SELECT NAME  from GROUPS where USER_ADMIN = '" + Session.getInstance("").getUserName() +"'";
            ResultSet rs = conn .createStatement().executeQuery(SQL);
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}

