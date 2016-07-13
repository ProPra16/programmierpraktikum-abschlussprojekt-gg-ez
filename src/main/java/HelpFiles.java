import javafx.scene.control.Alert;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * Created by Marcel on 08.07.16.
 */
public class HelpFiles {

    public static void showIlias(){
        if (!Desktop.isDesktopSupported()){
            String s = System.getProperty("os.name").toLowerCase();
            if (s.contains("linux") || s.contains("unix")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Not supported");
                alert.setContentText("Linux: Desktop.getDesktop ist not supported");
                alert.showAndWait();
            }
            return;
        }
        try {
            Desktop.getDesktop().browse(new URI("https://ilias.uni-duesseldorf.de"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showAbgabe(){
        if (!Desktop.isDesktopSupported()){
            String s = System.getProperty("os.name").toLowerCase();
            if (s.contains("linux") || s.contains("unix")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Not supported");
                alert.setContentText("Linux: Desktop.getDesktop ist not supported");
                alert.showAndWait();
            }
            return;
        }
        if (!Desktop.isDesktopSupported()){
            return;
        }
        try {
            Desktop.getDesktop().browse(new URI("http://auas.cs.uni-duesseldorf.de"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void openHelp(){
        if (!Desktop.isDesktopSupported()){
            String s = System.getProperty("os.name").toLowerCase();
            if (s.contains("linux") || s.contains("unix")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Not supported");
                alert.setContentText("Linux: Desktop.getDesktop ist not supported");
                alert.showAndWait();
            }
            return;
        }
        if (!Desktop.isDesktopSupported()){
            return;
        }
        try {
            Desktop.getDesktop().open(new File("Benutzerhandbuch.pdf"));
        } catch (IOException e) {
            System.out.println("Datei nicht gefunden");
        }
    }

    public static void openLicense() {
        try {
            Desktop.getDesktop().browse(new URI("http://www.apache.org/licenses/LICENSE-2.0"));
        }
        catch (Exception e) {
            System.out.println("Datei nicht gefunden");
        }
    }



}
