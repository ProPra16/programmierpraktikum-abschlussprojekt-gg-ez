import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Marcel on 22.06.16.
 */
public class Projekt7Controller{

    @FXML private TextArea textArea1;


    @FXML
    public void openExercise() throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Exercise");
        Stage stage = new Stage();

        File file = fileChooser.showOpenDialog(stage);

        System.out.println(file.getName());

        Path p = file.toPath();

        List<String> list;

        try {
            list = Files.readAllLines(p);

            for (String s: list){
                textArea1.appendText(s + "\n");

            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTextArea1(String text){
        textArea1.setText(text);
    }

}