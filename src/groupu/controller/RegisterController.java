package groupu.controller;

import  groupu.model.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
/*** ***/
public class RegisterController {

  private static final int minUsernameSize = 3;
  private static final int maxUsernameSize = 20;
  private static final int minPassSize = 6;
  private static final int maxPassSize = 30;

  @FXML private Button btnBack;
  @FXML private TextField txtUsername;
  @FXML private TextField txtPassword;


  public void actionRegister(ActionEvent actionEvent) {
    User u = new User();
    Alert alert;

    boolean exists = u.checkUserExists(txtUsername.getText());

    if (!exists) {
      if (txtUsername.getLength() >= minUsernameSize && txtUsername.getLength() <= maxUsernameSize) {
        if (txtPassword.getLength() >= minPassSize && txtPassword.getLength() <= maxPassSize) {
          if (!txtUsername.getText().contains(" ")) {
            if (!txtPassword.getText().contains(" ")) {
              User user = new User(txtUsername.getText(), txtPassword.getText());
              user.createUser();
              actionBack(actionEvent);
            } else {
              alert = new Alert(AlertType.ERROR);
              alert.setContentText("Password cannot have spaces!");
              alert.show();
            }
          } else {
            alert = new Alert(AlertType.ERROR);
            alert.setContentText("Username cannot have spaces!");
            alert.show();
          }
        } else {
          alert = new Alert(AlertType.ERROR);
          alert.setContentText("Password must be between 6 and 30!");
          alert.show();
        }
      } else {
        alert = new Alert(AlertType.ERROR);
        alert.setContentText("Username must between 3 and 20!");
        alert.show();
        }
      } else {
        alert = new Alert(AlertType.ERROR);
        alert.setContentText("Username already exists!");
        alert.show();
    }
  }

  public void actionBack(ActionEvent actionEvent) {
    Utilities.nextScene(btnBack, "login", "Login");
  }
}
