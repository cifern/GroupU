package groupu.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class RegisterController {

  private static final int width = 325;
  private static final int height = 275;

  @FXML private Button btnBack;

  public void actionRegister(ActionEvent actionEvent) {
    System.out.println("registered");
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
