import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.InputEvent;
import javafx.util.StringConverter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class BabystepsSpinner extends Spinner<LocalTime> {

    // Mode represents the unit that is currently being edited.
    // For convenience expose methods for incrementing and decrementing that
    // unit, and for selecting the appropriate portion in a spinner's editor
    enum Mode {

        MINUTES {
            @Override
            LocalTime increment(LocalTime time, int steps) {
                return time.plusMinutes(steps);
            }
            @Override
            void select(BabystepsSpinner spinner) {
                int minIndex = spinner.getEditor().getText().indexOf(':');
                spinner.getEditor().selectRange(0, minIndex);
            }
        },
        SECONDS {
            @Override
            LocalTime increment(LocalTime time, int steps) {
                return time.plusSeconds(steps);
            }
            @Override
            void select(BabystepsSpinner spinner) {
                int index = spinner.getEditor().getText().lastIndexOf(':');
                spinner.getEditor().selectRange(index+1, spinner.getEditor().getText().length());
            }
        };
        abstract LocalTime increment(LocalTime time, int steps);
        abstract void select(BabystepsSpinner spinner);
        LocalTime decrement(LocalTime time, int steps) {
            return increment(time, -steps);
        }
    }

    // Property containing the current editing mode:

    private final ObjectProperty<Mode> mode = new SimpleObjectProperty<>(Mode.MINUTES) ;

    public ObjectProperty<Mode> modeProperty() {
        return mode;
    }

    public final Mode getMode() {
        return modeProperty().get();
    }

    public final void setMode(Mode mode) {
        modeProperty().set(mode);
    }


    public BabystepsSpinner(LocalTime time) {
        setEditable(true);

        // Create a StringConverter for converting between the text in the
        // editor and the actual value:

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm:ss");

        StringConverter<LocalTime> localTimeConverter = new StringConverter<LocalTime>() {

            @Override
            public String toString(LocalTime time) {
                return formatter.format(time);
            }

            @Override
            public LocalTime fromString(String string) {
                String[] tokens = string.split(":");
                int minutes = getIntField(tokens, 0) ;
                int seconds = getIntField(tokens, 1);
                int totalSeconds = minutes * 60 + seconds ;
                return LocalTime.of(0,(totalSeconds / 60) % 60, seconds % 60);
            }

            private int getIntField(String[] tokens, int index) {
                if (tokens.length <= index || tokens[index].isEmpty()) {
                    return 0 ;
                }
                return Integer.parseInt(tokens[index]);
            }

        };

        // The textFormatter both manages the text <-> LocalTime conversion,
        // and vetoes any edits that are not valid. We just make sure we have
        // two colons and only digits in between:

        TextFormatter<LocalTime> textFormatter = new TextFormatter<LocalTime>(localTimeConverter, LocalTime.of(0,3,0), c -> {
            String newText = c.getControlNewText();
            if (newText.matches("[0-9]{0,2}?:[0-9]{0,2}")) {
                return c ;
            }
            return null ;
        });

        // The spinner value factory defines increment and decrement by
        // delegating to the current editing mode:

        SpinnerValueFactory<LocalTime> valueFactory = new SpinnerValueFactory<LocalTime>() {


            {
                setConverter(localTimeConverter);
                setValue(time);
            }

            @Override
            public void decrement(int steps) {
                setValue(mode.get().decrement(getValue(), steps));
                mode.get().select(BabystepsSpinner.this);
            }

            @Override
            public void increment(int steps) {
                setValue(mode.get().increment(getValue(), steps));
                mode.get().select(BabystepsSpinner.this);
            }

        };

        this.setValueFactory(valueFactory);
        this.getEditor().setTextFormatter(textFormatter);

        // Update the mode when the user interacts with the editor.
        // This is a bit of a hack, e.g. calling spinner.getEditor().positionCaret()
        // could result in incorrect state. Directly observing the caretPostion
        // didn't work well though; getting that to work properly might be
        // a better approach in the long run.
        this.getEditor().addEventHandler(InputEvent.ANY, e -> {
            int caretPos = this.getEditor().getCaretPosition();
            int minIndex = this.getEditor().getText().indexOf(':');
            if (caretPos <= minIndex) {
                mode.set( Mode.MINUTES );
            } else {
                mode.set( Mode.SECONDS );
            }
        });

        // When the mode changes, select the new portion:
        mode.addListener((obs, oldMode, newMode) -> newMode.select(this));

    }

    public BabystepsSpinner() {
        this(LocalTime.of(0,3,0));
    }
}