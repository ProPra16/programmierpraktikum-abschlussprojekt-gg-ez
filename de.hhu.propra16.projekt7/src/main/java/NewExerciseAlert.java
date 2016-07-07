import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class NewExerciseAlert {

    @FXML private TextField nameTextField;
    @FXML private TextField pathTextField;
    @FXML private TextField pairTextField;
    @FXML private TextField classTextField;
    @FXML private TextField testTextField;

    @FXML private TextArea descTextArea;
    @FXML private TextArea listClassTextArea;
    @FXML private TextArea listTestTextArea;

    private static Stage window;
    private static Projekt7Controller controller;
    private String path;
    private String name;
    private ArrayList<String> classList = new ArrayList<>();
    private ArrayList<String> testList  = new ArrayList<>();

    public void show(Projekt7Controller controller) throws IOException {
        this.controller = controller;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/NewExerciseAlert.fxml"));

        Parent root = loader.load();

        this.window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Create New Exercise");
        window.setResizable(false);
        window.setScene(new Scene(root, 900, 600));
        window.showAndWait();
    }

    @FXML
    private void editName(){

        String newName = nameTextField.getText();

        if(path == null) return;
        for(String pathSplit: path.split(name+".xml")){
            this.name = newName;
            this.path = pathSplit + name + ".xml";
        }

        pathTextField.setText(path);
    }

    @FXML
    private void choosePath(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Location");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage stage = new Stage();

        File file = fileChooser.showSaveDialog(stage);

        if(file == null) return;

        this.path = file.getPath();
        pathTextField.setText(path);

        this.name = file.getName();
        for(String nameSplit: name.split(".xml")){
            this.name = nameSplit;
        }
        nameTextField.setText(name);
    }

    @FXML
    private void editPath(){
        this.path = pathTextField.getText();

        Path p = Paths.get(path);
        this.name = p.getFileName().toString();

        for(String nameSplit: name.split(".xml")){
            this.name = nameSplit;
        }
        nameTextField.setText(name);
    }

    @FXML
    private void addPair(){
        String name = pairTextField.getText();
        addClass(name);
        addTest(name+"Test");
        pairTextField.clear();
    }

    @FXML
    private void removePair(){
        String name = pairTextField.getText();
        removeClass(name);
        removeTest(name+"Test");
        pairTextField.clear();
    }

    @FXML
    private void addClass(){
        String name = classTextField.getText();
        addClass(name);
    }

    @FXML
    private void removeClass(){
        String name = classTextField.getText();
        removeClass(name);
    }

    @FXML
    private void addTest(){
        String name = testTextField.getText();
        addTest(name+"Test");
    }

    @FXML
    private void removeTest(){
        String name = testTextField.getText();
        removeTest(name+"Test");
    }

    private void addClass(String name){
        if(classList.contains(name) || name.equals("")){
            classTextField.clear();
            return;
        }

        classList.add(name);
        listClassTextArea.appendText(name+"\n");

        classTextField.clear();
    }

    private void addTest(String name){
        if(testList.contains(name) || name.equals("")){
            testTextField.clear();
            return;
        }

        testList.add(name);
        listTestTextArea.appendText(name+"\n");

        testTextField.clear();
    }

    private void removeClass(String name){
        if(classList.contains(name)){
            classList.remove(name);
            listClassTextArea.clear();
            for(String s: classList){
                listClassTextArea.appendText(s+"\n");
            }
        }

        classTextField.clear();
    }

    private void removeTest(String name){
        if(testList.contains(name)){
            testList.remove(name);
            listTestTextArea.clear();
            for(String s: testList){
                listTestTextArea.appendText(s+"\n");
            }
        }

        testTextField.clear();
    }

    @FXML
    private void save(){
        if(name == null || path == null) return;

        try {
            Exercise exercise = new Exercise(name, path);
            exercise.addDescriptionText(descTextArea.getText());
            classList.forEach((s) -> {
                try {
                    exercise.addDefaultClass(s);
                } catch (TransformerException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
            });

            testList.forEach((s) -> {
                try {
                    exercise.addDefaultTest(s);
                } catch (TransformerException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
            });

            controller.setCurrentExercise(exercise);
            close();

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void close(){
        window.close();
    }
}
