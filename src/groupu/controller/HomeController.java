package groupu.controller;

import groupu.model.Group;
import groupu.model.Session;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.sql.Connection;
import java.sql.ResultSet;

import javafx.util.Callback;

public class HomeController{

    static String GroupSelect;

    @FXML private Button btnInfo;
    @FXML private Button btnCreateGroup;
    @FXML private TableView tableview;
    @FXML private TableColumn colName;
    @FXML private TableColumn colDescription;
    @FXML private Text txtUser;
    @FXML private ListView listview;

    private ObservableList<ObservableList> TableViewData;
    private Object select;

    @FXML
    void initialize()
    {
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

      /*** Listview listener**/
      listview.getSelectionModel().getSelectedItem() ;
      listview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
          System.out.println("ListView selection changed from oldValue = "
                  + oldValue + " to newValue = " + newValue);
          select = newValue;
        }
      });
    }

    public void buildData(){
      /** Populate group search tableview*/
      TableViewData = FXCollections.observableArrayList();
      GroupStorage groupStore = new GroupStorage();
      ResultSet rsGroups = groupStore.getGroups();

      /*** Data added to ObservableList ***/
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
        ResultSet rsUserGroups = groupStore.getUserGroups();
        while (rsUserGroups.next()) {
          String current = rsUserGroups.getString("name");
          ObservableList<String> list = FXCollections.observableArrayList(current);
          listview.getItems().addAll(list);
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

    if(!tableview.getSelectionModel().isEmpty()) {
      GroupSelect = select.toString();
      System.out.println("view group pressed" + GroupSelect);
      Utilities.nextScene(btnInfo, "group", GroupSelect);
    }
  }

  public void actionOpenUserGroups(ActionEvent actionEvent) {
    System.out.println("view group pressed");
    if(!listview.getSelectionModel().isEmpty()) {
      GroupSelect = select.toString();
      Utilities.nextScene(btnInfo, "group", GroupSelect);
    }
  }

  public void actionSearch(ActionEvent actionEvent) {
    System.out.println("search pressed");
  }


}
