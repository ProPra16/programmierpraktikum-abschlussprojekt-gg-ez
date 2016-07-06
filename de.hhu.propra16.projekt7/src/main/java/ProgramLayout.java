/**
 * Created by Marcel on 06.07.16.
 */
public class ProgramLayout {


    public static void styleDark(){
        main.scene.getStylesheets().removeAll("styleFabulous.css");
        main.scene.getStylesheets().add("styleDark.css");
    }

    public static void styleFab(){
        main.scene.getStylesheets().removeAll("styleDark.css");
        main.scene.getStylesheets().add("styleFabulous.css");
    }

    public static void styleWhite(){
        main.scene.getStylesheets().removeAll("styleDark.css");
        main.scene.getStylesheets().removeAll("styleFabulous.css");

    }

}
