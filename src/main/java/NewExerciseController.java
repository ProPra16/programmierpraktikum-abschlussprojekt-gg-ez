import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NewExerciseController {

    @FXML private TextField nameTextField;
    @FXML private TextField pathTextField;
    @FXML private TextField pairTextField;
    @FXML private TextField classTextField;
    @FXML private TextField testTextField;

    @FXML private TextArea descTextArea;

    @FXML private ListView<String> classListView;
    @FXML private ListView<String> testListView;

    private static Stage window;
    private static MainController controller;

    private String path;
    private String name;

    private static final ObservableList<String> classList = FXCollections.observableArrayList();
    private static final ObservableList<String> testList = FXCollections.observableArrayList();

    public void show(MainController controller) throws IOException {
        this.controller = controller;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/NewExerciseView.fxml"));
        loader.setController(this);

        Parent root = loader.load();

        this.window = new Stage();

        classListView.setItems(classList);
        testListView.setItems(testList);

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Create New Exercise");
        window.setResizable(false);
        window.setScene(new Scene(root, 900, 600));
        window.showAndWait();
    }

    @FXML
    public void choosePath(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Location");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage stage = new Stage();

        File file = fileChooser.showSaveDialog(stage);

        if(file == null) return;

        this.path = file.getPath();
        pathTextField.setText(path);

        /*this.name = file.getName();
        for(String nameSplit: name.split(".xml")){
            this.name = nameSplit;
        }
        nameTextField.setText(name);*/
    }

    @FXML
    public void editPath(){
        this.path = pathTextField.getText();

        /*Path p = Paths.get(path);
        this.name = p.getFileName().toString();

        for(String nameSplit: name.split(".xml")){
            this.name = nameSplit;
        }
        nameTextField.setText(name);*/
    }

    private void addName(){
        this.name = nameTextField.getText();
    }

    @FXML
    public void addPair(){
        String namePair = pairTextField.getText();
        addClass(namePair);
        addTest(namePair+"Test");
        pairTextField.clear();
    }

    @FXML
    public void addClass(){
        String nameClass = classTextField.getText();
        addClass(nameClass);
    }

    @FXML
    public void addTest(){
        String nameTest = testTextField.getText();
        addTest(nameTest+"Test");
    }

    @FXML
    public void removeClass(){
        final int selectedIndex = classListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            String itemToRemove = classListView.getSelectionModel().getSelectedItem();

            final int newSelectedIdx =
                    (selectedIndex == classListView.getItems().size() - 1)
                            ? selectedIndex - 1
                            : selectedIndex;

            classListView.getItems().remove(itemToRemove);
            classListView.getSelectionModel().select(newSelectedIdx);
        }
    }

    @FXML
    public void removeTest(){
        final int selectedIndex = testListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            String itemToRemove = testListView.getSelectionModel().getSelectedItem();

            final int newSelectedIdx =
                    (selectedIndex == testListView.getItems().size() - 1)
                            ? selectedIndex - 1
                            : selectedIndex;

            testListView.getItems().remove(itemToRemove);
            testListView.getSelectionModel().select(newSelectedIdx);
        }
    }

    private void addClass(String nameClass){
        if(classList.contains(nameClass) || nameClass.equals("")){
            classTextField.clear();
            return;
        }

        classList.add(nameClass);
        classTextField.clear();
    }

    private void addTest(String nameTest){
        if(testList.contains(nameTest) || nameTest.equals("")){
            testTextField.clear();
            return;
        }

        testList.add(nameTest);
        testTextField.clear();
    }

    @FXML
    public void save(){
        if(name == null || path == null) {
            System.out.println("Name or Path cant be empty");
            return;
        }

        try {
            addName();
            Exercise exercise = new Exercise(name, path);
            exercise.addDescriptionText(descTextArea.getText());
            classList.forEach((s) -> {
                try {
                    exercise.addDefaultClass(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            testList.forEach((s) -> {
                try {
                    exercise.addDefaultTest(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            controller.loadExercise(exercise);
            close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void close(){
        window.close();
    }
}
