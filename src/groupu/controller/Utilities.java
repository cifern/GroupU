package groupu.controller;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Utilities {
  public static void nextScene(Button btn, String fxmlName, String title) {
    try {
      FXMLLoader loader = new FXMLLoader(Utilities.class.getResource("../view/" + fxmlName + ".fxml"));
      Stage stage = (Stage) btn.getScene().getWindow();
      Scene scene = new Scene(loader.load());
      stage.setTitle(title);
      stage.setScene(scene);
    } catch (IOException io) {
      io.printStackTrace();
    }
  }
}
