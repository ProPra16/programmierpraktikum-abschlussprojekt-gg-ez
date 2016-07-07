

public class Modus {

    static final int TEST_SCHREIBEN = 0;

    static final int CODE_SCHREIBEN = 1;

    static final int REFACTORING = 2;

    public int getCurrent_mode() {
        return current_mode;
    }

    int current_mode;

    public Modus(int mode) {
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