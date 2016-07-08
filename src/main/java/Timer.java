import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Duration;


/**
 * Created by Marcel on 07.07.16.
 */
public class Timer {

    private static int startTime;
    private static int currentTime;
    private static Timeline time;


    public static void startTimer(){
        startTime = BabystepsOptions.getTime();

        SimpleStringProperty timeString = new SimpleStringProperty(timeString(startTime));


        MainController.time.bind(timeString);

        currentTime = startTime;

        time = new Timeline(new KeyFrame (Duration.millis(1000), e -> {
            currentTime--;
            timeString.setValue(timeString(currentTime));
        }));

        time.play();

        System.out.println("TEST");
    }

    private static String timeString(int time){
        return padString((time/60)+"")+":"+ padString((time%60)+"");
    }

    private static String padString(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = s.length(); i < 2; i++) {
            sb.append('0');
        }
        sb.append(s);

        return sb.toString();
    }

}
