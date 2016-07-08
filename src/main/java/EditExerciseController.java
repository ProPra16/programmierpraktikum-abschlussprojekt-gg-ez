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

import java.io.IOException;
import java.util.ArrayList;
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

    private ArrayList<String> addClassList = new ArrayList<>();
    private ArrayList<String> addTestList  = new ArrayList<>();
    private ArrayList<String> deleteClassList = new ArrayList<>();
    private ArrayList<String> deleteTestList  = new ArrayList<>();

    public void show(MainController controller, Exercise currentExercise) throws IOException {
        this.controller = controller;
        this.exercise = currentExercise;
        this.classMap = exercise.getClassesText();
        this.testMap = exercise.getTestsText();

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

    private TreeItem<String> addBranch(TreeItem<String> parent, String name) {
        TreeItem<String> item = new TreeItem<>(name);
        parent.getChildren().add(item);
        return item;
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
        classTextField.clear();
    }

    private void addClass(String nameClass){
        if(classMap.containsKey(nameClass) || nameClass.equals("") || addClassList.contains(nameClass)){
            classTextField.clear();
            return;
        }

        addClassList.add(nameClass);
        addBranch(classRoot, nameClass);
    }

    @FXML
    public void addTest(){
        String nameTest = testTextField.getText();
        addTest(nameTest+"Test");
        testTextField.clear();
    }

    private void addTest(String nameTest) {
        if(testMap.containsKey(nameTest) || nameTest.equals("") || addTestList.contains(nameTest)){
            classTextField.clear();
            return;
        }

        addTestList.add(nameTest);
        addBranch(testRoot, nameTest);
    }

    @FXML
    public void removeClass(){
        final int selectedIndex = classTreeView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            TreeItem<String> itemToRemove = classTreeView.getSelectionModel().getSelectedItem();

            final int newSelectedIdx =
                    (selectedIndex == classTreeView.getChildrenUnmodifiable().size() - 1)
                            ? selectedIndex - 1
                            : selectedIndex;

            itemToRemove.getParent().getChildren().remove(itemToRemove);
            classTreeView.getSelectionModel().select(newSelectedIdx);

            if(addClassList.contains(itemToRemove.getValue())){
                addClassList.remove(itemToRemove.getValue());
            }
            if(classMap.containsKey(itemToRemove.getValue())){
                deleteClassList.add(itemToRemove.getValue());
            }
        }
    }

    @FXML
    public void removeTest(){
        final int selectedIndex = testTreeView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            TreeItem<String> itemToRemove = testTreeView.getSelectionModel().getSelectedItem();

            final int newSelectedIdx =
                    (selectedIndex == testTreeView.getChildrenUnmodifiable().size() - 1)
                            ? selectedIndex - 1
                            : selectedIndex;

            itemToRemove.getParent().getChildren().remove(itemToRemove);
            testTreeView.getSelectionModel().select(newSelectedIdx);

            if(addTestList.contains(itemToRemove.getValue())){
                addTestList.remove(itemToRemove.getValue());
            }
            if(testMap.containsKey(itemToRemove.getValue())){
                deleteTestList.add(itemToRemove.getValue());
            }
        }
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
            exercise.addDescriptionText(descTextArea.getText());
            deleteClassList.forEach((s) -> {
                exercise.deleteClass(s);
            });

            deleteTestList.forEach(s -> {
                exercise.deleteTest(s);
            });

            addClassList.forEach((s) -> {
                try {
                    exercise.addDefaultClass(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            addTestList.forEach((s) -> {
                try {
                    exercise.addDefaultTest(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            exercise.saveEx();
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
