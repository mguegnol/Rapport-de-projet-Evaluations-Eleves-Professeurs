package schoolg;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	
    @Override
    public void start(Stage primaryStage) throws Exception {
       //Load the login interface
        Parent root2 = FXMLLoader.load(getClass().getResource("/Login.fxml"));
        primaryStage.setTitle("SchoolG Login");
        primaryStage.setScene(new Scene(root2, 300, 200));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
