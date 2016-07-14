import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Created by Marcel on 22.06.16.
 */
public class Main extends Application {

    public static Stage window;

    public static Scene scene;

    public static FXMLLoader loader;

    public static Parent root;

    public static MainController controller;





    public static void main(String[] args){
        launch(args);
    }

    public static void closeProgram() {
        Boolean beenden = CloseWindow.ask();

        if (beenden) {
            window.close();
            BabystepsTimer.stop();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
        root = loader.load();
        controller = loader.getController();

        scene = new Scene(root);

        window = primaryStage;
        window.setTitle("Test Driven Development Trainer (TDDT)");

        window.getIcons().addAll(new Image(("icon.png")));


        window.setScene(scene);
        window.show();

        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });

    }

    public static void babystepsCompile(){
        controller.tryTestingCode();
    }

    public static void saveExercise(){
        controller.saveExercise();
    }

    public static MainController getController(){
        return controller;
    }

}
