import java.util.ArrayList;
import java.util.Date;

/**
 * Created by seben100 on 08.07.16.
 */
public class Tracking {

    private int start;

    private int time;

    protected int currentState;

    protected int testingTime;

    protected int codingTime;

    protected int refactoringTime;

    protected int compileFailure;

    protected int testFailure;

    protected ArrayList timeList = new ArrayList<int[]>();

    protected int[] timeArray = new int[3];

    public Tracking() {
        testingTime = 0;
        codingTime = 0;
        refactoringTime = 0;
        compileFailure = 0;
        testFailure = 0;
        currentState = 0;
    }

    public void setStart() {
        start = (int) (System.currentTimeMillis() / 1000);
    }

    public void switching() {
        time = (int) (System.currentTimeMillis() / 1000) - start;
        switch (currentState) {
            case 0:
                testingTime = time;
                timeArray[0] = testingTime;
                break;
            case 1:
                codingTime = time;
                timeArray[1] = codingTime;
                break;
            case 2:
                refactoringTime = time;
                timeArray[2] = refactoringTime;
                timeList.add(timeArray);
                break;
        }
    }

}
