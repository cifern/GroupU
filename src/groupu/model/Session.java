package groupu.model;

import java.util.HashSet;
import java.util.Set;

public class Session {
    private static Session instance;

    private String userName;
    private Set<String> privileges;

    private Session(String userName) {
        this.userName = userName;
    }

    public static Session getInstance(String userName) {
        if(instance == null) {
            instance = new Session(userName);
        }
        return instance;
    }

    public String getUserName() {
        return userName;
    }

    public Set<String> getPrivileges() {
        return privileges;
    }

    public void cleanUserSession() {
        userName = "";// or null
        instance = null;
        privileges = new HashSet<>();// or null
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "userName='" + userName + '\'' +
                ", privileges=" + privileges +
                '}';
    }
}
