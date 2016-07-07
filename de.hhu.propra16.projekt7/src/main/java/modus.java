

public class modus {

    private static final int TEST_SCHREIBEN = 0;

    private static final int CODE_SCHREIBEN = 1;

    private static final int REFACTORING = 2;

    private int current_mode;

    public modus(int mode) {
        this.current_mode = mode;
    }

    public void nextModus() {
        switch (current_mode) {
            case 0: current_mode = CODE_SCHREIBEN; break;
            case 1: current_mode = REFACTORING; break;
            case 2: current_mode = TEST_SCHREIBEN; break;
        }
        //to do add timer
    }
}