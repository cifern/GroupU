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
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;

public class GroupController {

  @FXML private Button btnBack;
  @FXML private Button btnPost;
  @FXML private TextArea txtPostBody;
  @FXML private ListView listPosts;
  @FXML private Tab tabAdmin;
  @FXML private Tab tabGroupInfo;
  @FXML private Tab tabPosts;

  private String groupName;
  private ArrayList<String> postList;
  private ObservableList<String> posts;

  @FXML
  public void initialize() {
    groupName = HomeController.GroupSelect;
    System.out.println("XXXXX - INITIALIZED GROUP VIEW FOR GROUP: " + groupName);
    updateListOfPosts();

    // check if user is admin and hide admin tab if not
    Group g = new Group();
    String admin = g.getGroupAdmin(groupName);
    if (!Session.getInstance("").getUserName().equals(admin)) {
      tabAdmin.setDisable(true);
    }
  }

  public void updateListOfPosts() {
    Post p = new Post();
    postList = p.getPostsByGroupName(groupName);
    posts = FXCollections.observableArrayList();
    for (String s : postList) {
      posts.add(s);
    }
    listPosts.setItems(posts);
  }

  public void actionPost(ActionEvent actionEvent) {
    if (txtPostBody.getText().length() > 0 && txtPostBody.getText().length() <= 300) {
      Post p = new Post();
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
  }
}
