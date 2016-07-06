import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;


public class Projekt7Controller{

    @FXML private MenuItem MenuItemSave;
    @FXML private MenuItem babysteps;

    @FXML public ImageView imageViewStatus;

    @FXML private TabPane classTabPane;
    @FXML private TabPane testTabPane;

    private Exercise currEx;

    @FXML
    public void newExercise(){
    }

    @FXML
    public void openExercise() throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Exercise");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage stage = new Stage();

        File file = fileChooser.showOpenDialog(stage);

        try {
            this.currEx = new Exercise(file);
        } catch (ParserConfigurationException e) {

        } catch (IOException e) {

        } catch (SAXException e) {

        }

        writeInTextArea(currEx.getClassesText(), currEx.getTestsText());

        MenuItemSave.setDisable(false);
    }

    @FXML
    public void saveExercise() {

        classTabPane.getTabs().stream().forEach( (tab) -> {
            BorderPane borderPane = new BorderPane();
            borderPane = (BorderPane) tab.getContent();

            TextArea textArea = (TextArea) borderPane.getCenter();

            currEx.updateClass(tab.getText(), textArea.getText());
        });

        testTabPane.getTabs().stream().forEach( (tab) -> {
            BorderPane borderPane = new BorderPane();
            borderPane = (BorderPane) tab.getContent();

            TextArea textArea = (TextArea) borderPane.getCenter();

            currEx.updateTest(tab.getText(), textArea.getText());
        });

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
    public void babystepsOptions(){
        babystepsOptions.showOptions();
    }

    @FXML
    public void showAbgabe(){
        try {
            Desktop.getDesktop().browse(new URI("http://auas.cs.uni-duesseldorf.de"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void showIlias(){
        try {
            Desktop.getDesktop().browse(new URI("https://ilias.uni-duesseldorf.de"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showAbout(){
        aboutScreen.showAbout();
    }

    /*@FXML
    public void saveExerciseAs(){
        String string1 = textArea1.getText();
        String[] text1 = string1.split("\\n");

        ArrayList<String> ausgabe1 = new ArrayList<String>();
        for (int i=0; i<text1.length; i++){
            ausgabe1.add(text1[i]);
        }

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JAVA files (*.java)", "*.java");
        fileChooser.getExtensionFilters().add(extFilter);

        fileChooser.setTitle("Save JavaFile");
        Stage stage = new Stage();

        File file = fileChooser.showSaveDialog(stage);
        Path path = file.toPath();

        try {
            Files.write(path, ausgabe1);
        } catch (IOException e) {

        }
    }

    @FXML
    public void saveTestAs(){
        String string1 = testTextArea1.getText();
        String[] text1 = string1.split("\\n");

        ArrayList<String> ausgabe1 = new ArrayList<String>();
        for (int i=0; i<text1.length; i++){
            ausgabe1.add(text1[i]);
        }

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JAVA Test files (*.java)", "*_TEST.java");
        fileChooser.getExtensionFilters().add(extFilter);

        fileChooser.setTitle("Save TestFile");
        Stage stage = new Stage();

        File file = fileChooser.showSaveDialog(stage);
        Path path = file.toPath();

        try {
            Files.write(path, ausgabe1);
        } catch (IOException e) {

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
    }*/



    private void writeInTextArea(HashMap<String, String> classList, HashMap<String, String> testList){
        for (String key: classList.keySet()) {
            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(new TextArea(classList.get(key)));

            Tab tab = new Tab();
            tab.setText(key);
            tab.setContent(borderPane);

            classTabPane.getTabs().add(tab);
        }

        for (String key: testList.keySet()) {
            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(new TextArea(testList.get(key)));

            Tab tab = new Tab();
            tab.setText(key);
            tab.setContent(borderPane);

            testTabPane.getTabs().add(tab);
        }
    }




    /*public void setTextArea1Active(boolean active){
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

    }*/

    public void setStatusIcon(int status){
        if(status==1){
            imageViewStatus.setImage(new Image("icon1.png"));
        }

        if(status==2) {
            imageViewStatus.setImage(new Image("icon2.png"));
        }

        if(status==3) {
            imageViewStatus.setImage(new Image("icon3.png"));
        }

    }

    public void setThemeWhite(){
        ProgramLayout.styleWhite();
    }

    public void setThemeDark(){
        ProgramLayout.styleDark();
    }

    public void setThemeFab() {ProgramLayout.styleFab();}



    //TEST


    public void testButton(){
        setStatusIcon(1);
    }

    public void testButton2(){
        setStatusIcon(2);
    }

    public void testButton3(){
        setStatusIcon(3);
    }
}