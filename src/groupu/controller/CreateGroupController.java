package groupu.controller;

import groupu.model.Group;
import groupu.model.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

/**
 * class that represents the group controller
 *
 * @author ds-91
 * @author markopetrovic239
 *
 */
public class CreateGroupController {

    private static final int maxNameLength = 50;
    private static final int maxDescriptionLength = 200;

    private String[] tags = new String[10];
    private int tagCount = 0;

    private Group group;

    @FXML private Button btnCancel;
    @FXML private TextField txtGroupName;
    @FXML private TextArea txtDescription;
    @FXML private TextField txtTag;


    Alert alert;

    @FXML
    /***
     * method to initialize promt text for group name and group description
     */
        void initialize() {
        txtGroupName.setPromptText("Group Name");
        txtDescription.setPromptText("200 character description of your group");

        txtTag.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        ActionEvent a = null;
                        actionAddTag(a);
                        txtTag.clear();
                    }
        });

        /*** block spaces in tag field***/
        txtTag.textProperty().addListener(
                (observable, old_value, new_value) -> {
                    if(new_value.contains(" "))
                        txtTag.setText(old_value);
                }
        );
    }
    /***
     * creates group with tag's, description and group name
     * @param actionEvent
     ***/
    public void actionCreateGroup(ActionEvent actionEvent) {
        if (txtGroupName.getLength() > 0 && txtGroupName.getLength() < maxNameLength) {
            if (txtDescription.getLength() > 0 && txtDescription.getLength() < maxDescriptionLength) {
                if (!(txtGroupName.getText().startsWith(" ") || txtGroupName.getText().endsWith(" "))) {
                    if (!(txtDescription.getText().startsWith(" ") || txtDescription.getText().endsWith(" "))) {
                        Group group = new Group(
                            txtGroupName.getText(), txtDescription.getText(),
                            Session.getInstance("").getUserName(), tags);
                        Alert alert = new Alert(AlertType.CONFIRMATION);
                        alert.setContentText("Successfully created group!");
                        alert.showAndWait();
                        actionCancel(null);
                    } else {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setContentText("Description cannot start or end with a space!");
                        alert.show();
                    }
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setContentText("Group name cannot start or end with a space!");
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Group description must be between 1 and " + maxDescriptionLength + "!");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Group name must be between 1 and " + maxNameLength + "!");
            alert.show();
            //actionCancel(actionEvent);
        }


    }

    /***
     * @param actionEvent
     */
    public void actionCancel(ActionEvent actionEvent) {
        Utilities.nextScene(btnCancel, "home", "Home");
    }

/***
 * @param actionEvent
 * If tag is already made
 *
 **/
    public void actionAddTag(ActionEvent actionEvent) {
        for(int i = 0; i < tagCount; i++)
            if(tags[i].equals(txtTag.getText())){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Duplicate tag!");
                alert.show();
                return;
        }

        if(txtTag.getLength() > 0 && txtTag.getLength() < 30 && tagCount<10) {
            tags[tagCount] = txtTag.getText();
            tagCount++;
            txtTag.clear();
        }
        else if (tagCount >= 10){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Tag limit reached!");
            alert.show();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Tag is too long! (30 characters)");
            alert.show();
        }

    }
}


