package groupu.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.sql.Connection;
import java.sql.ResultSet;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;


public class HomeController {

  private static final int width = 416;
  private static final int height = 391;

  @FXML
  private Button btnInfo;
  @FXML
  private Button btnCreateGroup;
  @FXML
  private TableView tableview;
  @FXML private TableColumn colName;
  @FXML private TableColumn colDescription;

    private ObservableList<ObservableList> data;
    private static final String JdbcDriver = "org.h2.Driver";
    private static final String DatabaseUrl = "jdbc:h2:./res/UserDB";

    private final String user = "";
    private final String pass = "";

    private Connection c = null;
    private PreparedStatement ps = null;

    @FXML
    void initialize()
    {
      buildData();
    }

    public void buildData() {
        Connection c;
        data = FXCollections.observableArrayList();
      try{
        c = DriverManager.getConnection(DatabaseUrl, user, pass);
       
        String SQL = "SELECT NAME , DESCRIPTION from GROUPS";
        ResultSet rs = c.createStatement().executeQuery(SQL);

          colName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>, ObservableValue<String>>(){
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
              return new SimpleStringProperty(param.getValue().get(0).toString());
            }
          });

          colDescription.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>, ObservableValue<String>>(){
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
              return new SimpleStringProperty(param.getValue().get(1).toString());
            }
          });

        /********************************
         * Data added to ObservableList *
         ********************************/
        while(rs.next()){
          //Iterate Row
          ObservableList<String> row = FXCollections.observableArrayList();
          for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
            //Iterate Column
            row.add(rs.getString(i));
            System.out.println("Row [1] added "+rs.getString(i) );
          }

          data.add(row);
        }

        //ADDED TO TableView
        tableview.setItems(data);

      }catch(Exception e){
        e.printStackTrace();
        System.out.println("Error on Building Data");
      }
    }

  public void actionCreateGroup(ActionEvent actionEvent)
  {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/creategroup.fxml"));
      Stage stage = (Stage) btnCreateGroup.getScene().getWindow();
      Scene scene = new Scene(loader.load());
      stage.setTitle("Create New Group");
      //stage.setWidth(width);
     // stage.setHeight(height);
      stage.setResizable(true);
      stage.setScene(scene);
    } catch (IOException io) {
      io.printStackTrace();
    }
  }
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


  public void start(Stage stage) throws Exception {

  }

}
