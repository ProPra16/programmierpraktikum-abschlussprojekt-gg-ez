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

        SimpleStringProperty timeString = new SimpleStringProperty(timeStringFormatter(startTime));
        MainController.time.bind(timeString);

        currentTime = startTime;
        time = new Timeline(new KeyFrame (Duration.millis(1000), e -> {
            currentTime--;
            timeString.setValue(timeStringFormatter(currentTime));
        }));

        time.setCycleCount(startTime);
        time.play();
    }

    private static String timeStringFormatter(int time){
        return String.format("%02d", (time/60))+":"+ String.format("%02d", (time%60));
    }

    public static void stop() {
        time.stop();
    }

    public static Timeline getTime() {
        return time;
    }
}
