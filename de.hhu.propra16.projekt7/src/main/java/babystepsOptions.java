import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Marcel on 03.07.16.
 */
public class babystepsOptions {

    public static boolean babystepsActive;

    public static int time;

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
            try {
                Desktop.getDesktop().open(new File("./Benutzerhandbuch.pdf"));
            } catch (IOException e) {
            }
        });


        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        HBox toggle = new HBox();
        toggle.setAlignment(Pos.CENTER);

        ToggleGroup group = new ToggleGroup();
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
                    System.out.println(time);
                })
        );

        toggleOn.setOnAction(event -> babystepsActive=true);

        toggleOff.setOnAction(event -> babystepsActive=false);


        border.setCenter(grid);
        Scene scene = new Scene(border, 450,150);

        window.setScene(scene);
        window.showAndWait();
    }
}