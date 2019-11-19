package groupu.controller;

import groupu.model.Group;
import groupu.model.Message;
import groupu.model.Session;
import groupu.model.User;
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

    private ObservableList<ObservableList> TableViewData;
    private ObservableList<String> messageFromList;
    private ObservableList<String> messageBodyList;
    private Object select;
    Group group = new Group();

    @FXML
    void initialize()
    {
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

    public void updateMessageList() {
      Message m = new Message();
      messageFromList = FXCollections.observableArrayList();
      Set<String> userSet = new LinkedHashSet<String>();
      ArrayList<String> messages = m.getAllMessagesFromUsers();

      userSet.addAll(messages);
      messageFromList.addAll(userSet);

      listMessageList.setItems(messageFromList);
    }

    public void actionMessagesClicked() {
      Message m = new Message();
      ArrayList<String> messages = new ArrayList<String>();
      try {
        String clickedUser = listMessageList.getSelectionModel().getSelectedItem().toString();
        messageBodyList = m.getMessagesFromUser(clickedUser);

        listMessageConversation.setItems(messageBodyList);
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
        Message m = new Message(toUser, messageBody);
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
    System.out.println("delete message");
  }

  public void actionReply() {
    String toUser = listMessageList.getSelectionModel().getSelectedItem().toString();
    String body = txtReplyText.getText();

    if (!body.isEmpty()) {
      if (body.length() > 0 && body.length() <= 300) {
        Message m = new Message(toUser, body);
        m.sendPrivateMessage();

        txtReplyText.clear();

        Alert a = new Alert(AlertType.INFORMATION);
        a.setContentText("Reply sent!");
        a.show();
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
    System.out.println("remove friend");
  }
}
