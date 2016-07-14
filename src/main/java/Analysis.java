
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.text.StyledEditorKit;
import java.util.ArrayList;


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
        stage.setTitle("Tracking");

        final String testTime = "Testing Time";
        final String codeTime = "Coding Time";
        final String refTime = "Refactoring Time:";

        final CategoryAxis x = new CategoryAxis();
        final NumberAxis y = new NumberAxis();

        final BarChart<String, Number> Chart = new BarChart<>(x,y);

        Chart.setTitle("Tracking Analysis");
        y.setLabel("Time in s");


        XYChart.Series series1 = new XYChart.Series();
        series1.setName(testTime);
        XYChart.Series series2 = new XYChart.Series();
        series2.setName(codeTime);
        XYChart.Series series3 = new XYChart.Series();
        series3.setName(refTime);
        ArrayList<int[]> time = MainController.getArray();
        for(int i = 0; i < time.size(); i++) {
            int[] tmp = time.get(i);
            int tTime = tmp[0];
            Integer tTimeObj = new Integer(tTime);
            Number tTimeNum = (Number)tTimeObj;
            int cTime = tmp[1];
            Integer cTimeObj = new Integer(cTime);
            Number cTimeNum = (Number)cTimeObj;
            int rTime = tmp[2];
            Integer rTimeObj = new Integer(rTime);
            Number rTimeNum = (Number)rTimeObj;

            String Test = "Testcycle " + (i+1);

            series1.getData().add(new XYChart.Data(Test,tTimeNum));
            series2.getData().add(new XYChart.Data(Test,cTimeNum));
            series3.getData().add(new XYChart.Data(Test,rTimeNum));

        }



        /*        Text testTime = new Text("Testing Time: " + BabystepsTimer.timeStringFormatter(testT));
        Text codeTime = new Text("Coding Time: " + BabystepsTimer.timeStringFormatter(codeT));
        Text refTime = new Text("Refactoring Time: " + BabystepsTimer.timeStringFormatter(refT));
        Text compFails = new Text("Failing Compilation: " + failC + " time(s)");
        Text testFails = new Text("Failing Testing: " + failT + " time(s)");

        VBox layout = new VBox(10);
        layout.setMinSize(250.0, 150.0);
        layout.getChildren().addAll(testTime, codeTime, refTime, compFails, testFails);
        layout.setAlignment(Pos.CENTER);
*/
        final String failTest = "Failing Test: " + failT + "        ";
        final String failComp = "Failing Compile: " + failC;

        Label labelFT = new Label(failTest);
        Label labelFC = new Label(failComp);
        labelFT.setStyle("-fx-font-weight: bold");
        labelFC.setStyle("-fx-font-weight: bold");
        HBox hBox = new HBox(2);
        hBox.getChildren().addAll(labelFT, labelFC);
        hBox.setAlignment(Pos.CENTER);
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(800,600);
        Chart.getData().addAll(series1,series2,series3);
        borderPane.setCenter(Chart);
        borderPane.setBottom(hBox);
        Scene scene = new Scene(borderPane,800,500);

        if(MainController.isStyleWhite){
            scene.getStylesheets().clear();
        }

        if(MainController.isStyleDark){
            scene.getStylesheets().clear();
            scene.getStylesheets().add("styleDark.css");
        }

        if(MainController.isStyleFab){
            scene.getStylesheets().clear();
            scene.getStylesheets().add("styleFabulous.css");
        }
        stage.setTitle("Tracking Analysis");
        stage.setScene(scene);
        stage.show();
    }

}
