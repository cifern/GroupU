package groupu.model;

public class User {

    private String firstname;
    private String lastname;
    private String username;
    private String password;

    public User(String firstname, String lastname, String username, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "First name: " + this.firstname + "\n" +
            "Last name: " + this.lastname + "\n" +
            "Username: " + this.username + "\n" +
            "Password: " + this.password;
    }
}