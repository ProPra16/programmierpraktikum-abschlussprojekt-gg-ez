import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;


/**
 * Created by Marcel on 07.07.16.
 */
public class Timer {

    private static int startTime;
    private static Timeline time;


    public static void startTimer(){
        startTime = BabystepsOptions.getTime();

        IntegerProperty timeInt = new SimpleIntegerProperty();


        MainController.time.bind(timeInt);



        timeInt.set(startTime);
        time = new Timeline();

        time.getKeyFrames().add(
                new KeyFrame(Duration.seconds(startTime+1),
                        new KeyValue(timeInt, 0)));
        time.playFromStart();


        System.out.println("TEST");
    }

}
