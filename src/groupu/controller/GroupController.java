package groupu.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class GroupController {

  @FXML
  private Button btnBack;

  public void actionPost(ActionEvent actionEvent) {
  }

  public void actionJoinGroup(ActionEvent actionEvent) {
  }

  public void actionReportGroup(ActionEvent actionEvent) {
  }

  public void actionBack(ActionEvent actionEvent) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/home.fxml"));
      Stage stage = (Stage) btnBack.getScene().getWindow();
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

  public void actionKickMember(ActionEvent actionEvent) {
  }

  public void actionRemoveReport(ActionEvent actionEvent) {
  }

  public void actionDeleteGroup(ActionEvent actionEvent) {
  }

  public void actionSaveChanges(ActionEvent actionEvent) {
  }
}
