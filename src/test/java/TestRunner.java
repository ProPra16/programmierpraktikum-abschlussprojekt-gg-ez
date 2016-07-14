import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
    public static void main(String[] args) {
        doCompilertest();
        doModusTest();
    }

    private static void doCompilertest() {
        Result compileTestResult = JUnitCore.runClasses(CompilerTest.class);
        for (Failure failure : compileTestResult.getFailures()) {
            System.out.println(failure.toString());
        }
        if(compileTestResult.wasSuccessful()) System.out.println("Compiler Tests were successful!\n");
    }

    private static void doModusTest() {
        Result compileTestResult = JUnitCore.runClasses(ModusTest.class);
        for (Failure failure : compileTestResult.getFailures()) {
            System.out.println(failure.toString());
        }
        if(compileTestResult.wasSuccessful()) System.out.println("Modus Tests were successful!\n");
    }
}  	