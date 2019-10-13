package groupu.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class LoginController {

  private static final int width = 325;
  private static final int height = 275;

  @FXML private Button btnRegister;

  public void actionLogin(ActionEvent actionEvent) {
    System.out.println("login pressed");
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/home.fxml"));
      Stage stage = (Stage) btnRegister.getScene().getWindow();
      Scene scene = new Scene(loader.load());
      stage.setTitle("Home");
      //stage.setWidth(width);
      //stage.setHeight(height);
      stage.setResizable(true);
      stage.setScene(scene);
    } catch (IOException io) {
      io.printStackTrace();
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
