import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Marcel on 22.06.16.
 */
public class Main extends Application {

    public static Stage window;

    public static Scene scene;

    public static void main(String[] args){
        launch(args);
    }

    public static void closeProgram() {
        Boolean beenden = CloseWindow.ask();

        if (beenden) window.close();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Projekt7.fxml"));
        Parent root = loader.load();
        MainController controller = loader.getController();

        scene = new Scene(root);

        window = primaryStage;
        window.setTitle("TDDT");
        window.setScene(scene);
        window.show();

        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });

    }
}
