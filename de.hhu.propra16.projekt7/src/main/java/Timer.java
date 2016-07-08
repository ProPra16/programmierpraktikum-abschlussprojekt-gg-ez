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
    private static Timeline time;


    public static void startTimer(){
        startTime = BabystepsOptions.getTime();

        SimpleStringProperty timeString = new SimpleStringProperty(timer(startTime));


        MainController.time.bind(timeString);

        time = new Timeline(new KeyFrame (Duration.seconds(startTime+1), e -> {
            startTime--;
            timeString.set(timer(startTime));
        }));

        time.playFromStart();

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
