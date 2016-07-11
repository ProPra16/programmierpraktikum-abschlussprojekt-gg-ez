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
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    public ImageView imageViewStatus;

    @FXML
    private MenuItem MenuItemSave;
    @FXML
    private MenuItem MenuItemSaveAs;
    @FXML
    private MenuItem MenuItemEdit;
    @FXML
    private MenuItem babysteps;

    @FXML
    private TabPane classTabPane;
    @FXML
    private TabPane testTabPane;
    @FXML
    private TextArea messageTextArea;
    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Button ButtonForwards;
    @FXML
    private Button ButtonBackwards;

    @FXML
    public Label BabystepsLabel;
    @FXML
    public Label babystepsStatus;
    @FXML
    public Label timerLabel;
    @FXML
    public Label statusBar;

    public static Label clock;

    public static SimpleStringProperty time = new SimpleStringProperty("Time");
    public static SimpleStringProperty status = new SimpleStringProperty("");

    private HashMap<String, String> classMap;
    private HashMap<String, String> testMap;

    private ArrayList<TextArea> classTextList;
    private ArrayList<TextArea> testTextList;

    private Exercise currentExercise;

    private Exercise tempExercise;

    private Modus mode;
    public Tracking track; //NEW

    private Path path;

    @FXML
    public void newExercise() {
        NewExerciseController alert = new NewExerciseController();
        alert.show(this);
        ButtonBackwards.setDisable(false);
    }

    @FXML
    public void editExercise() {
        EditExerciseController alert = new EditExerciseController();
        alert.show(this, currentExercise);
    }

    @FXML
    public void openExercise() {
        path = FileHandler.fileChooserOpen();
        if (path == null) {
            return;
        }
        try {
            currentExercise = new Exercise(path);
            loadExercise(currentExercise);
            ButtonForwards.setDisable(false);
            babysteps.setDisable(false);
            MainController.status.set("File successfully opened");
        } catch (Exception e) {
            MainController.status.set("Error while opening File");
        }
    }

    @FXML
    public void saveExercise() {
        updateMapFromTabs();
        currentExercise.setMaps(classMap, testMap);
        FileHandler.saveFile(currentExercise);
    }

    @FXML
    public void saveExerciseAs() {
        Path path = FileHandler.fileChooserSave();
        if (path == null) {
            return;
        }
        currentExercise.setPath(path);

        saveExercise();
    }

    @FXML
    private void closeExercise() {
        if (currentExercise == null) return;

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

        mode = new Modus(2);

        this.currentExercise = exercise;

        this.classMap = currentExercise.getClassMap();
        this.testMap = currentExercise.getTestMap();

        classTextList = new ArrayList<>();
        testTextList = new ArrayList<>();

        addTabs(classTabPane, classMap, classTextList);
        addTabs(testTabPane, testMap, testTextList);

        String description = currentExercise.getDescriptionText();
        descriptionTextArea.setText(description);
        //descriptionTextArea.setPickOnBounds(true);

        changeMode();
        changeActivationStatus(false);
        track.setStart();
    }

    private void addTabs(TabPane TabPane, HashMap<String, String> map, ArrayList<TextArea> list) {
        for (String key : map.keySet()) {
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

    private void updateMapFromTabs() {
        classTabPane.getTabs().stream().forEach(tab -> {
            BorderPane borderPane = (BorderPane) tab.getContent();
            TextArea textArea = (TextArea) borderPane.getCenter();

            classMap.put(tab.getText(), textArea.getText());
        });

        testTabPane.getTabs().stream().forEach((tab) -> {
            BorderPane borderPane = (BorderPane) tab.getContent();
            TextArea textArea = (TextArea) borderPane.getCenter();

            testMap.put(tab.getText(), textArea.getText());
        });
    }

    private void changeActivationStatus(boolean status) {
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
    public void close() {
        Main.closeProgram();
    }

    @FXML
    public void openHelp() {
        HelpFiles.openHelp();
    }

    @FXML
    public void babystepsOptions() {
        BabystepsOptions.showOptions(this);
    }

    @FXML
    public void showAbgabe() {
        HelpFiles.showAbgabe();
    }

    @FXML
    public void showIlias() {
        HelpFiles.showIlias();
    }

    @FXML
    public void showAbout() {
        Stage about = new Stage();
        about.setResizable(false);
        Parent root2 = null;
        try {
            root2 = FXMLLoader.load(getClass().getResource("/AboutScreen.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root2, 600, 400);
        about.setTitle("About");
        about.setScene(scene);
        about.showAndWait();
    }

    public void tryTestingCode() {

        if (BabystepsOptions.getActive()) {
            try {
                tempExercise = new Exercise(path);
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        }

        saveExercise();

        Compiler compiler = new Compiler();
        messageTextArea.setText("");
        boolean tested = true;
        boolean compiled = true;
        if (compiler.tryCompiling(currentExercise)) {
            messageTextArea.appendText("Compiling successful\n");

            if (compiler.tryTests()) {
                if (BabystepsOptions.getActive()) {
                    if (mode.getCurrent_mode() == 0) {
                        saveExercise();
                        currentExercise = tempExercise;
                        loadExercise(currentExercise);
                    } else {
                        saveExercise();
                    }
                }

                messageTextArea.appendText("Testing successful\n");
                if (mode.getCurrent_mode() == 0) {
                    messageTextArea.appendText("But tests should fail\n");
                }

            } else {
                messageTextArea.appendText(compiler.getTestfailMessage());

                if (BabystepsOptions.getActive()) saveExercise();
                tested = false;
                track.testFailure++;
            }
        } else {
            if (BabystepsOptions.getActive()) {
                saveExercise();
                currentExercise = tempExercise;
                loadExercise(currentExercise);
            }

            messageTextArea.appendText(compiler.getCompileError());

            if (BabystepsOptions.getActive()) {
                messageTextArea.appendText("\n");
                messageTextArea.appendText("Babystep fehlgeschlagen. Letzter Schritt wird aufgerufen");
                saveExercise();
            }

            compiled = false;
            track.compileFailure++;

        }

        if (BabystepsOptions.getActive()) saveExercise();

        track.currentState = mode.getCurrent_mode();

        if (
                (mode.getCurrent_mode() == 0 && compiled && !tested) ||
                        (mode.getCurrent_mode() == 1 && compiled && tested) ||
                        (mode.getCurrent_mode() == 2 && compiled && tested)
                ) {
            changeMode();
            track.switching();
            track.setStart();
        }

        if (mode.getCurrent_mode() == 1) {
            ButtonBackwards.setDisable(false);
        } else {
            ButtonBackwards.setDisable(true);
        }

        if (BabystepsOptions.getActive()) {
            if (BabystepsTimer.getTime() != null && mode.getCurrent_mode() != 2) BabystepsTimer.stop();
            if (mode.getCurrent_mode() == 2) {
                BabystepsTimer.stop();
                timerLabel.setVisible(false);
            } else {
                BabystepsTimer.startTimer(this);
                timerLabel.setVisible(true);
            }
        }
    }

    @FXML
    public void ButtonForwardsAction() {
        tryTestingCode();
    }

    @FXML
    public void ButtonBackwardsAction() {
        if (BabystepsOptions.getActive()) {
            BabystepsTimer.stop();
            BabystepsTimer.startTimer(this);
        }
        changeMode();
        changeMode();
        ButtonBackwards.setDisable(true);

    }

    public void changeMode() {
        mode.nextModus();
        setStatusIcon(mode.getCurrent_mode());
        boolean disableTests = false;
        switch (mode.getCurrent_mode()) {
            case Modus.TEST_SCHREIBEN:
                disableTests = false;
                break;
            case Modus.CODE_SCHREIBEN:
                disableTests = true;
                break;
            case Modus.REFACTORING:
                return;
        }

        for (TextArea area : classTextList) {
            area.setEditable(disableTests);
            if (disableTests) area.setStyle("-fx-text-fill: black;");
            if (!disableTests) area.setStyle("-fx-text-fill: grey;");
        }

        for (TextArea area : testTextList) {
            area.setEditable(!disableTests);
            if (!disableTests) area.setStyle("-fx-text-fill: black;");
            if (disableTests) area.setStyle("-fx-text-fill: grey;");
        }

    }

    public void setStatusIcon(int status) {
        if (status == 0) imageViewStatus.setImage(new Image("icon1.png"));
        if (status == 1) imageViewStatus.setImage(new Image("icon2.png"));
        if (status == 2) imageViewStatus.setImage(new Image("icon3.png"));
    }

    public ArrayList getTimeList() {
        return track.timeList;
    }


    public void setThemeWhite() {
        ProgramStyle.styleWhite();
    }

    public void setThemeDark() {
        ProgramStyle.styleDark();
    }

    public void setThemeFab() {
        ProgramStyle.styleFab();
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        descriptionTextArea.setFocusTraversable(false);
        descriptionTextArea.setMouseTransparent(true);

        track = new Tracking();

        changeActivationStatus(true);

        clock = new Label();
        clock.setFont(Font.font("Verdana", 25));


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
        stackPane.getChildren().addAll(clock);
    }
}