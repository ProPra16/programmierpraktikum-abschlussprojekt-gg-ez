/**
 * Created by Marcel on 06.07.16.
 */
public class ProgramStyle {


    public static void styleDark(){
        Main.scene.getStylesheets().clear();
        Main.scene.getStylesheets().add("styleDark.css");
    }

    public static void styleFab(){
        Main.scene.getStylesheets().clear();
        Main.scene.getStylesheets().add("styleFabulous.css");
    }

    public static void styleWhite(){
        Main.scene.getStylesheets().clear();
    }


}
