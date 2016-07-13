public class Modus {

    public static enum Mode {
        Test("'Test schreiben'"), Code("'Code schreiben'"), Refactor("'Refactoring'");
        private String name;
        Mode(String name) {
            this.name = name;
        }

        @Override
        public String toString(){
            return name;
        }
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

    public Mode getPreviousMode() {
        switch (currentMode) {
            case Test: return Mode.Refactor;
            case Code: return Mode.Test;
            case Refactor: return Mode.Code;
            default: return null;
        }
    }
}