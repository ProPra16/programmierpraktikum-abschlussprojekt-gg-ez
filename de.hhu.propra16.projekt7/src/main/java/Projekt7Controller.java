import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class Projekt7Controller{

    @FXML private TextArea textArea1;
    @FXML private TextArea testTextArea1;

    private static Path pathFile1;
    private static Path pathTest1;

    @FXML
    public void openExercise() throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Exercise");
        Stage stage = new Stage();

        File file = fileChooser.showOpenDialog(stage);
        Path path = file.toPath();

        String[] geteilt = path.toString().split("\\.");
        String testPathString = geteilt[0] + "_TEST." + geteilt[1];
        Path testPath = Paths.get(testPathString);


        writeInTextArea1(path);

        List<String> listTest = new ArrayList<String>();
        try {
            listTest = Files.readAllLines(testPath);
            pathTest1=testPath;
            for (String s: listTest) testTextArea1.appendText(s + "\n");
        } catch (IOException e) {
            System.out.println("Kein zugehöriger Test gefunden");
        }
    }

    @FXML
    public void close(){
        main.closeProgram();
    }

    @FXML
    public void openHelp(){
        try {
            Desktop.getDesktop().open(new File ("./Benutzerhandbuch.pdf"));
        } catch (IOException e) {
            System.out.println("Datei nicht gefunden");
        }
    }

    @FXML
    public void showAbout(){
        aboutScreen.showAbout();
    }

    @FXML
    public void saveExercise() {
        String string1 = textArea1.getText();
        String[] text1 = string1.split("\\n");

        String string2 = testTextArea1.getText();
        String[] text2 = string2.split("\\n");

        ArrayList<String> ausgabe1 = new ArrayList<String>();
        for (int i=0; i<text1.length; i++){
            ausgabe1.add(text1[i]);
        }
        try {
            Files.write(pathFile1, ausgabe1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> ausgabe2 = new ArrayList<String>();
        for (int i=0; i<text2.length; i++){
            ausgabe2.add(text2[i]);
        }
        try {
            Files.write(pathTest1, ausgabe2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeInTextArea1(Path p){
        List<String> listFile = new ArrayList<String>();
        try {
            listFile = Files.readAllLines(p);
            pathFile1=p;
            for (String s: listFile) textArea1.appendText(s + "\n");
        } catch (IOException e) {
            System.out.println("Datei konnte nicht geladen werden");
        }
    }

    public void setTextArea1Active(boolean active){
        if (active) {
            textArea1.setEditable(true);
            textArea1.setStyle("-fx-text-fill: black;");
            return;
        } else {
            textArea1.setEditable(false);
            textArea1.setStyle("-fx-text-fill: darkgray;");
        }
    }

    public void setTestArea1Active(boolean active){
        if (active) {
            testTextArea1.setEditable(true);
            testTextArea1.setStyle("-fx-text-fill: black;");
            return;
        } else {
            testTextArea1.setEditable(false);
            testTextArea1.setStyle("-fx-text-fill: darkgray;");
        }

    }

}