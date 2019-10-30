package groupu.model;


final public class User {

    private String firstname;
    private String lastname;
    private final String username;


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