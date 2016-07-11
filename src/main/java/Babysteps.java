import javafx.scene.control.Alert;

import java.util.Timer;
import java.util.TimerTask;


public class Babysteps {


    public static TimerTask task;
    public static Timer timer;



    public static void activateBabysteps(){


        task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("test"); //DEBUG
                if(BabystepsTimer.getTime() != null) BabystepsTimer.stop();
                BabystepsTimer.startTimer();




            }
        };

        timer = new Timer();

        //timer.schedule(task,BabystepsOptions.getTime()*1000, BabystepsOptions.getTime()*1000);

        timer.schedule(task,0, BabystepsOptions.getTime()*1000);





    }


    public static void stop(){
        try {
            task.cancel();
            timer.cancel();
        } catch (NullPointerException e){

        }
    }


    public static void BabystepsFehlerMeldung(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Babystep fehlgeschlagen");
        alert.setContentText("Vorheriger Schritt wird aufgerufen.");
        alert.showAndWait();
    }


}
