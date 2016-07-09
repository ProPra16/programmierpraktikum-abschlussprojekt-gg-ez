import java.util.Timer;
import java.util.TimerTask;


public class Babysteps {


    public static TimerTask task;
    public static Timer timer;



    public static void activateBabysteps(){


        task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("test");
                if(BabystepsTimer.getTime() != null) BabystepsTimer.stop();
                BabystepsTimer.startTimer();









            }
        };

        timer = new Timer();

        timer.schedule(task,0, BabystepsOptions.getTime()*1000);




    }


    public static void stop(){
        task.cancel();
        timer.cancel();
    }
}
