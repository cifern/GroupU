package groupu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  private static final int width = 325;
  private static final int height = 275;

  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("view/login.fxml"));
    primaryStage.setTitle("Login");
    primaryStage.setScene(new Scene(root, width, height));
    primaryStage.setResizable(true);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
