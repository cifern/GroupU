package groupu.controller;

import groupu.model.Group;
import groupu.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateGroupController {


    @FXML private Button btnCancel;
    @FXML private TextField txtGroupName;
    @FXML private TextArea txtDescription;

    Alert alert;

    public void actionCreateGroup(ActionEvent actionEvent) {
        GroupStorage groupStorage;
        //boolean exists = false;

       // exists = groupStorage.checkUserExists(txtUsername.getText());

        if ( txtGroupName.getLength() > 0 && txtDescription.getLength() > 0 ) {

            groupStorage = new GroupStorage();
            groupStorage.createGroup(txtGroupName.getText(), txtDescription.getText());

            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("GROUP CREATED");
            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Username already exists or invalid information!");
            alert.show();
        }
    }

    public void actionCancel(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/home.fxml"));
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setTitle("Home");

            stage.setResizable(true);
            stage.setScene(scene);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}


