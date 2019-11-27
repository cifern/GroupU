package groupu.controller;

import groupu.model.Friend;
import groupu.model.Group;
import groupu.model.Message;
import groupu.model.Session;
import groupu.model.User;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;
import java.sql.ResultSet;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Callback;

public class HomeController{

    static String GroupSelect;

    @FXML private TextField searchGroupText;
    @FXML private Button btnInfo;
    @FXML private Button btnCreateGroup;
    @FXML private Button btnLogout;
    @FXML private Button btnSendMessage;
    @FXML private Button btnDeleteMessage;
    @FXML private TextField txtMessageTo;
    @FXML private TextField txtReplyText;
    @FXML private TextArea txtMessageBody;
    @FXML private TableView tableview;
    @FXML private TableColumn colName;
    @FXML private TableColumn colDescription;
    @FXML private ListView listviewAdmin;
    @FXML private ListView listviewJoined;
    @FXML private ListView listMessageList;
    @FXML private ListView listMessageConversation;
    @FXML private ListView listFriendsList;
    @FXML private TabPane homeTabPane;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
    private ObservableList<ObservableList> TableViewData;
    private ObservableList<String> messageBodies;
    private Object select;
    Group group = new Group();

    @FXML
    void initialize()
    {
      setupPlaceholders();
      setupFriendsListContextMenu();
      updateFriendsList();
      updateMessageList();
      buildData();

      /*** Tableview listener, Selects the entire row instead of 1 cell**/
      ObservableList<TablePosition> selectedCells = tableview.getSelectionModel().getSelectedCells() ;
      selectedCells.addListener((ListChangeListener.Change<? extends TablePosition> change) -> {
        if (selectedCells.size() > 0) {
          TablePosition selectedCell = selectedCells.get(0);
          int rowIndex = selectedCell.getRow();
          select = colName.getCellObservableValue(rowIndex).getValue();
        }
      });

      /*** owned groups listener**/
      listviewAdmin.getSelectionModel().getSelectedItem();
      listviewAdmin.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
          if(newValue!=null) {
            select = newValue;
            listviewJoined.getSelectionModel().select(null);
          }
        }
      });

      /*** joined groups listener**/
      listviewJoined.getSelectionModel().getSelectedItem() ;
      listviewJoined.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
          if(newValue!=null) {
            select = newValue;
            listviewAdmin.getSelectionModel().clearSelection();
          }
        }
      });
    }

    public void setupPlaceholders() {
      listviewJoined.setPlaceholder(new Label("No content"));
      listviewAdmin.setPlaceholder(new Label("No content"));
      listMessageConversation.setPlaceholder(new Label("No content"));
      listMessageList.setPlaceholder(new Label("No content"));
      listFriendsList.setPlaceholder(new Label("No content"));
    }

    public void updateFriendsList() {
      Friend f = new Friend();
      ObservableList<String> friends = FXCollections.observableArrayList();
      ArrayList<String> friendsList = f.getFriends();

      friends.addAll(friendsList);

      listFriendsList.setItems(friends);
    }

    public void updateMessageList() {
      Message m = new Message();
      ObservableList<String> messageFromList = FXCollections.observableArrayList();

      Set<String> userSet = new LinkedHashSet<String>();
      ArrayList<String> messages = m.getAllMessagesFromUsers();

      userSet.addAll(messages);
      messageFromList.addAll(userSet);

      listMessageList.setItems(messageFromList);
    }

  /**
   * I need ONE array that is populated with both TO AND FROM users.
   * This array will then be sorted by the Timestamp
   */
  public void actionMessagesClicked() {
      Message m = new Message();
      ObservableList<Message> messages;
      try {
        String clickedUser = listMessageList.getSelectionModel().getSelectedItem().toString();
        messages = m.getAllMessagesBetweenUsers(clickedUser, Session.getInstance("").getUserName());
        messageBodies = FXCollections.observableArrayList();

        for (Message msg : messages) {
          messageBodies.add(msg.getMessageBody());
        }

        listMessageConversation.setItems(messageBodies);

        System.out.println(messages.toString());
      } catch (NullPointerException e) {
        e.printStackTrace();
      }
    }

    public void buildData(){
      /** Populate group search tableview*/
      TableViewData = FXCollections.observableArrayList();

      User user = new User();
      ResultSet rsGroups = group.getGroups();

      /*** Data table added to searchlist ***/
      try {
        colName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
          public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
            return new SimpleStringProperty(param.getValue().get(0).toString());
          }
        });
        colDescription.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
          public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
            return new SimpleStringProperty(param.getValue().get(1).toString());
          }
        });
        while (rsGroups.next()) {
          //Iterate Row
          ObservableList<String> row = FXCollections.observableArrayList();
          for (int i = 1; i <= rsGroups.getMetaData().getColumnCount(); i++) {
            //Iterate Column
            row.add(rsGroups.getString(i));
          }
          TableViewData.add(row);
        }
        //ADDED TO TableView
        tableview.setItems(TableViewData);
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Error on Building group table");
      }

      /** Data added to users group list **/
      try {
        ResultSet rsUserGroups = group.getUserGroups();
        while (rsUserGroups.next()) {
          String current = rsUserGroups.getString("name");
          ObservableList<String> list = FXCollections.observableArrayList(current);
          listviewAdmin.getItems().addAll(list);
        }
      } catch (SQLException e) {
        e.printStackTrace();
          System.out.println("Error on Building user groups table");
      }

      /** Data added to joined group list **/
      try {
        ResultSet rUserGroups = user.getJoinedGroups();
        while (rUserGroups.next()) {
          String current2 = rUserGroups.getString("name");
          ObservableList<String> list2 = FXCollections.observableArrayList(current2);
          listviewJoined.getItems().addAll(list2);
        }
      } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Error on Building user groups table");
      }
    }

    /** open creategroup.fxml**/
  public void actionCreateGroup(ActionEvent actionEvent) {
    Utilities.nextScene(btnCreateGroup, "creategroup", "Create New Group");
  }


  public void actionOpenGroup(ActionEvent actionEvent) {
    if(select != null) {
      GroupSelect = select.toString();
      System.out.println("view group pressed" + GroupSelect);
      Utilities.nextScene(btnInfo, "group", GroupSelect);
    }
  }

  public void actionOpenUserGroups(ActionEvent actionEvent) {
    System.out.println("view group pressed");
    if(select != null) {
      GroupSelect = select.toString();
      Utilities.nextScene(btnInfo, "group", GroupSelect);
    }
  }

  public void actionSearch(ActionEvent actionEvent) {
    System.out.println("search pressed");


    System.out.println("search pressed");
    TableViewData = FXCollections.observableArrayList();

    User user = new User();
    ResultSet rsGroups = group.getSearch(searchGroupText.getText());

    /*** Data table added to searchlist ***/
    try {
      colName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
          return new SimpleStringProperty(param.getValue().get(0).toString());
        }
      });
      colDescription.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
          return new SimpleStringProperty(param.getValue().get(1).toString());
        }
      });
      while (rsGroups.next()) {
        //Iterate Row
        ObservableList<String> row = FXCollections.observableArrayList();
        for (int i = 1; i <= rsGroups.getMetaData().getColumnCount(); i++) {
          //Iterate Column
          row.add(rsGroups.getString(i));
        }
        TableViewData.add(row);
      }
      //ADDED TO TableView
      tableview.setItems(TableViewData);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Error on Building group table");
    }
  }

  public void actionLogout() {
    Session.getInstance("").cleanUserSession();
    Utilities.nextScene(btnLogout, "login", "Login");
  }

  public void actionSendMessage() {
    User u = new User();
    String toUser = txtMessageTo.getText();
    String messageBody = txtMessageBody.getText();

    if (u.checkUserExists(toUser)) {
      if (!Session.getInstance("").getUserName().equals(toUser)) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        Message m = new Message(toUser, messageBody, sdf.format(time));
        m.sendPrivateMessage();

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText("Message sent!");
        alert.show();

        txtMessageTo.clear();
        txtMessageBody.clear();
      } else {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setContentText("You can't send a message to yourself!");
        alert.show();
      }
    } else {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setContentText("Username doesn't exist!");
      alert.show();
    }
  }

  public void actionDeleteMessage() {
    String username = getSelectedFromConversationList();
    Message m = new Message(username, null);
    m.deleteAllMessages();

    updateMessageList();
    listMessageConversation.getItems().clear();
  }

  public void actionReply() {
    String toUser = listMessageList.getSelectionModel().getSelectedItem().toString();
    String body = txtReplyText.getText();
    Timestamp time = new Timestamp(System.currentTimeMillis());

    if (!body.isEmpty()) {
      if (body.length() > 0 && body.length() <= 300) {
        Message m = new Message(toUser, body, sdf.format(time));
        m.sendPrivateMessage();

        txtReplyText.clear();
        actionMessagesClicked();
      } else {
        Alert a = new Alert(AlertType.ERROR);
        a.setContentText("Message must be between 1 and 300!");
        a.show();
      }
    } else {
      Alert a = new Alert(AlertType.ERROR);
      a.setContentText("Empty message body!");
      a.show();
    }

  }

  public void actionRemoveFriend() {
    String username = getSelectedFromFriendsList();
    if (username != null) {
      Friend f = new Friend(username);
      f.removeFriend();

      updateFriendsList();
    } else {
      Alert a = new Alert(AlertType.ERROR);
      a.setContentText("You did not select a friend!");
      a.show();
    }
  }

  public void setupFriendsListContextMenu() {
    ContextMenu cm = new ContextMenu();
    MenuItem itemRemoveFriend = new MenuItem("Remove Friend");
    cm.getItems().add(itemRemoveFriend);
    listFriendsList.setContextMenu(cm);

    itemRemoveFriend.setOnAction(
        event -> {
          actionRemoveFriend();
    });
  }

  public String getSelectedFromFriendsList() {
    try {
      return listFriendsList.getSelectionModel().getSelectedItem().toString();
    } catch (Exception e) {
      return null;
    }
  }

  public String getSelectedFromConversationList() {
    try {
      return listMessageList.getSelectionModel().getSelectedItem().toString();
    } catch (Exception e) {
      return null;
    }
  }
}
