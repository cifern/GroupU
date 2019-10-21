package groupu.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

  private UserStorage userStorage;

  private static final int width = 325;
  private static final int height = 275;

  @FXML private Button btnRegister;
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
      // TODO: Go to 'home' when logged in instead of an alert
      alert = new Alert(AlertType.CONFIRMATION);
      alert.setContentText("CORRECT -- LOGGED IN");
      alert.show();

      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/home.fxml"));
        Stage stage = (Stage) btnRegister.getScene().getWindow();
        Scene scene = new Scene(loader.load());
        stage.setTitle("Home");

        stage.setResizable(true);
        stage.setScene(scene);
      } catch (IOException io) {
        io.printStackTrace();
      }


    } else {
      alert = new Alert(AlertType.ERROR);
      alert.setContentText("Invalid login!");
      alert.show();
    }

  }

  public void actionRegister(ActionEvent actionEvent) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/register.fxml"));
      Stage stage = (Stage) btnRegister.getScene().getWindow();
      Scene scene = new Scene(loader.load());
      stage.setTitle("Register");
      stage.setWidth(width);
      stage.setHeight(height);
      stage.setResizable(false);
      stage.setScene(scene);
    } catch (IOException io) {
      io.printStackTrace();
    }
  }
}
