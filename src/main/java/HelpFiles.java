import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * Created by Marcel on 08.07.16.
 */
public class HelpFiles {

    public static void showIlias() {
        try {
            Desktop.getDesktop().browse(new URI("https://ilias.uni-duesseldorf.de"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showAbgabe() {
        try {
            Desktop.getDesktop().browse(new URI("http://auas.cs.uni-duesseldorf.de"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void openHelp() {
        try {
            Desktop.getDesktop().open(new File("Benutzerhandbuch.pdf"));
        } catch (IOException e) {
            System.out.println("Datei nicht gefunden");
        }
    }


}
