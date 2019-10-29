package groupu.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginController {
  static String currentUser;
  private UserStorage userStorage;

  private static final int width = 325;
  private static final int height = 275;

  @FXML private Button btnRegister;
  @FXML private Button btnLogin;
  @FXML private TextField txtUsername;
  @FXML private TextField txtPassword;

  public void actionLogin(ActionEvent actionEvent) {
    Alert alert;
    userStorage = new UserStorage();

    String user = txtUsername.getText();
    String pass = txtPassword.getText();

    boolean exists = userStorage.checkUserExists(user);
    boolean passMatches = userStorage.comparePassword(user, pass);

    if (exists && passMatches) {
      currentUser = user;
      Utilities.nextScene(btnLogin, "home", "Home - " + currentUser);
    } else {
      alert = new Alert(AlertType.ERROR);
      alert.setContentText("Invalid login!");
      alert.show();
    }

  }

  public void actionRegister(ActionEvent actionEvent) {
    Utilities.nextScene(btnRegister, "register", "Register");
  }
}
