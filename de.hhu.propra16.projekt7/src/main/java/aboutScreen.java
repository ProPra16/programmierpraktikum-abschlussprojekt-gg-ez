import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Marcel on 01.07.16.
 */
public class AboutScreen {

    public static void showAbout(){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("About");

        Label label = new Label("INFOS");
        label.setFont(Font.font("Verdana", 15));

        VBox layout1 = new VBox(30);
        layout1.getChildren().addAll(label);
        layout1.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout1, 450,150);

        window.setScene(scene);
        window.showAndWait();
    }
}
