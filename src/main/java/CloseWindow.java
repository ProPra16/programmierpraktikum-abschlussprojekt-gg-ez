import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Marcel on 30.06.16.
 */
public class CloseWindow {

    static boolean beenden;

    public static boolean ask(){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Beenden?");

        Label label = new Label("Soll das Programm wirklich beendet werden?");
        label.setFont(Font.font("Verdana", 15));

        Button yesButton = new Button("Ja");
        yesButton.setFont(Font.font(("Verdana"), 15));

        Button noButton = new Button("Nein");
        noButton.setFont(Font.font(("Verdana"), 15));


        yesButton.setOnAction(e -> {
            beenden = true;
            window.close();
        });

        noButton.setOnAction(e -> {
            beenden=false;
            window.close();
        });

        VBox layout1 = new VBox(30);
        HBox layout2 = new HBox(20);

        layout1.getChildren().addAll(label, layout2);
        layout1.setAlignment(Pos.CENTER);

        layout2.getChildren().addAll(yesButton, noButton);
        layout2.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout1, 450,150);

        window.setScene(scene);
        window.showAndWait();

        return beenden;
    }
}
