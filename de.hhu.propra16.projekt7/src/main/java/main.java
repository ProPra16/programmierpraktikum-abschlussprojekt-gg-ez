import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Marcel on 22.06.16.
 */
public class main extends Application {

    public static void main(String[] args){
        launch(args);
    }



    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Projekt7.fxml"));
        Parent root = (Parent) loader.load();
        Projekt7Controller controller = (Projekt7Controller) loader.getController();


        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
