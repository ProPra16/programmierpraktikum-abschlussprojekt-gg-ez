import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Marcel on 03.07.16.
 */

public class BabystepsOptions {

    public static boolean babystepsActive;

    public static int time = 180;

    private MainController mainController = Main.getController();

    Stage window;

    @FXML GridPane grid;

    public void showOptions() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/BabystepsOptions.fxml"));
        loader.setController(this);

        Parent root = null;
        try {
            root = loader.load();
        } catch (Exception e) {

        }

        this.window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Create New Exercise");
        window.setResizable(false);

        BabystepsSpinner spinner = new BabystepsSpinner();
        spinner.setPrefWidth(90);

        spinner.valueProperty().addListener((obs, oldTime, newTime) -> {
            time = newTime.getMinute() * 60 + newTime.getSecond();
        });

        grid.add(spinner, 1, 0);


        Scene scene = new Scene(root, 450, 150);

        if (MainController.isStyleWhite) {
            scene.getStylesheets().clear();
        }

        if (MainController.isStyleDark) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add("styleDark.css");
        }

        if (MainController.isStyleFab) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add("styleFabulous.css");
        }

        window.setScene(scene);
        window.showAndWait();
        window.setOnCloseRequest(e -> time = 180);

    }

    @FXML
    private void onButton(){
        babystepsActive = true;
        mainController.babystepsStatus.setText("On");
        if(BabystepsTimer.getTime() != null) BabystepsTimer.stop();
        if(mainController.getCurrentMode() != Modus.Mode.Refactor){
            mainController.showTimer(true);
            BabystepsTimer.startTimer();
        }
        window.close();
    }

    @FXML
    private void offButton(){
        babystepsActive = false;
        mainController.babystepsStatus.setText("Off");
        BabystepsTimer.stop();
        mainController.showTimer(false);
        window.close();
    }

    public static int getTime(){
        return time;
    }

    public static boolean getActive(){
        return babystepsActive;
    }
}
