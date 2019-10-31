/*** DATA ACCESS OBJECT ***/
package groupu.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAO {
        public Connection getConnection() throws SQLException, ClassNotFoundException {
            Class.forName("org.h2.Driver");
            return  DriverManager.getConnection("jdbc:h2:./res/UserDB", "", "");
        }
    }

