/**
 * Created by Marcel on 06.07.16.
 */
public class ProgramLayout {


    public static void styleDark(){
        main.scene.getStylesheets().add("styleDark.css");
    }



    public static void styleWhite(){
        main.scene.getStylesheets().removeAll("styleDark.css");
    }

}
