import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.transform.TransformerException;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    @FXML public ImageView imageViewStatus;
    @FXML private MenuItem MenuItemSave;
    @FXML private MenuItem MenuItemSaveAs;
    @FXML private MenuItem babysteps;
    @FXML private TabPane classTabPane;
    @FXML private TabPane testTabPane;
    @FXML private TextArea messageTextArea;
    @FXML private TextArea descriptionTextArea;

    @FXML private GridPane gridLinks;

    public static SimpleStringProperty time = new SimpleStringProperty();

    private ArrayList<TextArea> classTextList;
    private ArrayList<TextArea> testTextList;

    private Exercise currentExercise;
    private Modus mode;

    @FXML
    public void newExercise() throws IOException {
        NewExerciseController alert = new NewExerciseController();
        alert.show(this);
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
            loadExercise(new Exercise(file));
        } catch (Exception e) {
            System.out.println("Fehler beim laden");
        }
    }

    public void loadExercise(Exercise exercise) {
        closeExercise();
        this.currentExercise = exercise;
        loadExerciseToText(currentExercise.getClassesText(), currentExercise.getTestsText(), currentExercise.getDescriptionText());
    }

    private void loadExerciseToText(HashMap<String, String> classList, HashMap<String, String> testList, String description){
        mode = new Modus(2);
        classTextList = new ArrayList<>();
        for (String key: classList.keySet()) {
            BorderPane borderPane = new BorderPane();
            TextArea temporaryTextArea = new TextArea(classList.get(key));
            borderPane.setCenter(temporaryTextArea);
            classTextList.add(temporaryTextArea);

            Tab tab = new Tab();
            tab.setText(key);
            tab.setContent(borderPane);

            classTabPane.getTabs().add(tab);
        }

        testTextList = new ArrayList<>();
        for (String key: testList.keySet()) {
            BorderPane borderPane = new BorderPane();
            TextArea temporaryTextArea = new TextArea(testList.get(key));
            borderPane.setCenter(temporaryTextArea);
            testTextList.add(temporaryTextArea);

            Tab tab = new Tab();
            tab.setText(key);
            tab.setContent(borderPane);

            testTabPane.getTabs().add(tab);
        }

        descriptionTextArea.setText(description);
        descriptionTextArea.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        changeMode();

        MenuItemSave.setDisable(false);
        MenuItemSaveAs.setDisable(false);
    }

    @FXML
    private void closeExercise() {
        classTabPane.getTabs().clear();
        testTabPane.getTabs().clear();

        messageTextArea.clear();
        descriptionTextArea.clear();

        currentExercise = null;

        MenuItemSave.setDisable(true);
        MenuItemSaveAs.setDisable(true);
    }

    @FXML
    public void saveExercise() {

        classTabPane.getTabs().stream().forEach( tab -> {
            BorderPane borderPane = (BorderPane) tab.getContent();
            TextArea textArea = (TextArea) borderPane.getCenter();

            currentExercise.updateClass(tab.getText(), textArea.getText());
        });

        testTabPane.getTabs().stream().forEach( (tab) -> {
            BorderPane borderPane = (BorderPane) tab.getContent();
            TextArea textArea = (TextArea) borderPane.getCenter();

            currentExercise.updateTest(tab.getText(), textArea.getText());
        });

        try {
            currentExercise.saveEx();
        } catch (TransformerException e) {
            System.out.println("Fehler beim Speichern");
        }
    }

    @FXML
    public void saveExerciseAs(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Save Exercise as File");
        Stage stage = new Stage();

        File newFile = fileChooser.showSaveDialog(stage);

        if(newFile == null || currentExercise == null) return;

        currentExercise.setName(newFile.getName());
        currentExercise.setPath(newFile.getPath());
        try {
            currentExercise.saveEx();
        } catch (TransformerException e) {
            System.out.println("Fehler beim Speichern");
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
    public void showAbout()throws Exception{
        //AboutScreen.showAbout();
        Stage about = new Stage();
        about.setResizable(false);
        Parent root2 = FXMLLoader.load(getClass().getResource("/AboutScreen.fxml"));
        Scene scene = new Scene(root2, 600, 400);
        about.setTitle("About");
        about.setScene(scene);
        about.showAndWait();
    }

    public void tryTestingCode(){
        saveExercise();
        Compiler compiler = new Compiler();
        messageTextArea.setText("");
        boolean tested = true;
        boolean compiled = true;
        if(compiler.tryCompiling(currentExercise)) {
            messageTextArea.appendText("Compiling successful\n");
            if(compiler.tryTests()){
                messageTextArea.appendText("Testing successful\n");
            } else {
                messageTextArea.appendText(compiler.getTestfailMessage());
                tested = false;
            }
        }else{
            messageTextArea.appendText(compiler.getCompileError());
            compiled = false;
        }

        if (
                (mode.getCurrent_mode() == 0 && compiled && tested == false) ||
                        (mode.getCurrent_mode() == 1 && compiled && tested) ||
                        (mode.getCurrent_mode() == 2 && compiled && tested)
                ) changeMode();

    }

    public void changeMode(){
        mode.nextModus();
        setStatusIcon(mode.getCurrent_mode());
        boolean disableTests = false;
        switch (mode.getCurrent_mode()) {
            case Modus.TEST_SCHREIBEN: disableTests = false; break;
            case Modus.CODE_SCHREIBEN: disableTests = true; break;
            case Modus.REFACTORING: return;
        }

        for(TextArea area: classTextList){
            area.setEditable(disableTests);
            //area.setStyle("-fx-text-fill: black;");
        }

        for(TextArea area: testTextList){
            area.setEditable(!disableTests);
            //area.setStyle("-fx-text-fill: black;");
        }

    }

    public void setStatusIcon(int status){
        System.out.println(status); //DEBUG
        if(status==0){
            imageViewStatus.setImage(new Image("icon1.png"));
        }

        if(status==1) {
            imageViewStatus.setImage(new Image("icon2.png"));
        }

        if(status==2) {
            imageViewStatus.setImage(new Image("icon3.png"));
        }

    }

    public void setThemeWhite(){
        ProgramStyle.styleWhite();
    }

    public void setThemeDark(){
        ProgramStyle.styleDark();
    }

    public void setThemeFab(){
        ProgramStyle.styleFab();
    }






    //TEST



    public void Timertest(){

        Timer.startTimer();

    }


    public void testButton(){
        setStatusIcon(1);
    }

    public void testButton2(){
        setStatusIcon(2);
    }

    public void testButton3(){
        setStatusIcon(3);
    }






    @Override
    public void initialize(URL location, ResourceBundle resources) {
        BorderPane border = new BorderPane();
        Label timeLabel = new Label();
        timeLabel.setFont(Font.font("Verdana", 40));
        border.setCenter(timeLabel);
        gridLinks.add(border,0,1);
        timeLabel.textProperty().bind(time);
    }
}