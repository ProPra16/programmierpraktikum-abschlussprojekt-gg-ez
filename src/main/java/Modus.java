public class Modus {

    public static enum Mode {
        Test, Code, Refactor
    }

    public Mode currentMode;

    public Modus(Mode mode) {
        this.currentMode = mode;
    }

    public void nextModus() {
        switch (currentMode) {
            case Test: currentMode = Mode.Code ; break;
            case Code: currentMode = Mode.Refactor; break;
            case Refactor: currentMode = Mode.Test; break;

        }

    }

    public Mode getCurrentMode() {
        return currentMode;
    }

    @Override
    public String toString() {
        switch (currentMode) {
            case Test: return "Test schreiben";
            case Code: return "Code schreiben";
            case Refactor: return "Refactoring";
            default: return null;
        }
    }

    public Mode getPreviousMode() {
        switch (currentMode) {
            case Test: return Mode.Refactor;
            case Code: return Mode.Test;
            case Refactor: return Mode.Code;
            default: return null;
        }
    }
}