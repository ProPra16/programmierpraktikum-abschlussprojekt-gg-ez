import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.HashMap;

public class EditExerciseController {

    @FXML private TextField nameTextField;
    @FXML private TextField pairTextField;
    @FXML private TextField classTextField;
    @FXML private TextField testTextField;

    @FXML private TextArea descTextArea;

    @FXML private TreeView<String> classTreeView;
    @FXML private TreeView<String> testTreeView;

    private MainController controller;
    private Stage window;
    private Exercise exercise;

    private HashMap<String, String> classMap;
    private HashMap<String, String> testMap;

    private TreeItem<String> classRoot;
    private TreeItem<String> testRoot;

    public void show(MainController controller, Exercise currentExercise) {
        try {
            this.controller = controller;
            this.exercise = currentExercise;
            this.classMap = exercise.getClassMap();
            this.testMap = exercise.getTestMap();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditExerciseView.fxml"));
            loader.setController(this);

            Parent root = loader.load();

            this.window = new Stage();

            initialize();

            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Create New Exercise");
            window.setResizable(false);
            window.setScene(new Scene(root, 900, 600));
            window.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialize() {
        nameTextField.setText(exercise.getName());
        descTextArea.setText(exercise.getDescriptionText());

        this.classRoot = new TreeItem<>();
        classRoot.setExpanded(true);

        for (String key: classMap.keySet()) {
            TreeItem<String> item = addBranch(classRoot, key);
            addBranch(item, classMap.get(key));
        }

        classTreeView.setRoot(classRoot);


        this.testRoot = new TreeItem<>();
        testRoot.setExpanded(true);

        for (String key: testMap.keySet()) {
            TreeItem<String> item = addBranch(testRoot, key);
            addBranch(item, testMap.get(key));
        }

        testTreeView.setRoot(testRoot);
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
        String name = nameTextField.getText();
        if(name == null) {
            System.out.println("Name cant be empty");
            return;
        }

        try {
            exercise.setName(name);
            exercise.setDescriptionText(descTextArea.getText());

            if(classMap != null && testMap != null) exercise.setMaps(classMap, testMap);
            if(classMap != null && testMap == null) exercise.setClassMap(classMap);
            if(classMap == null && testMap != null) exercise.setTestMap(testMap);

            FileHandler.saveFile(exercise);

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
