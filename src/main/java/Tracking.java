/**
 * Created by seben100 on 08.07.16.
 */
public class Tracking {

    private double start;

    private double time;

    protected int currentState;

    protected double testingTime;

    protected double codingTime;

    protected double refactoringTime;

    protected int compileFailure;

    protected int testFailure;

    public Tracking() {
        testingTime = 0;
        codingTime = 0;
        refactoringTime = 0;
        compileFailure = 0;
        testFailure = 0;
        currentState = 0;
    }

    public void setStart() {
        start = System.currentTimeMillis();
    }

    public void switching() {
        time = System.currentTimeMillis() - start;
        switch (currentState) {
            case 0: testingTime += time; break;
            case 1: codingTime += time; break;
            case 2: refactoringTime += time; break;
        }
    }

    public double getTime() {
        return time;
    }

}
