import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.xml.parsers.ParserConfigurationException;
import java.net.URL;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML public ImageView imageViewStatus;

    @FXML private MenuItem MenuItemSave;
    @FXML private MenuItem MenuItemSaveAs;
    @FXML private MenuItem MenuItemEdit;
    @FXML private MenuItem babysteps;
    @FXML private MenuItem tracking;

    @FXML private TabPane classTabPane;
    @FXML private TabPane testTabPane;
    @FXML private TextArea messageTextArea;
    @FXML private TextArea descriptionTextArea;

    @FXML private Button ButtonForwards;
    @FXML private Button ButtonBackwards;

    @FXML public Label BabystepsLabel, babystepsStatus, timerLabel, statusBar, clock;

    public static SimpleStringProperty time = new SimpleStringProperty("Time");
    public static SimpleStringProperty status = new SimpleStringProperty("");

    private HashMap<String, String> classMap;
    private HashMap<String, String> testMap;

    private ArrayList<TextArea> classTextList;
    private ArrayList<TextArea> testTextList;

    private Exercise currentExercise;
    private Exercise tempExercise;

    private Modus mode;
    public Modus.Mode currentMode;

    public static Tracking track; //NEW

    private Path path;

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
       path = FileHandler.fileChooserOpen();
        if (path == null){
            return;
        }
        try {
            currentExercise = new Exercise(path);
            loadExercise(currentExercise);
            babysteps.setDisable(false);
            tracking.setDisable(false);
            MainController.status.set("Exercise successfully opened");
        } catch (Exception e) {
            MainController.status.set("Error while opening Exercise");
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
        if(currentExercise == null) return;
        MainController.status.set("Exercise successfully closed");
        classTabPane.getTabs().clear();
        testTabPane.getTabs().clear();

        messageTextArea.clear();
        descriptionTextArea.clear();

        FileHandler.saveFile(currentExercise);
        currentExercise = null;

        changeActivationStatus(true);
    }

    public void loadExercise(Exercise exercise) {
        closeExercise();

        mode = new Modus(Modus.Mode.Refactor);

        this.currentExercise = exercise;

        this.classMap = currentExercise.getClassMap();
        this.testMap  = currentExercise.getTestMap();

        classTextList = new ArrayList<>();
        testTextList = new ArrayList<>();

        addTabs(classTabPane, classMap, classTextList);
        addTabs(testTabPane,  testMap,  testTextList);

        String description = currentExercise.getDescriptionText();
        descriptionTextArea.setText(description);

        changeMode();
        changeActivationStatus(false);
        track.setStart();
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
        ButtonForwards.setDisable(status);
    }

    @FXML
    public void analyse() {
        Analysis.display(track.testingTime, track.codingTime, track.refactoringTime, track.compileFailure, track.testFailure);
    }

    @FXML
    public void close(){
        Main.closeProgram();
    }

    @FXML
    public void openHelp(){
        HelpFiles.openHelp();
    }

    @FXML
    public void babystepsOptions(){
        BabystepsOptions.showOptions(this);
    }

    @FXML
    public void showAbgabe(){
        HelpFiles.showAbgabe();
    }

    @FXML
    public void showIlias(){
        HelpFiles.showIlias();
    }

    @FXML
    public void showAbout() throws Exception{
        Stage about = new Stage();
        about.setResizable(false);
        Parent root2 = FXMLLoader.load(getClass().getResource("/AboutScreen.fxml"));
        Scene scene = new Scene(root2, 600, 400);
        about.setTitle("About");
        about.setScene(scene);
        about.showAndWait();
    }

    public void tryTestingCode(){
        if (BabystepsOptions.getActive()) {
            try {
                tempExercise = new Exercise(path);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        }

        saveExercise();
        status.set("");

        Compiler compiler = new Compiler();
        messageTextArea.setText("");
        boolean tested = true;
        boolean compiled = true;
        if (compiler.tryCompiling(currentExercise)) {
            messageTextArea.appendText("Compiling successful\n");

            if (compiler.tryTests()) {
                if (BabystepsOptions.getActive()) {
                    if (mode.getCurrentMode() == Modus.Mode.Test) {
                        currentExercise = tempExercise;
                        loadExercise(currentExercise);
                    }
                }

                messageTextArea.appendText("Testing successful\n");
                if (mode.getCurrentMode() == Modus.Mode.Test) {
                    messageTextArea.appendText("\nBut tests should fail\n");
                }

            } else {
                messageTextArea.appendText(compiler.getTestfailMessage());

                tested = false;
                track.testFailure++;
            }
        } else {
            if (BabystepsOptions.getActive()) {
                currentExercise = tempExercise;
                loadExercise(currentExercise);
            }

            messageTextArea.appendText(compiler.getCompileError());

            if (BabystepsOptions.getActive()) {
                messageTextArea.appendText("\n");
                messageTextArea.appendText("Babystep fehlgeschlagen. Letzter Schritt wird aufgerufen");
            }

            compiled = false;
            track.compileFailure++;

        }

        track.currentState = mode.getCurrentMode();

        if (
                (mode.getCurrentMode() == Modus.Mode.Test && currentExercise.isEmptyClass() && !compiled) ||
                        (mode.getCurrentMode() == Modus.Mode.Test && !tested) ||
                        (mode.getCurrentMode() == Modus.Mode.Code && compiled && tested) ||
                        (mode.getCurrentMode() == Modus.Mode.Refactor && compiled && tested)
                ) {
            changeMode();
            saveExercise();
            track.switching();
            track.setStart();
            status.set("Switched from Modus " + mode.getPreviousMode().toString() +" to Modus " + currentMode.toString() +" successfully");
        } else {
            status.set("Error while switching Modus");
        }

        if (mode.getCurrentMode() == Modus.Mode.Code) {
            ButtonBackwards.setDisable(false);
        } else {
            ButtonBackwards.setDisable(true);
        }

        if (BabystepsOptions.getActive()) {
            if (BabystepsTimer.getTime() != null && mode.getCurrentMode() != Modus.Mode.Refactor) BabystepsTimer.stop();
            if (mode.getCurrentMode() == Modus.Mode.Refactor) {
                BabystepsTimer.stop();
                BabystepsTimer.timeString.set("∞");
            } else {
                BabystepsTimer.startTimer(this);
                timerLabel.setVisible(true);
            }
        }
    }

    public void showTimer(boolean status) {
        if (status) {
            timerLabel.setVisible(true);
            clock.setVisible(false);
        }
        if (!status) {
            timerLabel.setVisible(false);
            clock.setVisible(true);
        }
    }

    @FXML
    public void ButtonForwardsAction(){
        tryTestingCode();
    }

    @FXML
    public void ButtonBackwardsAction(){
        if (BabystepsOptions.getActive()) {
            BabystepsTimer.stop();
            BabystepsTimer.startTimer(this);
        }
        changeMode();
        changeMode();

        classTabPane.getTabs().clear();
        testTabPane.getTabs().clear();
        loadExercise(currentExercise);
        status.set("");

        ButtonBackwards.setDisable(true);
    }

    public void changeMode(){
        mode.nextModus();
        currentMode = mode.getCurrentMode();
        setStatusIcon(mode.getCurrentMode());
        boolean disableTests = false;
        if(mode.getCurrentMode() == Modus.Mode.Test) disableTests = false;
        if(mode.getCurrentMode() == Modus.Mode.Code) disableTests = true;
        if(mode.getCurrentMode() == Modus.Mode.Refactor) return;

        for(TextArea area: classTextList){
            area.setEditable(disableTests);
            if (disableTests) area.setStyle("-fx-text-fill: black;");
            if (!disableTests) area.setStyle("-fx-text-fill: grey;");
        }

        for(TextArea area: testTextList){
            area.setEditable(!disableTests);
            if (!disableTests) area.setStyle("-fx-text-fill: black;");
            if (disableTests) area.setStyle("-fx-text-fill: grey;");
        }

    }

    public Modus.Mode getCurrentMode() {
        return currentMode;
    }

    public void setStatusIcon(Modus.Mode mode){
        if(mode == Modus.Mode.Test) imageViewStatus.setImage(new Image("icon1.png"));
        if(mode == Modus.Mode.Code) imageViewStatus.setImage(new Image("icon2.png"));
        if(mode == Modus.Mode.Refactor) imageViewStatus.setImage(new Image("icon3.png"));
    }

    public static ArrayList getArray() {
        ArrayList<int[]> arrayList = track.timeList;
        return arrayList;
    }


    public static boolean isStyleWhite = false;
    public static boolean isStyleDark = false;
    public static boolean isStyleFab = false;

    public void setThemeWhite(){
        ProgramStyle.styleWhite();
        isStyleWhite = true;
        isStyleDark = false;
        isStyleFab = false;
    }

    public void setThemeDark(){
        ProgramStyle.styleDark();
        isStyleWhite = false;
        isStyleDark = true;
        isStyleFab = false;
    }

    public void setThemeFab(){
        ProgramStyle.styleFab();
        isStyleWhite = false;
        isStyleDark = false;
        isStyleFab = true;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        descriptionTextArea.setFocusTraversable(false);

        track = new Tracking();

        changeActivationStatus(true);

        ZonedDateTime.now();
        Timeline currentTime = new Timeline(
                new KeyFrame(Duration.seconds(0), event -> clock.setText(
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
                )),
                new KeyFrame(Duration.seconds(1))
        );
        currentTime.setCycleCount(Animation.INDEFINITE);
        currentTime.play();

        statusBar.textProperty().bind(status);
        timerLabel.textProperty().bind(time);
    }
}