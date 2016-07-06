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
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;


public class Projekt7Controller {

    @FXML public ImageView imageViewStatus;
    @FXML private MenuItem MenuItemSave;
    @FXML private MenuItem MenuItemSaveAs;
    @FXML private MenuItem babysteps;
    @FXML private TabPane classTabPane;
    @FXML private TabPane testTabPane;

    private Exercise currEx;

    /*
     * Ist nur zu Testzwecken momentan da!
     * Erstellt eine Test.xml datei in /de.hhu.propra16.projektt7/exercises!
     */
    @FXML
    public void newExercise(){

        try {
            currEx = new Exercise("Test");
            currEx.addDefaultPair("TestClass");
            currEx.addDefaultClass("Class2");

            writeInTextArea(currEx.getClassesText(), currEx.getTestsText());

            MenuItemSave.setDisable(false);
            MenuItemSaveAs.setDisable(false);
        } catch (ParserConfigurationException e) {
        } catch (TransformerException e) {
        }
    }

    @FXML
    public void openExercise() throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Exercise");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage stage = new Stage();

        File file = fileChooser.showOpenDialog(stage);

        if(file == null) return;

        try {
            this.currEx = new Exercise(file);
        } catch (ParserConfigurationException e) {

        } catch (IOException e) {

        } catch (SAXException e) {

        }

        writeInTextArea(currEx.getClassesText(), currEx.getTestsText());

        MenuItemSave.setDisable(false);
        MenuItemSaveAs.setDisable(false);
    }

    @FXML
    private void closeExercise() {
        classTabPane.getTabs().clear();
        testTabPane.getTabs().clear();
        currEx = null;
        MenuItemSave.setDisable(true);
        MenuItemSaveAs.setDisable(true);
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
    public void saveExerciseAs(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        fileChooser.setTitle("Save Exercise as File");
        Stage stage = new Stage();

        File newFile = fileChooser.showSaveDialog(stage);
        if(newFile == null || currEx == null) return;

        currEx.setName(newFile.getName());
        File currFile = currEx.getFile();

        Path pathNewFile = newFile.toPath();
        Path pathCurrFile = currFile.toPath();

        try {
            Files.copy(pathCurrFile, pathNewFile);
        } catch (IOException e) {
        }
    }

    @FXML
    public void close(){
        Main.closeProgram();
    }

    @FXML
    public void openHelp(){
        try {
            Desktop.getDesktop().open(new File ("Benutzerhandbuch.pdf"));
        } catch (IOException e) {
            System.out.println("Datei nicht gefunden");
        }
    }

    @FXML
    public void babystepsOptions(){
        BabystepsOptions.showOptions();
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
        AboutScreen.showAbout();
    }

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

    public void setThemeFab(){
        ProgramLayout.styleFab();
    }



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