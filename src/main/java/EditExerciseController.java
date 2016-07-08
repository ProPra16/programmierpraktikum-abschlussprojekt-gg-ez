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

    public void show(MainController controller, Exercise currentExercise) throws IOException {
        this.controller = controller;
        this.exercise = currentExercise;

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

        TreeItem<String> classRoot = new TreeItem<>();
        classTreeView = new TreeView<>(classRoot);

        TreeItem<String> testRoot = new TreeItem<>();
        testTreeView = new TreeView<>(testRoot);
    }

    private TreeItem<String> addBranch(TreeItem<String> root, String name) {
        TreeItem<String> item = new TreeItem<>(name);
        root.getChildren().add(item);
        return item;
    }

    @FXML
    public void addPair(){

    }

    @FXML
    public void addClass(){

    }

    @FXML
    public void addTest(){

    }

    @FXML
    public void removeClass(){

    }

    @FXML
    public void removeTest(){

    }

    @FXML
    public void save(){

    }

    @FXML
    public void close(){
        window.close();
    }
}
