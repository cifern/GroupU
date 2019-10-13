package groupu.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {

  private static final int width = 416;
  private static final int height = 391;

  @FXML
  private Button btnInfo;

  public void actionInfo(ActionEvent actionEvent) {
    System.out.println("view group pressed");
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/group.fxml"));
      Stage stage = (Stage) btnInfo.getScene().getWindow();
      Scene scene = new Scene(loader.load());
      stage.setTitle("Group");
      //stage.setWidth(width);
      //stage.setHeight(height);
      stage.setResizable(true);
      stage.setScene(scene);
    } catch (IOException io) {
      io.printStackTrace();
    }

  }

  public void actionOpen(ActionEvent actionEvent) {
    System.out.println("view group pressed");
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/group.fxml"));
      Stage stage = (Stage) btnInfo.getScene().getWindow();
      Scene scene = new Scene(loader.load());
      stage.setTitle("Group");
      //stage.setWidth(width);
      //stage.setHeight(height);
      stage.setResizable(true);
      stage.setScene(scene);
    } catch (IOException io) {
      io.printStackTrace();
    }

  }

  public void actionSearch(ActionEvent actionEvent) {
    System.out.println("search pressed");
  }
}
