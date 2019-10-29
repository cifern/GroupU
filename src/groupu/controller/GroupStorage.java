package groupu.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GroupStorage {

    private static final String JdbcDriver = "org.h2.Driver";
    private static final String DatabaseUrl = "jdbc:h2:./res/UserDB";

    private final String user = "";
    private final String pass = "";

    private Connection conn = null;
    private PreparedStatement ps = null;

    public void createGroup(String name, String description, String user_admin) {
        try {
            Class.forName(JdbcDriver);
            conn = DriverManager.getConnection(DatabaseUrl, user, pass);
            ps = conn.prepareStatement("INSERT INTO Groups(name, DESCRIPTION, USER_ADMIN) VALUES(?, ?, ?)");
            ps.setString(1, name);
            ps.setString(2, description);
            ps.setString(3, user_admin);

            ps.execute();

            ps.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
