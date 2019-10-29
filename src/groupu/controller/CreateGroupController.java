package groupu.controller;

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

public class CreateGroupController{

    private static final int maxNameLength = 50;
    private static final int maxDescriptionLength = 80;

    @FXML private Button btnCancel;
    @FXML private TextField txtGroupName;
    @FXML private TextArea txtDescription;

    Alert alert;

    public void actionCreateGroup(ActionEvent actionEvent) {
        GroupStorage groupStorage;

        if ( (txtGroupName.getLength() > 0 && txtGroupName.getLength() < maxNameLength)
                && (txtDescription.getLength() > 0 && txtDescription.getLength() < maxDescriptionLength )) {

            User user = new User();
            groupStorage = new GroupStorage();
            groupStorage.createGroup(txtGroupName.getText(), txtDescription.getText(),  user.getUser());
            System.out.println(user.getUser());

            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("GROUP CREATED");
            alert.show();
            actionCancel(actionEvent);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Too many characters!");
            alert.show();
        }
    }

    public void actionCancel(ActionEvent actionEvent) {
        Utilities.nextScene(btnCancel, "home", "Home");
    }
}


