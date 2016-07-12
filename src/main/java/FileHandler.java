import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.nio.file.Path;

public class FileHandler {

    public static Path fileChooserOpen(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Exercise");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage stage = new Stage();

        File file = fileChooser.showOpenDialog(stage);

        if(file == null) return null;

        return file.toPath();
    }

    public static Path fileChooserSave(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Save Exercise as File");
        Stage stage = new Stage();

        File newFile = fileChooser.showSaveDialog(stage);

        if(newFile == null) return null;

        return newFile.toPath();
    }

    public static void saveFile(Exercise exercise) {
        try {
            Document doc = exercise.getDoc();

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            DOMSource src = new DOMSource(doc);

            File file = new File(exercise.getPath().toString());

            StreamResult fileResult = new StreamResult(file);
            transformer.transform(src, fileResult);

            MainController.status.set("Exercise successfully saved");
        } catch (Exception e) {
            MainController.status.set("Error while saving Exercise");
        }
    }
}
