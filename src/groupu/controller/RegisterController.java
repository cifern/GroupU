package groupu.controller;

import groupu.model.User;
import java.io.IOException;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {

  private static final int width = 325;
  private static final int height = 275;

  private static final int minUsernameSize = 3;
  private static final int minPassSize = 6;

  @FXML private Button btnBack;
  @FXML private TextField txtUsername;
  @FXML private TextField txtFirstname;
  @FXML private TextField txtLastname;
  @FXML private TextField txtPassword;

  // TODO: Go to home view after user registers
  public void actionRegister(ActionEvent actionEvent) {
    UserStorage userStorage = new UserStorage();
    boolean exists = false;

    exists = userStorage.checkUserExists(txtUsername.getText());

    if (!exists && txtFirstname.getLength() > 0 && txtLastname.getLength() > 0 &&
        txtUsername.getLength() >= minUsernameSize && txtPassword.getLength() > minPassSize) {

      User tempUser = new User(txtFirstname.getText(), txtLastname.getText(),
          txtUsername.getText(), txtPassword.getText());
      userStorage = new UserStorage();
      userStorage.createUser(tempUser.getFirstname(), tempUser.getLastname(),
          tempUser.getUsername(), tempUser.getPassword());
      actionBack(actionEvent);
    } else {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setContentText("Username already exists or invalid information!");
      alert.show();
    }
  }

  public void actionBack(ActionEvent actionEvent) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/login.fxml"));
      Stage stage = (Stage) btnBack.getScene().getWindow();
      Scene scene = new Scene(loader.load());
      stage.setTitle("Login");
      stage.setWidth(width);
      stage.setHeight(height);
      stage.setResizable(false);
      stage.setScene(scene);
    } catch (IOException io) {
      io.printStackTrace();
    }
  }
}
