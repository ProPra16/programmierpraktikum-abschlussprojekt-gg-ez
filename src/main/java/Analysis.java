import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by alex on 09.07.16.
 */
public class Analysis {

    public static void display() {
        Stage stage = new Stage();

        Text txt = new Text("HIIII");
        VBox layout = new VBox(10);
        layout.setMinSize(250.0, 150.0);
        layout.getChildren().add(txt);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        stage.setTitle("Analysis");
        stage.setScene(scene);
        stage.show();
    }
}
