package groupu.controller;

import groupu.model.Group;
import groupu.model.Post;
import groupu.model.Session;
import java.util.ArrayList;

import groupu.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

public class GroupController {

  @FXML private Button btnBack;
  @FXML private Button btnPost;
  @FXML private TextArea txtPostBody;
  @FXML private ListView listPosts;
  @FXML private TabPane tabPane;
  @FXML private Tab tabAdmin;
  @FXML private Tab tabGroupInfo;
  @FXML private Tab tabPosts;
  @FXML private Label labelGroupName;
  @FXML private Label labelGroupDescription;
  @FXML private ListView listMemberList;
  @FXML private TextArea txtGroupDescription;

  private String groupName;
  private ArrayList<String> postList;
  private ObservableList<String> posts;
  private ObservableList<String> userList;

  private Group g = new Group();
  private Post p = new Post();

  @FXML
  public void initialize() {
    groupName = HomeController.GroupSelect;

    updateTabs();
    updateGroupInfo();
    updateListOfPosts();
    updateListOfUsers();
  }

  public void updateTabs() {
    if (!Session.getInstance("").getUserName().equals(g.getGroupAdmin(groupName))) {
      tabPane.getTabs().remove(tabAdmin);
    }
  }

  public void updateListOfUsers() {
    ObservableList<String> userList = g.getAllUsers(groupName);
    listMemberList.setItems(userList);
  }

  public void updateGroupInfo() {
    labelGroupName.setText(groupName);
    labelGroupDescription.setText(g.getGroupDescription(groupName));
  }

  public void updateListOfPosts() {
    postList = p.getPostsByGroupName(groupName);
    posts = FXCollections.observableArrayList();
    for (String s : postList) {
      posts.add(s);
    }
    listPosts.setItems(posts);
  }

  public void actionPost(ActionEvent actionEvent) {
    if (txtPostBody.getText().length() > 0 && txtPostBody.getText().length() <= 300) {
      String poster = Session.getInstance("").getUserName();
      String data = txtPostBody.getText();

      p.createPost(data, poster, groupName);

      posts.add(poster + ": " + data);
      listPosts.refresh();
      txtPostBody.clear();
    }
  }

  public void actionJoinGroup(ActionEvent actionEvent) {
    User user = new User();
    Group group = new Group(HomeController.GroupSelect);

    user.joinGroup(group);
  }

  public void actionReportGroup(ActionEvent actionEvent) {
  }

  public void actionBack(ActionEvent actionEvent) {
    Utilities.nextScene(btnBack, "home", "Home");
  }

  public void actionKickMember(ActionEvent actionEvent) {
  }

  public void actionRemoveReport(ActionEvent actionEvent) {
  }

  public void actionDeleteGroup(ActionEvent actionEvent) {
  }

  public void actionSaveChanges(ActionEvent actionEvent) {
    Alert alert;

    System.out.println("pressed save changes");
    if (txtGroupDescription.getLength() > 0 && txtGroupDescription.getLength() <= 200) {
      g.setDescription(txtGroupDescription.getText(), groupName);

      alert = new Alert(AlertType.CONFIRMATION);
      alert.setContentText("Updated group description!");
      alert.show();

      txtGroupDescription.clear();
      updateGroupInfo();
    } else {
      alert = new Alert(AlertType.ERROR);
      alert.setContentText("Description must be between 1 and 200 characters!");
      alert.show();;
    }
  }
}
