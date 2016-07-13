import java.util.ArrayList;

/**
 * Created by seben100 on 08.07.16.
 */
public class Tracking {

    private int start;

    private int time;

    protected Modus.Mode currentState;

    protected int testingTime;

    protected int codingTime;

    protected int refactoringTime;

    protected int compileFailure;

    protected int testFailure;

    protected ArrayList timeList = new ArrayList<int[]>();

    protected int[] timeArray;

    public Tracking() {
        testingTime = 0;
        codingTime = 0;
        refactoringTime = 0;
        compileFailure = 0;
        testFailure = 0;
        currentState = Modus.Mode.Test;
    }

    public void setStart() {
        start = (int) (System.currentTimeMillis() / 1000);
    }

    public void switching() {
        time = (int) (System.currentTimeMillis() / 1000) - start;
        if(currentState == Modus.Mode.Test){
            testingTime = time;
            timeArray = new int[3];
            timeArray[0] = testingTime;
        }
        if(currentState == Modus.Mode.Code){
                codingTime = time;
                timeArray[1] = codingTime;
        }
        if(currentState == Modus.Mode.Refactor){
                refactoringTime = time;
                timeArray[2] = refactoringTime;
                timeList.add(timeArray);
        }
        setStart();
    }

}
