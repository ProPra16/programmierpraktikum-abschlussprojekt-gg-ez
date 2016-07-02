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

    public static Stage window;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Projekt7.fxml"));
        Parent root = loader.load();
        Projekt7Controller controller = loader.getController();

        Scene scene = new Scene(root);

        window = primaryStage;
        window.setScene(scene);
        window.show();

        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });
    }

    public static void closeProgram() {
        Boolean beenden = closeWindow.ask();

        if (beenden) window.close();
    }
}
