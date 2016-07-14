import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by seben100 on 14.07.16.
 */
public class ModusTest {

    @Test
    public void testToCodeTest(){
        Modus mode = new Modus(Modus.Mode.Test);
        mode.nextModus();
        assertEquals(Modus.Mode.Code, mode.getCurrentMode());
    }

    @Test
    public void codeToRefTest(){
        Modus mode = new Modus(Modus.Mode.Code);
        mode.nextModus();
        assertEquals(Modus.Mode.Refactor, mode.getCurrentMode());

    }

    @Test
    public void refToTestTest(){
        Modus mode = new Modus(Modus.Mode.Refactor);
        mode.nextModus();
        assertEquals(Modus.Mode.Test, mode.getCurrentMode());
    }

    @Test
    public void codeToTestTest(){
        Modus mode = new Modus(Modus.Mode.Code);
        assertEquals(Modus.Mode.Test, mode.getPreviousMode());
    }
}
