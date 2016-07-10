import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Created by alex on 09.07.16.
 */
public class Analysis {

    private static String text;

    private static int select;

    private static final String WASTING = "You are wasting to much time doing ";

    private static final String TESTING = "testing.";

    private static final String CODING = "codeing.";

    private static final String REFACTORING = "refactoring.";

    public static void display(int testT, int codeT, int refT, int failC, int failT) {
        Stage stage = new Stage();

        Text testTime = new Text("Testing Time: " + BabystepsTimer.timeStringFormatter(testT));
        Text codeTime = new Text("Coding Time: " + BabystepsTimer.timeStringFormatter(codeT));
        Text refTime = new Text("Refactoring Time: " + BabystepsTimer.timeStringFormatter(refT));
        Text compFails = new Text("Failing Compilation: " + failC + " time(s)");
        Text testFails = new Text("Failing Testing: " + failT + " time(s)");

        VBox layout = new VBox(10);
        layout.setMinSize(250.0, 150.0);
        layout.getChildren().addAll(testTime, codeTime, refTime, compFails, testFails);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        stage.setTitle("Tracking Analysis");
        stage.setScene(scene);
        stage.show();
    }
}
