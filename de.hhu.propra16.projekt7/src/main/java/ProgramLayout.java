/**
 * Created by Marcel on 06.07.16.
 */
public class ProgramLayout {


    public static void styleDark(){
        Main.scene.getStylesheets().removeAll("styleFabulous.css");
        Main.scene.getStylesheets().add("styleDark.css");
    }

    public static void styleFab(){
        Main.scene.getStylesheets().removeAll("styleDark.css");
        Main.scene.getStylesheets().add("styleFabulous.css");
    }

    public static void styleWhite(){
        Main.scene.getStylesheets().removeAll("styleDark.css");
        Main.scene.getStylesheets().removeAll("styleFabulous.css");

    }

}
