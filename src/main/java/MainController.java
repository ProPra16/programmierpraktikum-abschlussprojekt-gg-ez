import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML public ImageView imageViewStatus;
    @FXML private MenuItem MenuItemSave;
    @FXML private MenuItem MenuItemSaveAs;
    @FXML private MenuItem MenuItemEdit;
    @FXML private MenuItem babysteps;
    @FXML private TabPane classTabPane;
    @FXML private TabPane testTabPane;
    @FXML private TextArea messageTextArea;
    @FXML private TextArea descriptionTextArea;

    @FXML private GridPane gridLinks;

    public static SimpleStringProperty time = new SimpleStringProperty("Time");

    private HashMap<String, String> classMap;
    private HashMap<String, String> testMap;

    private ArrayList<TextArea> classTextList;
    private ArrayList<TextArea> testTextList;

    private Exercise currentExercise;
    private Modus mode;

    @FXML
    public void newExercise() {
        NewExerciseController alert = new NewExerciseController();
        alert.show(this);
    }

    @FXML
    public void editExercise() {
        EditExerciseController alert = new EditExerciseController();
        alert.show(this, currentExercise);
    }

    @FXML
    public void openExercise() {
       Path path = FileHandler.fileChooserOpen();
        if (path == null){
            return;
        }
        try {
            currentExercise = new Exercise(path);
            loadExercise(currentExercise);
        } catch (Exception e) {

        }
    }

    @FXML
    public void saveExercise() {
        updateMapFromTabs();
        currentExercise.setMaps(classMap, testMap);
        FileHandler.saveFile(currentExercise);
    }

    @FXML
    public void saveExerciseAs(){
        Path path = FileHandler.fileChooserSave();
        if (path == null){
            return;
        }
        currentExercise.setPath(path);

        saveExercise();
    }

    @FXML
    private void closeExercise() {
        classTabPane.getTabs().clear();
        testTabPane.getTabs().clear();

        messageTextArea.clear();
        descriptionTextArea.clear();

        descriptionTextArea.setText("Exercise Description:\n");

        FileHandler.saveFile(currentExercise);
        currentExercise = null;

        changeActivationStatus(true);
    }

    public void loadExercise(Exercise exercise) {
        closeExercise();

        mode = new Modus(2);

        this.currentExercise = exercise;

        this.classMap = currentExercise.getClassMap();
        this.testMap  = currentExercise.getTestMap();

        classTextList = new ArrayList<>();
        testTextList = new ArrayList<>();

        addTabs(classTabPane, classMap, classTextList);
        addTabs(testTabPane,  testMap,  testTextList);

        String description = currentExercise.getDescriptionText();
        descriptionTextArea.appendText(description);

        changeMode();
        changeActivationStatus(false);
    }

    private void addTabs(TabPane TabPane, HashMap<String, String> map, ArrayList<TextArea> list) {
        for (String key: map.keySet()) {
            BorderPane borderPane = new BorderPane();
            TextArea temporaryTextArea = new TextArea(map.get(key));
            borderPane.setCenter(temporaryTextArea);
            list.add(temporaryTextArea);

            Tab tab = new Tab();
            tab.setText(key);
            tab.setContent(borderPane);

            TabPane.getTabs().add(tab);
        }
    }

    private void updateMapFromTabs(){
        classTabPane.getTabs().stream().forEach( tab -> {
            BorderPane borderPane = (BorderPane) tab.getContent();
            TextArea textArea = (TextArea) borderPane.getCenter();

            classMap.put(tab.getText(), textArea.getText());
        });

        testTabPane.getTabs().stream().forEach( (tab) -> {
            BorderPane borderPane = (BorderPane) tab.getContent();
            TextArea textArea = (TextArea) borderPane.getCenter();

            testMap.put(tab.getText(), textArea.getText());
        });
    }

    private void changeActivationStatus(boolean status){
        MenuItemSave.setDisable(status);
        MenuItemSaveAs.setDisable(status);
        MenuItemEdit.setDisable(status);
        messageTextArea.setDisable(status);
        descriptionTextArea.setDisable(status);
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void showIlias(){
        try {
            Desktop.getDesktop().browse(new URI("https://ilias.uni-duesseldorf.de"));
        } catch (Exception e) {
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

        if(Timer.getTime() != null) Timer.stop();
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
        changeActivationStatus(true);
        descriptionTextArea.setText("Exercise Description:\n");
        descriptionTextArea.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        BorderPane border = new BorderPane();
        Label timeLabel = new Label();
        timeLabel.setFont(Font.font("Verdana", 40));
        border.setCenter(timeLabel);
        gridLinks.add(border,0,1);
        timeLabel.textProperty().bind(time);

    }
}