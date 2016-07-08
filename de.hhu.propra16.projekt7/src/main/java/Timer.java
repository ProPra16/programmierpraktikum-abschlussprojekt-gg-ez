import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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

        SimpleStringProperty timeString = new SimpleStringProperty(timer(startTime));


        MainController.time.bind(timeString);

        currentTime = startTime;

        time = new Timeline(new KeyFrame (Duration.millis(1000), e -> {
            currentTime--;
            timeString.setValue(timer(currentTime));
        }));

        time.play();

        System.out.println("TEST");
    }

    private static String timer(int time){
        return pad((time/60)+"")+":"+pad((time%60)+"");
    }

    private static String pad(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = s.length(); i < 2; i++) {
            sb.append('0');
        }
        sb.append(s);

        return sb.toString();
    }

}