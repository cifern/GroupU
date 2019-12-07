package groupu.controller;

import groupu.model.Session;
import groupu.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

/** * Class for the Login view. */
public class LoginController {

  private User userStore;

  @FXML private Button btnRegister;
  @FXML private Button btnLogin;
  @FXML private TextField txtUsername;
  @FXML private TextField txtPassword;

  /** Method called when the scene loads. */
  @FXML
  void initialize() {
    txtPassword.setOnKeyPressed(
        event -> {
          if (event.getCode() == KeyCode.ENTER) {
            actionLogin();
            txtPassword.clear();
          }
        });
  }

  /**
   * * Method when login button is pressed.
   *
   */
  public void actionLogin() {
    Alert alert;

    String username = txtUsername.getText();
    String pass = txtPassword.getText();

    userStore = new User();

    boolean exists = userStore.checkUserExists(username);
    boolean passMatches = userStore.comparePassword(username, pass);

    if (exists && passMatches) {
      Session.getInstance("").cleanUserSession();
      Session session = Session.getInstance(username);
      Utilities.nextScene(btnLogin, "home", "Home - " + username);
    } else {
      alert = new Alert(AlertType.ERROR);
      alert.setContentText("Invalid login!");
      alert.show();
    }
  }

  /** * Method to transition to the register view. */
  public void actionRegister() {
    Utilities.nextScene(btnRegister, "register", "Register");
  }
}
