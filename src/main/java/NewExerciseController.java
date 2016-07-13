import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class NewExerciseController {

    @FXML private TextField pathTextField;
    @FXML private TextField nameTextField;
    @FXML private TextField pairTextField;
    @FXML private TextField classTextField;
    @FXML private TextField testTextField;

    @FXML private TextArea descTextArea;

    @FXML private TreeView<String> classTreeView;
    @FXML private TreeView<String> testTreeView;

    private Stage window;
    private MainController controller;

    private Path path;
    private String name;

    private HashMap<String, String> classMap = new HashMap<>();
    private HashMap<String, String> testMap  = new HashMap<>();

    private TreeItem<String> classRoot;
    private TreeItem<String> testRoot;

    public void show(MainController controller) {
        try {
            this.controller = controller;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/NewExerciseView.fxml"));
            loader.setController(this);

            Parent root = loader.load();

            this.window = new Stage();

            initialize();

            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Create New Exercise");
            window.setResizable(false);
            Scene scene = new Scene(root, 900, 600);

            if(MainController.isStyleWhite){
                scene.getStylesheets().clear();
            }

            if(MainController.isStyleDark){
                scene.getStylesheets().clear();
                scene.getStylesheets().add("styleDark.css");
            }

            if(MainController.isStyleFab){
                scene.getStylesheets().clear();
                scene.getStylesheets().add("styleFabulous.css");
            }

            window.setScene(scene);
            window.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialize() {
        this.classRoot = new TreeItem<>();
        classRoot.setExpanded(true);
        classTreeView.setRoot(classRoot);

        this.testRoot = new TreeItem<>();
        testRoot.setExpanded(true);
        testTreeView.setRoot(testRoot);
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

        this.path = file.toPath();
        pathTextField.setText(path.toString());
    }

    @FXML
    public void editPath(){
        this.path = Paths.get(pathTextField.getText());
    }

    @FXML
    public void addPair(){
        String namePair = pairTextField.getText();
        addClass(namePair);
        addTest(namePair);
        pairTextField.clear();
    }

    @FXML
    public void addClass(){
        String nameClass = classTextField.getText();
        addClass(nameClass);
        classTextField.clear();
    }

    @FXML
    public void addTest(){
        String nameTest = testTextField.getText();
        addTest(nameTest+"Test");
        testTextField.clear();
    }

    private void addClass(String nameClass){
        if(classMap.containsKey(nameClass) || nameClass.equals("")){
            classTextField.clear();
            return;
        }

        String value = Exercise.getDefaultClassString(nameClass);
        classMap.put(nameClass, value);
        TreeItem<String> parent = addBranch(classRoot, nameClass);
        addBranch(parent, value);
    }

    private void addTest(String nameTest) {
        if(testMap.containsKey(nameTest) || nameTest.equals("")){
            testTextField.clear();
            return;
        }

        String value = Exercise.getDefaultTestString(nameTest);
        testMap.put(nameTest, value);
        TreeItem<String> parent = addBranch(testRoot, nameTest);
        addBranch(parent, value);
    }

    @FXML
    public void removeClass(){
        removeItem(classTreeView, classMap);
    }

    @FXML
    public void removeTest(){
        removeItem(testTreeView, testMap);
    }

    private void removeItem(TreeView<String> treeView, HashMap<String, String> map){
        final int selectedIndex = treeView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            TreeItem<String> itemToRemove = treeView.getSelectionModel().getSelectedItem();

            final int newSelectedIdx =
                    (selectedIndex == treeView.getChildrenUnmodifiable().size() - 1)
                            ? selectedIndex - 1
                            : selectedIndex;

            itemToRemove.getParent().getChildren().remove(itemToRemove);
            treeView.getSelectionModel().select(newSelectedIdx);

            map.remove(itemToRemove.getValue());
        }
    }

    private TreeItem<String> addBranch(TreeItem<String> parent, String name) {
        TreeItem<String> item = new TreeItem<>(name);
        parent.getChildren().add(item);
        return item;
    }

    @FXML
    public void save(){
        this.name = nameTextField.getText();
        if(name == null || path == null) {
            MainController.status.set("Error while creating Exercise: Name or Path cant be empty");
            return;
        }

        try {
            editPath();
            Exercise exercise = new Exercise(path, name);

            exercise.setDescriptionText(descTextArea.getText());

            if(classMap != null && testMap != null) exercise.setMaps(classMap, testMap);
            if(classMap != null && testMap == null) exercise.setClassMap(classMap);
            if(classMap == null && testMap != null) exercise.setTestMap(testMap);

            FileHandler.saveFile(exercise);

            controller.loadExercise(exercise);
            close();
            MainController.status.set("Exercise successfully created");
        } catch (Exception e) {
            MainController.status.set("Error while creating Exercise");
        }
    }

    @FXML
    public void close(){
        window.close();
    }
}
