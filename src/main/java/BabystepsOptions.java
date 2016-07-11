import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Marcel on 03.07.16.
 */

public class BabystepsOptions {

    public static boolean babystepsActive;

    public static int time = 180;

    public static ToggleGroup group;

    public static void showOptions(){

        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Configure Babysteps");

        BorderPane border = new BorderPane();

        MenuBar menubar = new MenuBar();
        Menu closeMenu = new Menu("Close");
        Menu helpMenu = new Menu("?");

        MenuItem close = new MenuItem("Close");
        MenuItem help = new MenuItem("Help");

        closeMenu.getItems().addAll(close);
        helpMenu.getItems().addAll(help);

        menubar.getMenus().addAll(closeMenu, helpMenu);
        border.setTop(menubar);

        close.setOnAction(event -> {
            window.close();
        });

        help.setOnAction(event -> {
            HelpFiles.openHelp();
        });


        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        HBox toggle = new HBox();
        toggle.setAlignment(Pos.CENTER);

        group = new ToggleGroup();
        ToggleButton toggleOn = new ToggleButton("On");
        toggleOn.setToggleGroup(group);
        ToggleButton toggleOff = new ToggleButton("Off");
        toggleOff.setToggleGroup(group);
        toggleOff.setSelected(true);

        toggle.getChildren().addAll(toggleOn, toggleOff);

        Label label1 = new Label("Babysteps:");
        Label label2 = new Label("Time");

        Spinner chooseTime = new Spinner(1,3,3);

        grid.add(label1,1,1);
        grid.add(toggle,2,1);
        grid.add(label2,1,2);
        grid.add(chooseTime,2,2);


        chooseTime.valueProperty().addListener(
                ((observable, oldValue, newValue) -> {
                    time = (int) newValue;
                    time= time * 60;
                })
        );

        toggleOn.setOnAction(event -> {
            babystepsActive=true;

            if(BabystepsTimer.getTime() != null) BabystepsTimer.stop();
            BabystepsTimer.startTimer();
            window.close();
            Main.controller.BabystepsLabel.setVisible(true);

        });

        toggleOff.setOnAction(event -> {
            BabystepsTimer.stop();
            Babysteps.stop();
            window.close();
            Main.controller.BabystepsLabel.setVisible(false);
            BabystepsTimer.showTimer(false);
        });


        border.setCenter(grid);
        Scene scene = new Scene(border, 450,150);

        window.setScene(scene);
        window.showAndWait();
    }


    public static int getTime(){
        return time;
    }

    public static boolean getActive(){
        return babystepsActive;
    }
}
